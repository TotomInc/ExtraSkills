package io.totominc.ExtraSkills.player;

import io.totominc.ExtraSkills.skills.Skill;
import io.totominc.ExtraSkills.skills.Skills;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class ExtraSkillsPlayer {
  private final Map<String, PlayerSkill> playerSkillMap = new HashMap<>();

  public ExtraSkillsPlayer() {
    for (Skill skill : Skills.values()) {
      String skillId = skill.getID();

      playerSkillMap.put(skillId, new PlayerSkill(skillId));
    }
  }

  /**
   * Add experience to a specific skill.
   *
   * @param skill Skill.
   * @param amount Amount of experience to gain.
   */
  public void gainSkillExperience(@NotNull Skill skill, @NotNull Double amount) {
    PlayerSkill playerSkill = this.playerSkillMap.get(skill.getID());

    if (playerSkill != null) {
      playerSkill.gainExperience(amount);
    }
  }
}
