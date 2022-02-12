package io.totominc.ExtraSkills.player;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.config.skillprogression.SoundConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerSkill {
  private final ExtraSkillsPlayer extraSkillsPlayer;
  private final String id;
  private int level = 1;
  private double experience = 0;

  public PlayerSkill(ExtraSkillsPlayer extraSkillsPlayer, String id) {
    this.extraSkillsPlayer = extraSkillsPlayer;
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
      this.sendLevelupSound();
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
   * <p>
   * Only send the action-bar if it has been enabled in the config.yml.
   *
   * @param playerUuid Bukkit Player UUID.
   */
  public void sendActionBar(@NotNull UUID playerUuid) {
    if (ExtraSkills.getPluginConfig().getSkillProgressionConfig().getActionBarConfig().isEnabled()) {
      String message = ExtraSkills.getPluginConfig().getSkillProgressionConfig().getActionBarConfig().format();
      Component component = MiniMessage.get().deserialize(this.interpolateSkillMessage(message));

      ExtraSkills.getAdventure().player(playerUuid).sendActionBar(component);
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
   * </ul>
   *
   * @param message Message to interpolate values to.
   * @return An interpolated message.
   */
  private String interpolateSkillMessage(String message) {
    Map<String, String> values = new HashMap<>();
    StrSubstitutor substitutor = new StrSubstitutor(values, "{", "}");

    // Make sure the skill name is properly formatted with first letter uppercase.
    values.put("skill_name", this.id.substring(0, 1).toUpperCase() + this.id.substring(1).toLowerCase());
    values.put("current_exp", String.format("%.2f", this.experience));
    values.put("exp_required", String.format("%.2f", this.getExperienceRequired()));
    values.put("exp_percentage", String.format("%.2f", this.getExperiencePercentage()));

    return substitutor.replace(message);
  }

  /**
   * Send a sound specified on the skill-configuration section of the config.yml
   * when a player levelup its skill.
   */
  private void sendLevelupSound() {
    Player player = Bukkit.getPlayer(this.extraSkillsPlayer.getPlayerUuid());
    SoundConfig soundConfig = ExtraSkills.getPluginConfig().getSkillProgressionConfig().getSoundConfig();

    if (player != null) {
      player.playSound(player, Sound.valueOf(soundConfig.name()), soundConfig.volume(), soundConfig.pitch());
    }
  }
}
