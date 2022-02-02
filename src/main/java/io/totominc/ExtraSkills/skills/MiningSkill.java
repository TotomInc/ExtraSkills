package io.totominc.ExtraSkills.skills;

import io.totominc.ExtraSkills.player.ExtraSkillsPlayerManager;
import io.totominc.ExtraSkills.utils.BlockUtils;
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

    this.loadRewards();
  }

  public void afterUpdate() {
    // Nothing to do...
  }

  /**
   * Load rewards from the configuration file and add them into the rewards
   * EnumMap.
   */
  private void loadRewards() {
    EnumMap<Material, Double> rewards = getRewards();

    for (String item : this.getSkillConfig().getConfig().getStringList("xp-rewards-per-blocks")) {
      String[] splitted = item.split("\\s+");
      Material material = Material.getMaterial(splitted[0].toUpperCase());
      double xpReward = Double.parseDouble(splitted[1]);

      if (material != null && xpReward > 0) {
        rewards.put(material, xpReward);
      }
    }
  }

  /**
   * On player block break, give mining experience to the player.
   *
   * @param event BlockBreakEvent.
   */
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  private void handleBlockBreakEvent(@NotNull BlockBreakEvent event) {
    if (this.getSkillConfig().getConfig().getStringList("skill-disabled-in-worlds").contains(event.getBlock().getWorld().getName())) {
      return;
    }

    Player player = event.getPlayer();

    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
      return;
    }

    if (BlockUtils.isPlayerPlaced(event.getBlock())) {
      return;
    }

    double experienceReward = this.getReward(event.getBlock().getType());

    ExtraSkillsPlayerManager.gainSkillExperience(player, this, experienceReward);
  }
}
