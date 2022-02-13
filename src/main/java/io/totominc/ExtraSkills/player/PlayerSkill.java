package io.totominc.ExtraSkills.player;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.config.skilllevelup.BroadcastConfig;
import io.totominc.ExtraSkills.config.skilllevelup.ChatConfig;
import io.totominc.ExtraSkills.config.skilllevelup.SoundConfig;
import io.totominc.ExtraSkills.config.skillprogression.SkillProgressionConfig;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public final class PlayerSkill {
  private final ExtraSkillsPlayer extraSkillsPlayer;
  private final SkillProgressionConfig skillProgressionConfig;
  private final String id;
  private int level = 1;
  private double experience = 0;
  private BukkitTask previousTask;

  public PlayerSkill(ExtraSkillsPlayer extraSkillsPlayer, String id) {
    this.extraSkillsPlayer = extraSkillsPlayer;
    this.skillProgressionConfig = ExtraSkills.getPluginConfig().getSkillProgressionConfig();
    this.id = id;
  }

  /**
   * Identifier / name of this skill.
   *
   * @return Name of the skill used as an identifier.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Add experience and call levelup method.
   *
   * @param amount Amount of experience to gain.
   */
  public void gainExperience(Double amount) {
    this.experience += amount;
    this.levelup();
  }

  /**
   * Try to levelup as much as possible.
   */
  public void levelup() {
    boolean hasLevelupOnce = false;

    while (this.experience >= this.getExperienceRequired()) {
      hasLevelupOnce = true;
      this.experience -= this.getExperienceRequired();
      this.level += 1;
    }

    if (hasLevelupOnce) {
      this.sendLevelUpSound();
      this.sendLevelUpChat();
      this.sendLevelUpBroadcast();
    }
  }

  /**
   * Amount of experience for this skill.
   *
   * @return Experience amount.
   */
  public double getExperience() {
    return this.experience;
  }

  /**
   * Return the amount of experience required for the current level.
   *
   * @return Amount of experience required to levelup.
   */
  public double getExperienceRequired() {
    return this.getExperienceRequired(this.level);
  }

  /**
   * Return the amount of experience required for a specific level.
   *
   * @param level Level to check experience required for.
   * @return Amount of experience required for the specific level.
   */
  public double getExperienceRequired(int level) {
    return 100 * Math.pow(1.05, level - 1);
  }

  /**
   * Return the percentage progression for the next levelup.
   *
   * @return Percentage to next levelup.
   */
  public double getExperiencePercentage() {
    return this.experience / this.getExperienceRequired() * 100;
  }

  /**
   * Current level for this skill.
   *
   * @return Skill level.
   */
  public int getLevel() {
    return this.level;
  }

  /**
   * Set the current level.
   *
   * @param level Level to set.
   */
  public void setLevel(int level) {
    this.level = level;
  }

  /**
   * Set the current amount of experience.
   *
   * @param experience Experience amount.
   */
  public void setExperience(double experience) {
    this.experience = experience;
  }

  /**
   * Send an action-bar to the player with the message format defined in the
   * config.yml: skill-progression.action-bar.format
   */
  public void sendActionBar() {
    if (skillProgressionConfig.getActionBarConfig().isEnabled()) {
      String message = skillProgressionConfig.getActionBarConfig().format();
      Component component = MiniMessage.get().deserialize(this.interpolateSkillMessage(message));

      ExtraSkills.getAdventure().player(this.extraSkillsPlayer.getPlayerUuid()).sendActionBar(component);
    }
  }

  /**
   * Send a boss-bar to the player with the message format defined in the
   * config.yml: skill-progression.boss-bar.format
   */
  public void sendBossBar() {
    if (skillProgressionConfig.getBossBarConfig().isEnabled()) {
      // ExtraSkillsPlayer reference used inside Bukkit Runnable Task.
      ExtraSkillsPlayer extraSkillsPlayer = this.extraSkillsPlayer;
      Audience playerAudience = ExtraSkills.getAdventure().player(this.extraSkillsPlayer.getPlayerUuid());

      if (this.previousTask != null && !this.previousTask.isCancelled()) {
        this.previousTask.cancel();
        this.previousTask = null;
      }

      playerAudience.hideBossBar(this.extraSkillsPlayer.getBossBar());

      this.extraSkillsPlayer.setBossBar(this.generateBossBar());

      playerAudience.showBossBar(this.extraSkillsPlayer.getBossBar());

      // Runnable Task that will automatically hide the boss-bar after X seconds
      // as defined in the configuration file.
      this.previousTask = new BukkitRunnable() {
        @Override
        public void run() {
          playerAudience.hideBossBar(extraSkillsPlayer.getBossBar());
        }
      }.runTaskLater(
        ExtraSkills.getInstance(),
        ExtraSkills.getPluginConfig().getSkillProgressionConfig().getBossBarConfig().removeAfterSeconds() * 20
      );
    }
  }

  /**
   * Replace the placeholder content of a message with real values. Possible
   * values are:
   * <ul>
   *   <li>skill_name: name of the skill.</li>
   *   <li>current_exp: current amount of experience.</li>
   *   <li>exp_required: amount of experience required to levelup.</li>
   *   <li>exp_percentage: percentage progression to the next levelup.</li>
   *   <li>level: current skill level.</li>
   *   <li>player_name: name of the player associated to this PlayerSkill instance.</li>
   * </ul>
   *
   * @param message Message to interpolate values to.
   * @return An interpolated message.
   */
  private String interpolateSkillMessage(String message) {
    Map<String, String> values = new HashMap<>();
    StrSubstitutor substitutor = new StrSubstitutor(values, "{", "}");
    Player player = Bukkit.getPlayer(this.extraSkillsPlayer.getPlayerUuid());

    // Make sure the skill name is properly formatted with a first letter in uppercase.
    values.put("skill_name", this.id.substring(0, 1).toUpperCase() + this.id.substring(1).toLowerCase());
    values.put("current_exp", String.format("%.2f", this.experience));
    values.put("exp_required", String.format("%.2f", this.getExperienceRequired()));
    values.put("exp_percentage", String.format("%.2f", this.getExperiencePercentage()));
    values.put("level", String.valueOf(this.getLevel()));

    if (player != null) {
      values.put("player_name", player.getDisplayName());
    }

    return substitutor.replace(message);
  }

  /**
   * Send a sound specified on the skill-configuration section of the config.yml
   * when a player levelup its skill.
   */
  private void sendLevelUpSound() {
    Player player = Bukkit.getPlayer(this.extraSkillsPlayer.getPlayerUuid());
    SoundConfig soundConfig = ExtraSkills.getPluginConfig().getSkillLevelupConfig().getSoundConfig();

    if (player != null) {
      player.playSound(player, Sound.valueOf(soundConfig.name()), soundConfig.volume(), soundConfig.pitch());
    }
  }

  /**
   * Send a chat message only to the specified player when he level-up its skill.
   */
  private void sendLevelUpChat() {
    Player player = Bukkit.getPlayer(this.extraSkillsPlayer.getPlayerUuid());
    ChatConfig chatConfig = ExtraSkills.getPluginConfig().getSkillLevelupConfig().getChatConfig();

    if (player != null) {
      ExtraSkills.getAdventure().player(player).sendMessage(MiniMessage.get().deserialize(this.interpolateSkillMessage(chatConfig.format())));
    }
  }

  /**
   * Send a chat message to all online players of the server when the player
   * level-up its skill.
   */
  private void sendLevelUpBroadcast() {
    BroadcastConfig broadcastConfig = ExtraSkills.getPluginConfig().getSkillLevelupConfig().getBroadcastConfig();
    boolean hasLevel = false;

    for (Integer lvl : broadcastConfig.levels()) {
      if (level == lvl) {
        hasLevel = true;
        break;
      }
    }

    Player player = Bukkit.getPlayer(this.extraSkillsPlayer.getPlayerUuid());

    if (player != null && hasLevel) {
      ExtraSkills.getAdventure().all().sendMessage(MiniMessage.get().deserialize(this.interpolateSkillMessage(broadcastConfig.format())));
    }
  }

  /**
   * Generate a boss-bar for the current PlayerSkill.
   *
   * @return BossBar instance.
   */
  private BossBar generateBossBar() {
    return BossBar.bossBar(
      MiniMessage.get().deserialize(this.interpolateSkillMessage(this.skillProgressionConfig.getBossBarConfig().format())),
      (float) this.getExperiencePercentage() / 100,
      this.skillProgressionConfig.getBossBarConfig().color(),
      this.skillProgressionConfig.getBossBarConfig().segments()
    );
  }
}
