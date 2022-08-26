package io.totominc.extraskills.skills;

import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.abilities.Ability;
import io.totominc.extraskills.abilities.AbilityOption;
import io.totominc.extraskills.data.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Random;

public abstract class SkillAbility {
  public final ExtraSkills instance;
  private final Skill skill;
  private final Random random = new Random();

  public SkillAbility(ExtraSkills instance, Skill skill) {
    this.instance = instance;
    this.skill = skill;
  }

  /**
   * Use a random value against the player ability value and try to trigger the
   * ability.
   *
   * @param player Player.
   * @param ability Ability.
   * @return True, has triggered the ability randomness.
   */
  public boolean hasTriggeredAbility(Player player, Ability ability) {
    PlayerData playerData = this.instance.getPlayerDataManager().getPlayerData(player.getUniqueId());

    if (playerData == null) {
      return false;
    }

    return !(this.random.nextDouble() > playerData.getAbilityValue(ability) / 100);
  }

  /**
   * Verify if a player has unlocked the ability and has triggered the random
   * value for this ability.
   * <p>
   * Should be used for block-related actions.
   *
   * @param player Player.
   * @param block Block.
   * @param ability Ability.
   * @return True, can use the ability.
   */
  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean canUseAbility(Player player, Block block, Ability ability) {
    PlayerData playerData = this.instance.getPlayerDataManager().getPlayerData(player.getUniqueId());

    if (playerData == null) {
      return false;
    }

    AbilityOption abilityOption = playerData.getAbilityOption(ability);

    if (
      abilityOption == null ||
      !abilityOption.isEnabled() ||
      !abilityOption.blocks().contains(block.getType()) ||
      playerData.getSkillLevel(this.skill) < abilityOption.unlockLevel()
    ) {
      return false;
    }

    return this.hasTriggeredAbility(player, ability);
  }

  /**
   * Verify if the player has unlocked the ability and has been triggered.
   * <p>
   * Should be used for item-related actions.
   *
   * @param player Player.
   * @param ability Ability.
   * @return True, can use the ability.
   */
  public boolean canUseAbility(Player player, Ability ability) {
    PlayerData playerData = this.instance.getPlayerDataManager().getPlayerData(player.getUniqueId());

    if (playerData == null) {
      return false;
    }
    AbilityOption abilityOption = playerData.getAbilityOption(ability);

    if (
      abilityOption == null ||
      !abilityOption.isEnabled() ||
      playerData.getSkillLevel(this.skill) < abilityOption.unlockLevel()
    ) {
      return false;
    }

    return this.hasTriggeredAbility(player, ability);
  }
}
