package io.totominc.extraskills.skills.mining;

import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.abilities.Ability;
import io.totominc.extraskills.configuration.Option;
import io.totominc.extraskills.skills.Skill;
import io.totominc.extraskills.skills.SkillAbility;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public final class MiningAbilities extends SkillAbility {
  public MiningAbilities(ExtraSkills instance) {
    super(instance, Skill.MINING);
  }

  /**
   * Drop twice as many resources.
   *
   * @param player Player.
   * @param block Block.
   */
  public void luckyMiner(Player player, Block block) {
    if (!this.canUseAbility(player, block, Ability.LUCKY_MINER)) {
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

  /**
   * Apply a potion effect.
   *
   * @param player Player.
   * @param block Block.
   */
  public void haster(Player player, Block block) {
    if (!this.canUseAbility(player, block, Ability.HASTER)) {
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

  /**
   * Drop more block experience.
   *
   * @param player Player.
   * @param block Block.
   * @param expToDrop Experience to drop.
   */
  public void farseeing(Player player, Block block, int expToDrop) {
    if (!this.canUseAbility(player, block, Ability.FARSEEING)) {
      return;
    }

    ExperienceOrb experienceOrb = (ExperienceOrb) block.getWorld().spawnEntity(block.getLocation(), EntityType.EXPERIENCE_ORB);

    experienceOrb.setExperience(expToDrop * this.instance.getOptionManager().getInt(Option.ABILITY_FARSEEING_MULTIPLIER));
  }
}
