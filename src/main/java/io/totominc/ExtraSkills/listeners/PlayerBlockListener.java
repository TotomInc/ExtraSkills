package io.totominc.ExtraSkills.listeners;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.utils.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class PlayerBlockListener implements Listener {
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockPlaceEvent(@NotNull BlockPlaceEvent event) {
    writeKey(event.getBlock());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockMultiPlaceEvent(@NotNull BlockMultiPlaceEvent event) {
    writeKey(event.getBlock());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockBreakEvent(@NotNull BlockBreakEvent event) {
    Bukkit.getScheduler().runTask(ExtraSkills.getInstance(), () -> this.removeKey(event.getBlock()));
  }

  /**
   * Call `writeKey` based on the given block location.
   *
   * @param block Block that have been manually placed.
   */
  private void writeKey(@NotNull Block block) {
    this.writeKey(block.getLocation());
  }

  /**
   * Write a key pointing to the block location inside the chunk persistent
   * data container. This is used to keep track of manually placed player
   * blocks.
   *
   * @param location Location where to write the key.
   */
  private void writeKey(@NotNull Location location) {
    location.getChunk().getPersistentDataContainer().set(BlockUtils.generateKey(location), PersistentDataType.INTEGER, 1);
  }

  /**
   * Call `removeKey` based on the given block location.
   *
   * @param block Block that have been destroyed.
   */
  private void removeKey(@NotNull Block block) {
    this.removeKey(block.getLocation());
  }

  /**
   * Remove the key of a specific block location if it has been registered in
   * the chunk persistent data container.
   *
   * @param location Location where to remove the key.
   */
  private void removeKey(@NotNull Location location) {
    location.getChunk().getPersistentDataContainer().remove(BlockUtils.generateKey(location));
  }
}
