package io.totominc.extraskills.data;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerDataManager {
  private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

  @Nullable
  public PlayerData getPlayerData(UUID uuid) {
    return playerDataMap.get(uuid);
  }

  public void addPlayerData(UUID uuid, PlayerData playerData) {
    if (!playerDataMap.containsKey(uuid)) {
      playerDataMap.put(uuid, playerData);
    }
  }

  public void removePlayerData(UUID uuid) {
    playerDataMap.remove(uuid);
  }
}
