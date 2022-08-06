package io.totominc.ExtraSkills.leveler;

import com.udojava.evalex.Expression;
import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.configuration.Option;
import io.totominc.ExtraSkills.data.PlayerData;
import io.totominc.ExtraSkills.skills.Skill;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public final class Leveler {
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

    System.out.println("Leveler.addExperience: " + ExtraSkills.getInstance().getOptionManager().getBoolean(Option.ACTION_BAR_ENABLED));

    if (
      ExtraSkills.getInstance().getOptionManager().getBoolean(Option.ACTION_BAR_ENABLED) &&
      ExtraSkills.getInstance().getOptionManager().getBoolean(Option.ACTION_BAR_ENABLE_SKILL_EXPERIENCE)
    ) {
      ExtraSkills.getAdventure().player(player).sendActionBar(
        playerData.getSkillExperienceMessage(skill, experience)
      );
    }
  }
}
