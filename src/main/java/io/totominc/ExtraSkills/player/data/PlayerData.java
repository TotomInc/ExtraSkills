package io.totominc.ExtraSkills.player.data;

import java.util.Map;
import java.util.UUID;

class PlayerSkillData {
  public int level;
  public double experience;
}

public class PlayerData {
  public Map<String, PlayerSkillData> playerSkillDataMap;
  public UUID playerUuid;


}
