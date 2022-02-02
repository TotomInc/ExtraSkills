package io.totominc.ExtraSkills.utils;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class BlockUtils {
  /**
   * Generate a key to write inside the chunk persistent data container.
   *
   * @param location Location where to write the key.
   * @return NamespacedKey based on location.
   */
  public static NamespacedKey generateKey(@NotNull Location location) {
    return new NamespacedKey(ExtraSkills.getInstance(), String.valueOf(location.hashCode()).toLowerCase());
  }

  /**
   * Return true if the block has been placed by a player.
   *
   * @param block Block.
   * @return True if placed by a player.
   */
  public static boolean isPlayerPlaced(@NotNull Block block) {
    return block.getChunk().getPersistentDataContainer().has(generateKey(block.getLocation()), PersistentDataType.INTEGER);
  }
}
