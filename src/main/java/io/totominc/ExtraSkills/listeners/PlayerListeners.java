package io.totominc.ExtraSkills.listeners;

import io.totominc.ExtraSkills.player.ExtraSkillsPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {
  @EventHandler(priority = EventPriority.NORMAL)
  public void onPlayerJoinEvent(PlayerJoinEvent event) {
    ExtraSkillsPlayerManager.addPlayer(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.NORMAL)
  public void onPlayerQuitEvent(PlayerQuitEvent event) {
    ExtraSkillsPlayerManager.removePlayer(event.getPlayer());
  }
}
