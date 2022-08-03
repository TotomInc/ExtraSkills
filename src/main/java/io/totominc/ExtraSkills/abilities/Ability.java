package io.totominc.ExtraSkills.abilities;

import io.totominc.ExtraSkills.skills.Skill;

public enum Ability {
  LUCKY_MINER(Skill.MINING, 2, 2);

  private final Skill associatedSkill;
  private final double baseValue;
  private final double baseValueGainedPerLevel;

  /**
   * Ability level is calculated from skill levels.
   * levelUpRate = 2 means skillLevel = 10 : abilityLevel = 5.
   */
  private final double levelUpRate = 5;

  Ability(Skill associatedSkill, double baseValue, double baseValueGainedPerLevel) {
    this.associatedSkill = associatedSkill;
    this.baseValue = baseValue;
    this.baseValueGainedPerLevel = baseValueGainedPerLevel;
  }

  public Skill getAssociatedSkill() {
    return associatedSkill;
  }

  public double getBaseValue() {
    return baseValue;
  }

  public double getBaseValueGainedPerLevel() {
    return baseValueGainedPerLevel;
  }

  /**
   * Level-up rate is a coefficient used to determine ability level based on
   * the skill level.
   *
   * Formula used should be <code>skillLevel / abilityLevelUpRate</code>
   */
  public double getLevelUpRate() {
    return this.levelUpRate;
  }
}
