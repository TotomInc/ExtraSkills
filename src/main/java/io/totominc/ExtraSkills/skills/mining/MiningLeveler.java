package io.totominc.ExtraSkills.skills.mining;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.skills.Skill;
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
  private final MiningAbilities miningAbilities = new MiningAbilities();

  // TODO: Implement blocked worlds.
  // TODO: Implement blocked regions with WorldGuard.
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onBlockBreakEvent(@NotNull BlockBreakEvent event) {
    // Allows only in Survival game-mode.
    if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
      System.out.println("MiningLeveler.onBlockBreakEvent: player in creative");
      return;
    }

    // Check if block is not placed by a player, using chunk meta-data persistence.
    if (BlockUtils.isPlayerPlaced(event.getBlock())) {
      System.out.println("MiningLeveler.onBlockBreakEvent: block player placed");
      return;
    }

    // Don't allow silk-touch mining.
    if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
      System.out.println("MiningLeveler.onBlockBreakEvent: player has silk-touch");
      return;
    }

    // Apply lucky miner from here.
    this.miningAbilities.luckyMiner(event.getPlayer(), event.getBlock());

    Double experience = this.miningSource.getReward(event.getBlock().getType());

    // Verify if block material is registered on the sources before adding experience.
    if (experience == null) {
      System.out.println("MiningLeveler.onBlockBreakEvent: block material not in MiningSource");
      return;
    }

    System.out.println("MiningLeveler.onBlockBreakEvent: block material reward " + experience);

    // Add skill experience to the player.
    ExtraSkills.getInstance().getLeveler().addExperience(event.getPlayer(), Skill.MINING, experience);
  }
}
