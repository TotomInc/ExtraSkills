package io.totominc.extraskills.skills.mining;

import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.abilities.Ability;
import io.totominc.extraskills.abilities.AbilityOption;
import io.totominc.extraskills.configuration.Option;
import io.totominc.extraskills.data.PlayerData;
import io.totominc.extraskills.skills.Skill;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Random;

public final class MiningAbilities {
  private final ExtraSkills instance;
  private final Random random = new Random();

  public MiningAbilities(ExtraSkills instance) {
    this.instance = instance;
  }

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

  public void haster(Player player, Block block) {
    PlayerData playerData = ExtraSkills.getInstance().getPlayerDataManager().getPlayerData(player.getUniqueId());

    if (playerData == null) {
      return;
    }

    AbilityOption abilityOption = playerData.getAbilityOption(Ability.HASTER);

    if (
      abilityOption == null ||
      !abilityOption.isEnabled() ||
      !abilityOption.blocks().contains(block.getType()) ||
      playerData.getSkillLevel(Skill.MINING) < abilityOption.unlockLevel()
    ) {
      return;
    }

    if (this.random.nextDouble() > playerData.getAbilityValue(Ability.HASTER) / 100) {
      return;
    }

    // Try to parse the PotionEffectType from the configuration in order to prevent
    // invalid configuration.
    PotionEffectType potionEffectType = PotionEffectType.getByName(
      this.instance.getOptionManager().getString(Option.ABILITY_HASTER_POTION_EFFECT)
    );

    if (potionEffectType == null) {
      Bukkit.getLogger().warning("Invalid PotionEffectType for \"" + Option.ABILITY_HASTER_POTION_EFFECT.getPath() + "\" property inside \"config.yml\". Unable to apply random potion effect.");
      return;
    }

    new PotionEffect(
      potionEffectType,
      this.instance.getOptionManager().getInt(Option.ABILITY_HASTER_DURATION),
      this.instance.getOptionManager().getInt(Option.ABILITY_HASTER_AMPLIFIER)
    ).apply(player);
  }
}
