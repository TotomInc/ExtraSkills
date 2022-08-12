package io.totominc.ExtraSkills.leveler;

import com.udojava.evalex.Expression;
import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.configuration.Option;
import io.totominc.ExtraSkills.data.PlayerData;
import io.totominc.ExtraSkills.skills.Skill;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;

public final class Leveler {
  private final ExtraSkills instance;

  public Leveler(ExtraSkills instance) {
    this.instance = instance;
  }

  /**
   * EvalEx expression representation from a string.
   *
   * @see <a href="https://github.com/uklimaschewski/EvalEx">EvalEx</a>
   */
  public Expression getExperienceExpression(Skill skill) {
    return new Expression(ExtraSkills.getInstance().getSkillManager().getSkillOption(skill).experienceExpression());
  }

  // TODO: Implement experience multiplier per skill with permissions.
  public double getMultiplier() {
    return 1d;
  }

  /**
   * Add experience to a specific skill, try to level-up the skill and send
   * action-bar to player with skill progression.
   */
  public void addExperience(Player player, Skill skill, double amount) {
    PlayerData playerData = ExtraSkills.getInstance().getPlayerDataManager().getPlayerData(player.getUniqueId());

    // Handle possible case if PlayerData is not set for current player.
    if (playerData == null) {
      return;
    }

    // Handle possible case if experience amount is set to 0. We want to allow possible
    // negative values to subtract experience from a player skill.
    if (amount == 0) {
      return;
    }

    double experience = amount * getMultiplier();

    playerData.addSkillExperience(skill, experience);
    playerData.trySkillLevelup(skill);

    if (
      this.instance.getOptionManager().getBoolean(Option.ACTION_BAR_ENABLED) &&
      this.instance.getOptionManager().getBoolean(Option.ACTION_BAR_ENABLE_SKILL_EXPERIENCE)
    ) {
      ExtraSkills.getAdventure().player(player).sendActionBar(
        playerData.getActionBarSkillExperienceMessage(skill, experience)
      );
    }

    if (
      this.instance.getOptionManager().getBoolean(Option.BOSS_BAR_ENABLED) &&
      this.instance.getOptionManager().getBoolean(Option.BOSS_BAR_ENABLE_SKILL_EXPERIENCE)
    ) {
      float progression = (float) (playerData.getSkillExperience(skill) / playerData.getSkillExperienceRequired(skill));
      BossBar.Color bossBarColor = BossBar.Color.valueOf(Option.BOSS_BAR_COLOR.name());
      BossBar.Overlay bossBarOverlay = BossBar.Overlay.valueOf(Option.BOSS_BAR_TYPE.name());

      ExtraSkills.getAdventure().player(player).showBossBar(
        BossBar.bossBar(
          playerData.getBossBarSkillExperienceMessage(skill, experience),
          progression,
          bossBarColor,
          bossBarOverlay
        )
      );
    }
  }
}
