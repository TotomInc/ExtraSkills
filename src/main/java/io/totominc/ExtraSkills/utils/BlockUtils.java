package io.totominc.ExtraSkills.utils;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

public final class BlockUtils {
  public static NamespacedKey generateKey(Block block) {
    return new NamespacedKey(ExtraSkills.getInstance(), String.valueOf(block.getLocation().hashCode()).toLowerCase());
  }
}
