package io.totominc.ExtraSkills.skills.mining;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.abilities.Ability;
import io.totominc.ExtraSkills.data.PlayerData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public final class MiningAbilities {
  private final Random random = new Random();
  // TODO: Load lucky-miner materials from a config.
  private final HashSet<Material> materials = new HashSet<>();

  public MiningAbilities() {
    this.materials.add(Material.DIAMOND_ORE);
    this.materials.add(Material.REDSTONE_ORE);
    this.materials.add(Material.LAPIS_ORE);
    this.materials.add(Material.EMERALD_ORE);
    this.materials.add(Material.GOLD_ORE);
    this.materials.add(Material.IRON_ORE);
    this.materials.add(Material.COAL_ORE);
  }

  public void luckyMiner(Player player, Block block) {
    if (!this.materials.contains(block.getType())) {
      return;
    }

    PlayerData playerData = ExtraSkills.getInstance().getPlayerDataManager().getPlayerData(player.getUniqueId());

    if (playerData == null) {
      return;
    }

    if (this.random.nextDouble() > playerData.getAbilityValue(Ability.LUCKY_MINER) / 100) {
      return;
    }

    ItemStack tool = player.getInventory().getItemInMainHand();
    Collection<ItemStack> drops = block.getDrops(tool);

    if (tool.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0) {
      return;
    }

    for (ItemStack drop : drops) {
      block.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), drop.clone());
    }
  }
}
