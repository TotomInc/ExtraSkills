package io.totominc.extraskills.utils;

import io.totominc.extraskills.ExtraSkills;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataType;

public final class BlockUtils {
  public static NamespacedKey generateKey(Block block) {
    return new NamespacedKey(ExtraSkills.getInstance(), String.valueOf(block.getLocation().hashCode()).toLowerCase());
  }

  public static Boolean isPlayerPlaced(Block block) {
    return block.getChunk().getPersistentDataContainer().has(generateKey(block), PersistentDataType.INTEGER);
  }
}
