package io.totominc.ExtraSkills.abilities;

import io.totominc.ExtraSkills.skills.Skill;

public enum Ability {
  VEIN_MINER(Skill.MINING, 1, 1);

  private final Skill associatedSkill;
  private final double baseValue;
  private final double baseValueGainedPerLevel;

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
}
