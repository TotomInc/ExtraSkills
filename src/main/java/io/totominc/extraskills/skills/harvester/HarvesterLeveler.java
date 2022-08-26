package io.totominc.extraskills.skills.harvester;

import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.leveler.SkillLeveler;
import io.totominc.extraskills.skills.Skill;
import io.totominc.extraskills.utils.BlockUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class HarvesterLeveler extends SkillLeveler implements Listener {
  public HarvesterLeveler(ExtraSkills instance) {
    super(instance);
  }

  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onBlockBreakEvent(BlockBreakEvent event) {
    // Verify if player is eligible for experience gain.
    if (this.shouldPreventSkillAndAbility(event.getBlock().getLocation(), event.getPlayer())) {
      System.out.println("HarvesterLeveler.onBlockBreakEvent: prevented mining skill and ability");
      return;
    }

    // Check if block is not placed by a player, using chunk meta-data persistence.
    if (BlockUtils.isPlayerPlaced(event.getBlock())) {
      System.out.println("HarvesterLeveler.onBlockBreakEvent: block player placed");
      return;
    }

    // Add experience to player.
    this.gainExperience(Skill.HARVESTER, event.getPlayer(), event.getBlock());
  }
}
