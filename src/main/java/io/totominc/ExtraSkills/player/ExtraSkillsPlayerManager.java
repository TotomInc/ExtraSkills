package io.totominc.ExtraSkills.player;

import io.totominc.ExtraSkills.skills.Skill;
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
      players.put(playerUuid, new ExtraSkillsPlayer());
    }
  }

  /**
   * Remove a player from the players HashMap using its UUID as a key.
   *
   * @param player Bukkit Player instance.
   */
  public static void removePlayer(@NotNull Player player) {
    players.remove(player.getUniqueId());
  }

  /**
   * Remove all players from the players HashMap.
   */
  public static void clearPlayers() {
    players.clear();
  }

  /**
   * Add experience of a specific skill to a player.
   *
   * @param player Player instance.
   * @param skill Skill.
   * @param amount Amount of experience to gain.
   */
  public static void gainSkillExperience(@NotNull Player player, @NotNull Skill skill, @NotNull Double amount) {
    UUID playerUuid = player.getUniqueId();

    if (players.containsKey(playerUuid)) {
      players.get(playerUuid).gainSkillExperience(skill, amount);
    }
  }
}
