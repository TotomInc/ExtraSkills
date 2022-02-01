package io.totominc.ExtraSkills.skills;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class MiningSkill extends Skill {
  public MiningSkill() {
    super("MINING");

    EnumMap<Material, Double> rewards = getRewards();
    rewards.put(Material.STONE, 0.5);
  }

  public void afterUpdate() {
    // Nothing to do...
  }

  /**
   * On player block break, give mining experience to the player.
   *
   * @param event BlockBreakEvent.
   */
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  private void handleBlockBreakEvent(@NotNull BlockBreakEvent event) {
    Player player = event.getPlayer();

    ExtraSkills.getInstance().getLogger().info(player.getDisplayName());

    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
      return;
    }

    double blockReward = this.getReward(event.getBlock().getType());

    ExtraSkills.getInstance().getLogger().info(String.valueOf(blockReward));
  }
}
