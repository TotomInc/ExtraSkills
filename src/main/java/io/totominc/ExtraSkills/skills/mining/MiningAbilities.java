package io.totominc.extraskills.skills.mining;

import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.abilities.Ability;
import io.totominc.extraskills.abilities.AbilityOption;
import io.totominc.extraskills.data.PlayerData;
import io.totominc.extraskills.skills.Skill;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Random;

public final class MiningAbilities {
  private final Random random = new Random();

  public void luckyMiner(Player player, Block block) {
    PlayerData playerData = ExtraSkills.getInstance().getPlayerDataManager().getPlayerData(player.getUniqueId());

    if (playerData == null) {
      return;
    }

    AbilityOption abilityOption = playerData.getAbilityOption(Ability.LUCKY_MINER);

    if (
      abilityOption == null ||
      !abilityOption.isEnabled() ||
      !abilityOption.blocks().contains(block.getType()) ||
      playerData.getSkillLevel(Skill.MINING) < abilityOption.unlockLevel()
    ) {
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
