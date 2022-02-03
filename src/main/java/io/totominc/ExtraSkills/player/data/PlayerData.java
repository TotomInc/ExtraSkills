package io.totominc.ExtraSkills.player.data;

import io.totominc.ExtraSkills.player.PlayerSkill;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
  public final UUID playerUuid;
  public final Map<String, PlayerSkill> playerSkillMap = new HashMap<>();
  public final Map<String, PlayerSkillData> playerSkillDataMap = new HashMap<>();

  public PlayerData(UUID playerUuid, Map<String, PlayerSkill> playerSkillMap) {
    this.playerUuid = playerUuid;

    for (PlayerSkill playerSkill : playerSkillMap.values()) {
      this.playerSkillMap.put(playerSkill.id, playerSkill);
      this.playerSkillDataMap.put(playerSkill.id, new PlayerSkillData(playerSkill.id, playerSkill.level, playerSkill.experience));
    }
  }

  /**
   * Convert a PlayerData class into a JSONObject.
   *
   * @return JSONObject containing PlayerData state.
   */
  public JSONObject toJSON() {
    JSONObject root = new JSONObject();

    root.put("uuid", this.playerUuid);
    root.put("skills", this.getPlayerSkillDataToJSON());

    return root;
  }

  /**
   * Convert PlayerSkillData map into a JSONObject.
   *
   * @return JSONObject containing PlayerSkillData map state.
   */
  private JSONObject getPlayerSkillDataToJSON() {
    JSONObject skills = new JSONObject();

    for (PlayerSkillData playerSkillData : playerSkillDataMap.values()) {
      JSONObject skill = new JSONObject();

      skill.put("level", playerSkillData.level());
      skill.put("experience", playerSkillData.experience());
      skills.put(playerSkillData.id(), skill);
    }

    return skills;
  }
}
