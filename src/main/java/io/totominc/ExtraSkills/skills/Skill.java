package io.totominc.extraskills.skills;

import io.totominc.extraskills.abilities.Ability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Skill {
  COMBAT(new ArrayList<>(Collections.emptyList()), SkillSourceType.ENTITY),
  ENCHANTER(new ArrayList<>(), SkillSourceType.ITEM),
  MINING(new ArrayList<>(Collections.singleton(Ability.LUCKY_MINER)), SkillSourceType.BLOCK);

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
