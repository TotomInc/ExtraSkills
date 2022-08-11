package io.totominc.ExtraSkills.skills.mining;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.leveler.SkillLeveler;
import io.totominc.ExtraSkills.skills.Skill;
import io.totominc.ExtraSkills.utils.BlockUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public final class MiningLeveler extends SkillLeveler implements Listener {
  private final MiningAbilities miningAbilities = new MiningAbilities();

  public MiningLeveler(ExtraSkills instance) {
    super(instance);
  }

  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onBlockBreakEvent(@NotNull BlockBreakEvent event) {
    // Verify if player is eligible for experience gain.
    if (this.shouldPreventSkillAndAbility(event.getBlock().getLocation(), event.getPlayer())) {
      System.out.println("MiningLeveler.onBlockBreakEvent: prevented mining skill and ability");
      return;
    }

    // Check if block is not placed by a player, using chunk meta-data persistence.
    if (BlockUtils.isPlayerPlaced(event.getBlock())) {
      System.out.println("MiningLeveler.onBlockBreakEvent: block player placed");
      return;
    }

    // Apply lucky miner from here.
    this.miningAbilities.luckyMiner(event.getPlayer(), event.getBlock());

    // Add experience to player.
    this.gainExperience(Skill.MINING, event.getPlayer(), event.getBlock());
  }
}
