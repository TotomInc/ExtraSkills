package io.totominc.ExtraSkills.abilities;

import io.totominc.ExtraSkills.skills.Skill;

public enum Ability {
  LUCKY_MINER(Skill.MINING);

  private final Skill associatedSkill;

  Ability(Skill associatedSkill) {
    this.associatedSkill = associatedSkill;
  }

  public Skill getAssociatedSkill() {
    return associatedSkill;
  }
}
