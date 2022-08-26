package io.totominc.extraskills.skills.enchanter;

import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.abilities.Ability;
import io.totominc.extraskills.skills.Skill;
import io.totominc.extraskills.skills.SkillAbility;
import org.bukkit.event.enchantment.EnchantItemEvent;

public final class EnchanterAbilities extends SkillAbility {
  public EnchanterAbilities(ExtraSkills instance) {
    super(instance, Skill.ENCHANTER);
  }

  /**
   * If triggered, remove experience cost from enchantment event.
   *
   * @param event EnchantItemEvent.
   */
  public void soothsayer(EnchantItemEvent event) {
    if (!this.canUseAbility(event.getEnchanter(), Ability.SOOTHSAYER)) {
      return;
    }

    event.setExpLevelCost(0);
  }
}
