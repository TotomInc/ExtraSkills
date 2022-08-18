package io.totominc.extraskills.utils;

import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

public final class ChunkUtils {
  public static void writeKey(Chunk chunk, NamespacedKey key) {
    chunk.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
  }

  public static void removeKey(Chunk chunk, NamespacedKey key) {
    chunk.getPersistentDataContainer().remove(key);
  }
}
