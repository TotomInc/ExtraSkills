package io.totominc.ExtraSkills.skills;

import io.totominc.ExtraSkills.abilities.Ability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Skill {
  MINING(new ArrayList<>(Collections.singleton(Ability.VEIN_MINER)));

  private final List<Ability> abilities;

  Skill(List<Ability> abilities) {
    this.abilities = Collections.unmodifiableList(abilities);
  }

  public List<Ability> getAbilities() {
    return abilities;
  }
}
