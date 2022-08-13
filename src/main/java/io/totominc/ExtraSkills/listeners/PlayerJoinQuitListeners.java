package io.totominc.ExtraSkills.listeners;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerJoinQuitListeners implements Listener {
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoinEvent(PlayerJoinEvent event) {
    // Try to load player-data from the defined storage provider. If no saved
    // player-data exists, create a fresh PlayerData instance.
    ExtraSkills.getInstance().getPlayerStorageManager().loadPlayerData(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuitEvent(PlayerQuitEvent event) {
    // Save player-data to the defined storage provider.
    ExtraSkills.getInstance().getPlayerStorageManager().savePlayerData(event.getPlayer(), true);
    ExtraSkills.getInstance().getBossBarManager().removePlayer(event.getPlayer());
  }
}
