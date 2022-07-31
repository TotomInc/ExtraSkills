package io.totominc.ExtraSkills.listeners;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.utils.BlockUtils;
import io.totominc.ExtraSkills.utils.ChunkUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class BlockListeners implements Listener {
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockPlaceEvent(@NotNull BlockPlaceEvent event) {
    ChunkUtils.writeKey(event.getBlock().getChunk(), BlockUtils.generateKey(event.getBlock()));
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockMultiPlaceEvent(@NotNull BlockMultiPlaceEvent event) {
    ChunkUtils.writeKey(event.getBlock().getChunk(), BlockUtils.generateKey(event.getBlock()));
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockBreakEvent(@NotNull BlockBreakEvent event) {
    Bukkit.getScheduler().runTask(
      ExtraSkills.getInstance(),
      () -> ChunkUtils.removeKey(event.getBlock().getChunk(), BlockUtils.generateKey(event.getBlock()))
    );
  }
}
