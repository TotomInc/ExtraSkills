package io.totominc.ExtraSkills.leveler;

import com.udojava.evalex.Expression;
import io.totominc.ExtraSkills.ExtraSkills;
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
  // TODO: load experience expression from config.
  public Expression getExperienceExpression() {
    return new Expression("IF(level == 1, base, base * level * power)");
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

    playerData.addSkillExperience(skill, amount * getMultiplier());
    playerData.trySkillLevelup(skill);

    ExtraSkills.getAdventure().player(player).sendActionBar(
      Component.text(skill.name() + " lvl. " + playerData.getSkillLevel(skill) + " exp. " + playerData.getSkillExperience(skill) + "/" + playerData.getSkillExperienceRequired(skill))
    );
  }
}
