package io.totominc.ExtraSkills.skills.mining;

import io.totominc.ExtraSkills.utils.BlockUtils;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public final class MiningLeveler implements Listener {
  private final MiningSource miningSource = new MiningSource();

  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onBlockBreakEvent(@NotNull BlockBreakEvent event) {
    if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
      System.out.println("MiningLeveler.onBlockBreakEvent: player in creative");
      return;
    }

    if (BlockUtils.isPlayerPlaced(event.getBlock())) {
      System.out.println("MiningLeveler.onBlockBreakEvent: block player placed");
      return;
    }

    if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
      System.out.println("MiningLeveler.onBlockBreakEvent: player has silk-touch");
      return;
    }

    Double experience = miningSource.getRewards().get(event.getBlock().getType());

    if (experience == null) {
      System.out.println("MiningLeveler.onBlockBreakEvent: block material not in MiningSource");
      return;
    }

    System.out.println("MiningLeveler.onBlockBreakEvent: block material reward " + experience);
  }
}
