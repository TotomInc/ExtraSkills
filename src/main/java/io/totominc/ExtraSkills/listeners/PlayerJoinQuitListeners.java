package io.totominc.ExtraSkills.listeners;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.data.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerJoinQuitListeners implements Listener {
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoinEvent(PlayerJoinEvent event) {
    ExtraSkills.getInstance().getPlayerDataManager().addPlayerData(
      event.getPlayer().getUniqueId(),
      new PlayerData(event.getPlayer())
    );
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuitEvent(PlayerQuitEvent event) {
    ExtraSkills.getInstance().getPlayerDataManager().removePlayerData(event.getPlayer().getUniqueId());
  }
}
