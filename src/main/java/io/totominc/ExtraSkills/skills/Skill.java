package io.totominc.extraskills.skills;

import io.totominc.extraskills.abilities.Ability;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Skill {
  COMBAT(List.of(), SkillSourceType.ENTITY),
  ENCHANTER(List.of(), SkillSourceType.ITEM),
  MINING(Arrays.asList(Ability.LUCKY_MINER, Ability.HASTER), SkillSourceType.BLOCK);

  private final List<Ability> abilities;
  private final SkillSourceType sourceType;

  Skill(List<Ability> abilities, SkillSourceType sourceType) {
    this.abilities = Collections.unmodifiableList(abilities);
    this.sourceType = sourceType;
  }

  public List<Ability> getAbilities() {
    return this.abilities;
  }

  public SkillSourceType getSourceType() {
    return this.sourceType;
  }
}
