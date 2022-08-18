package io.totominc.extraskills.data;

import com.udojava.evalex.Expression;
import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.abilities.Ability;
import io.totominc.extraskills.abilities.AbilityOption;
import io.totominc.extraskills.configuration.Option;
import io.totominc.extraskills.skills.Skill;
import io.totominc.extraskills.utils.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.text.StringSubstitutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class PlayerData {
  private final ExtraSkills instance;
  private final Player player;
  private final Map<Skill, PlayerSkillData> playerSkillDataMap = new HashMap<>();

  public PlayerData(ExtraSkills instance, Player player) {
    this.instance = instance;
    this.player = player;

    for (Skill skill : Skill.values()) {
      this.playerSkillDataMap.put(skill, new PlayerSkillData(this.calculateSkillExperienceRequired(skill, 1)));
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

    // Max-level for this skill. If its value is set to 0, it's an infinite value.
    double maxLevel = this.instance.getSkillManager().getSkillOption(skill).maxLevel();

    // Don't add experience if player is at max-level.
    if (maxLevel > 0 && skillData.getLevel() >= maxLevel) {
      System.out.println("PlayerData.addSkillExperience: skill \"" + skill.toString() + "\" at max level, not adding experience");
      return;
    }

    skillData.addExperience(amount);
  }

  public double trySkillLevelup(Skill skill) {
    PlayerSkillData skillData = playerSkillDataMap.get(skill);

    if (skillData == null) {
      System.out.println("PlayerData.trySkillLevelup: no skill found \"" + skill.toString() + "\"");
      return 0;
    }

    // Max-level for this skill. If its value is set to 0, it's an infinite value.
    double maxLevel = this.instance.getSkillManager().getSkillOption(skill).maxLevel();

    // Don't try to level-up if the player is already at the max-level.
    if (maxLevel > 0 && skillData.getLevel() >= maxLevel) {
      System.out.println("PlayerData.trySkillLevelup: player already at max skill \"" + skill.toString() + "\" level");
      return 0;
    }

    double levelsGained = 0;

    // Level-up as much as possible.
    while (skillData.getExperience() >= skillData.getExperienceRequired()) {
      levelsGained += 1;

      skillData.removeExperience(skillData.getExperienceRequired());
      skillData.addLevels(1);
      skillData.setExperienceRequired(
        this.calculateSkillExperienceRequired(skill, skillData.getLevel())
      );
    }

    // After a level-up on the max-level, make sure to reset experience.
    if (maxLevel > 0 && skillData.getLevel() == maxLevel) {
      skillData.setExperience(0);
    }

    return levelsGained;
  }

  public void setSkillLevel(Skill skill, double level) {
    PlayerSkillData skillData = playerSkillDataMap.get(skill);

    if (skillData == null) {
      System.out.println("PlayerData.setSkillLevel: no skill found \"" + skill.toString() + "\"");
      return;
    }

    skillData.setLevel(level);
  }

  public double getSkillLevel(Skill skill) {
    PlayerSkillData skillData = playerSkillDataMap.get(skill);

    if (skillData == null) {
      System.out.println("PlayerData.getSkillLevel: no skill found \"" + skill.toString() + "\"");
      return 1d;
    }

    return skillData.getLevel();
  }

  public void setSkillExperience(Skill skill, double experience) {
    PlayerSkillData skillData = playerSkillDataMap.get(skill);

    if (skillData == null) {
      System.out.println("PlayerData.setSkillLevel: no skill found \"" + skill.toString() + "\"");
      return;
    }

    skillData.setExperience(experience);
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

  public Component getActionBarSkillExperienceMessage(Skill skill, double reward) {
    String template = ExtraSkills.getInstance().getOptionManager().getString(Option.ACTION_BAR_SKILL_EXPERIENCE_FORMAT);

    return MiniMessage.miniMessage().deserialize(this.parseSkillExperienceMessage(skill, template, reward));
  }

  public Component getBossBarSkillExperienceMessage(Skill skill, double reward) {
    String template = ExtraSkills.getInstance().getOptionManager().getString(Option.BOSS_BAR_SKILL_EXPERIENCE_FORMAT);

    return MiniMessage.miniMessage().deserialize(this.parseSkillExperienceMessage(skill, template, reward));
  }

  public Component getTitleSkillLevelUpMessage(Skill skill, Option option) {
    String template = ExtraSkills.getInstance().getOptionManager().getString(option);

    return MiniMessage.miniMessage().deserialize(this.parseSkillLevelUpMessage(skill, template));
  }

  private String parseSkillExperienceMessage(Skill skill, String message, double reward) {
    Map<String, String> values = new HashMap<>();
    StringSubstitutor stringSubstitutor = new StringSubstitutor(values, "{", "}");

    values.put("reward", TextUtils.get2DecimalsString(reward));
    values.put("skill", TextUtils.getCapitalizedString(skill.name()));
    values.put("skill_capitalize", skill.name().toUpperCase());
    values.put("skill_lowercase", skill.name().toLowerCase());
    values.put("experience", TextUtils.getDoubleWithoutDecimals(this.getSkillExperience(skill)));
    values.put("experience_2f", TextUtils.get2DecimalsString(this.getSkillExperience(skill)));
    values.put("experience_required", TextUtils.getDoubleWithoutDecimals(this.getSkillExperienceRequired(skill)));
    values.put("experience_required_2f", TextUtils.get2DecimalsString(this.getSkillExperienceRequired(skill)));
    values.put("level", TextUtils.getDoubleWithoutDecimals(this.getSkillLevel(skill)));
    values.put("player_name", this.player.getDisplayName());

    return stringSubstitutor.replace(message);
  }

  private String parseSkillLevelUpMessage(Skill skill, String message) {
    Map<String, String> values = new HashMap<>();
    StringSubstitutor stringSubstitutor = new StringSubstitutor(values, "{", "}");

    values.put("skill", TextUtils.getCapitalizedString(skill.name()));
    values.put("skill_capitalize", skill.name().toUpperCase());
    values.put("skill_lowercase", skill.name().toLowerCase());
    values.put("experience", TextUtils.getDoubleWithoutDecimals(this.getSkillExperience(skill)));
    values.put("experience_2f", TextUtils.get2DecimalsString(this.getSkillExperience(skill)));
    values.put("experience_required", TextUtils.getDoubleWithoutDecimals(this.getSkillExperienceRequired(skill)));
    values.put("experience_required_2f", TextUtils.get2DecimalsString(this.getSkillExperienceRequired(skill)));
    values.put("level", TextUtils.getDoubleWithoutDecimals(this.getSkillLevel(skill)));
    values.put("player_name", this.player.getDisplayName());

    return stringSubstitutor.replace(message);
  }

  /**
   * Use string expression to calculate experience required for a specific skill.
   */
  private double calculateSkillExperienceRequired(Skill skill, double skillLevel) {
    Expression expression = ExtraSkills.getInstance().getLeveler().getExperienceExpression(skill);

    if (ExtraSkills.getInstance().getOptionManager().getBoolean(Option.USE_GLOBAL_EXPERIENCE_EXPRESSION)) {
      expression = ExtraSkills.getInstance().getLeveler().getExperienceExpression();
    }

    expression.setVariable("level", BigDecimal.valueOf(skillLevel));

    return expression.eval().doubleValue();
  }
}
