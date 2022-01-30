package io.totominc.ExtraSkills.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListeners implements Listener {
  @EventHandler(priority = EventPriority.NORMAL)
  public void onPlayerJoinEvent(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    player.sendMessage("Hello, " + player.getDisplayName() + "!");
  }
}
