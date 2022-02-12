package io.totominc.ExtraSkills.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ExtraSkillsPlayerManager {
  private static final Map<UUID, ExtraSkillsPlayer> players = new HashMap<>();

  /**
   * Register a collection of players.
   *
   * @param players Multiple Bukkit Player instances.
   */
  public static void addPlayers(@NotNull Collection<? extends Player> players) {
    for (Player player : players) {
      addPlayer(player);
    }
  }

  /**
   * Register a player inside the players HashMap using its UUID as a key.
   *
   * @param player Bukkit Player instance.
   */
  public static void addPlayer(@NotNull Player player) {
    UUID playerUuid = player.getUniqueId();

    if (!players.containsKey(playerUuid)) {
      players.put(playerUuid, new ExtraSkillsPlayer(playerUuid));
    }
  }

  /**
   * Get an ExtraSkillsPlayer based on its Bukkit Player entity.
   *
   * @param player Bukkit Player entity.
   * @return ExtraSkillsPlayer instance.
   */
  public static ExtraSkillsPlayer getPlayer(@NotNull Player player) {
    return getPlayer(player.getUniqueId());
  }

  /**
   * Get an ExtraSkillsPlayer based on its Bukkit Player entity UUID.
   *
   * @param playerUuid Bukkit Player entity UUID.
   * @return ExtraSkillsPlayer instance.
   */
  public static ExtraSkillsPlayer getPlayer(@NotNull UUID playerUuid) {
    return players.get(playerUuid);
  }

  /**
   * Remove a player from the players HashMap using its UUID as a key.
   *
   * @param player Bukkit Player instance.
   */
  public static void removePlayer(@NotNull Player player) {
    UUID playerUuid = player.getUniqueId();

    if (players.containsKey(playerUuid)) {
      players.get(playerUuid).save();
      players.remove(player.getUniqueId());
    }
  }

  /**
   * Remove all players from the players HashMap.
   */
  public static void clearPlayers() {
    players.clear();
  }
}
