package io.totominc.ExtraSkills.data.storage;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.data.PlayerData;
import org.bukkit.entity.Player;

public abstract class PlayerStorageManager {
  public final ExtraSkills instance;

  public PlayerStorageManager(ExtraSkills instance) {
    this.instance = instance;
  }

  public PlayerData createPlayerData(Player player) {
    PlayerData playerData = new PlayerData(player);

    ExtraSkills.getInstance().getPlayerDataManager().addPlayerData(player.getUniqueId(), playerData);

    return playerData;
  }

  public abstract void loadPlayerData(Player player);

  public abstract void savePlayerData(Player player, boolean isLeaving);
}
