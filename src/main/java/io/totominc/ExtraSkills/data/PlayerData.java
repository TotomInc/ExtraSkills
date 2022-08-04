package io.totominc.ExtraSkills.data;

import com.udojava.evalex.Expression;
import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.abilities.Ability;
import io.totominc.ExtraSkills.abilities.AbilityOption;
import io.totominc.ExtraSkills.skills.Skill;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class PlayerData {
  private final Player player;
  private final Map<Skill, PlayerSkillData> playerSkillDataMap = new HashMap<>();

  public PlayerData(Player player) {
    this.player = player;

    this.loadSkillDataMap();
  }

  // TODO: implement loading player skill-data save.
  public void loadSkillDataMap() {
    for (Skill skill : Skill.values()) {
      this.playerSkillDataMap.put(skill, new PlayerSkillData(this.calculateSkillExperienceRequired(1)));
    }
  }

  @Nullable
  public AbilityOption getAbilityOption(Ability ability) {
    return ExtraSkills.getInstance().getAbilityManager().getAbilityOption(ability);
  }

  public double getAbilityLevel(Ability ability) {
    AbilityOption abilityOption = ExtraSkills.getInstance().getAbilityManager().getAbilityOption(ability);

    return this.getSkillLevel(ability.getAssociatedSkill()) / abilityOption.levelUpRate();
  }

  public double getAbilityValue(Ability ability) {
    AbilityOption abilityOption = ExtraSkills.getInstance().getAbilityManager().getAbilityOption(ability);

    return abilityOption.baseValue() + this.getAbilityLevel(ability) * abilityOption.valueGainedPerLevel();
  }

  public void addSkillExperience(Skill skill, double amount) {
    PlayerSkillData skillData = playerSkillDataMap.get(skill);

    if (skillData == null) {
      System.out.println("PlayerData.addSkillExperience: no skill found \"" + skill.toString() + "\"");
      return;
    }

    skillData.addExperience(amount);
  }

  // TODO: implement skill max-level.
  public void trySkillLevelup(Skill skill) {
    PlayerSkillData skillData = playerSkillDataMap.get(skill);

    if (skillData == null) {
      System.out.println("PlayerData.trySkillLevelup: no skill found \"" + skill.toString() + "\"");
      return;
    }

    while (skillData.getExperience() >= skillData.getExperienceRequired()) {
      skillData.removeExperience(skillData.getExperienceRequired());
      skillData.addLevels(1);
      skillData.setExperienceRequired(this.calculateSkillExperienceRequired(skillData.getLevel()));
    }
  }

  public double getSkillLevel(Skill skill) {
    PlayerSkillData skillData = playerSkillDataMap.get(skill);

    if (skillData == null) {
      System.out.println("PlayerData.getSkillLevel: no skill found \"" + skill.toString() + "\"");
      return 1d;
    }

    return skillData.getLevel();
  }

  public double getSkillExperience(Skill skill) {
    PlayerSkillData skillData = playerSkillDataMap.get(skill);

    if (skillData == null) {
      System.out.println("PlayerData.getSkillExperience: no skill found \"" + skill.toString() + "\"");
      return 100d;
    }

    return skillData.getExperience();
  }

  public double getSkillExperienceRequired(Skill skill) {
    PlayerSkillData skillData = playerSkillDataMap.get(skill);

    if (skillData == null) {
      System.out.println("PlayerData.getSkillExperienceRequired: no skill found \"" + skill.toString() + "\"");
      return 100d;
    }

    return skillData.getExperienceRequired();
  }

  /**
   * Use string expression to calculate experience required for a specific skill.
   */
  private double calculateSkillExperienceRequired(double level) {
    // TODO: load expression values from config file.
    double base = 100d;
    double power = 1.33d;

    Expression expression = ExtraSkills.getInstance().getLeveler().getExperienceExpression();

    expression.setVariable("level", BigDecimal.valueOf(level));
    expression.setVariable("base", BigDecimal.valueOf(base));
    expression.setVariable("power", BigDecimal.valueOf(power));

    return expression.eval().doubleValue();
  }
}
