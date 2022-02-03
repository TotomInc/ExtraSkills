package io.totominc.ExtraSkills.player;

import io.totominc.ExtraSkills.player.data.JsonDataHandler;
import io.totominc.ExtraSkills.player.data.PlayerData;
import io.totominc.ExtraSkills.skills.Skill;
import io.totominc.ExtraSkills.skills.Skills;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExtraSkillsPlayer {
  private final Map<String, PlayerSkill> playerSkillMap = new HashMap<>();
  private final UUID playerUuid;

  public ExtraSkillsPlayer(UUID playerUuid) {
    this.playerUuid = playerUuid;

    for (Skill skill : Skills.values()) {
      String skillId = skill.getID();

      playerSkillMap.put(skillId, new PlayerSkill(skillId));
    }

    this.load();
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

  /**
   * Save the values of this instance into a .json file.
   */
  public void save() {
    try {
      JsonDataHandler.savePlayerState(this.playerSkillMap, this.playerUuid);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Load the player state from the save file if the user has one.
   */
  public void load() {
    try {
      PlayerData playerData = JsonDataHandler.loadPlayerState(this.playerUuid);

      if (playerData != null) {
        for (PlayerSkill playerSkill : playerData.playerSkillMap.values()) {
          playerSkillMap.replace(playerSkill.id, playerSkill);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}