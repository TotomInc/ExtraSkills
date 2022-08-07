package io.totominc.ExtraSkills.listeners;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.configuration.Option;
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
  private final boolean checkPlayerBlockReplaced;

  public BlockListeners(ExtraSkills instance) {
    this.checkPlayerBlockReplaced = instance.getOptionManager().getBoolean(Option.CHECK_PLAYER_BLOCK_PLACED);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockPlaceEvent(@NotNull BlockPlaceEvent event) {
    if (this.checkPlayerBlockReplaced) {
      ChunkUtils.writeKey(event.getBlock().getChunk(), BlockUtils.generateKey(event.getBlock()));
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockMultiPlaceEvent(@NotNull BlockMultiPlaceEvent event) {
    if (this.checkPlayerBlockReplaced) {
      ChunkUtils.writeKey(event.getBlock().getChunk(), BlockUtils.generateKey(event.getBlock()));
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockBreakEvent(@NotNull BlockBreakEvent event) {
    Bukkit.getScheduler().runTask(
      ExtraSkills.getInstance(),
      () -> ChunkUtils.removeKey(event.getBlock().getChunk(), BlockUtils.generateKey(event.getBlock()))
    );
  }
}
