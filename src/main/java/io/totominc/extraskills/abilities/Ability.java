package io.totominc.extraskills.abilities;

import io.totominc.extraskills.skills.Skill;

public enum Ability {
  LUCKY_MINER(Skill.MINING, true, false),
  HASTER(Skill.MINING, true, false),
  FARSEEING(Skill.MINING, true, false),
  SOOTHSAYER(Skill.ENCHANTER, false, false);

  private final Skill associatedSkill;
  private final boolean hasBlockConfig;
  private final boolean hasEntityConfig;

  Ability(Skill associatedSkill, boolean hasBlockConfig, boolean hasEntityConfig) {
    this.associatedSkill = associatedSkill;
    this.hasBlockConfig = hasBlockConfig;
    this.hasEntityConfig = hasEntityConfig;
  }

  public Skill getAssociatedSkill() {
    return this.associatedSkill;
  }

  public boolean getHasBlockConfig() {
    return this.hasBlockConfig;
  }

  public boolean getHasEntityConfig() {
    return this.hasEntityConfig;
  }
}
