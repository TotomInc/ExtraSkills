package io.totominc.extraskills.skills.enchanter;

import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.leveler.SkillLeveler;
import io.totominc.extraskills.skills.Skill;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.jetbrains.annotations.NotNull;

public final class EnchanterLeveler extends SkillLeveler implements Listener {
  private final EnchanterAbilities enchanterAbilities;

  public EnchanterLeveler(ExtraSkills instance) {
    super(instance);

    this.enchanterAbilities = new EnchanterAbilities(instance);
  }

  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onEnchantItemEvent(@NotNull EnchantItemEvent event) {
    // Verify if player is eligible for experience gain and abilities.
    if (this.shouldPreventSkillAndAbility(event.getEnchanter().getLocation(), event.getEnchanter())) {
      System.out.println("EnchanterLeveler.onEnchantItemEvent: prevented enchant skill and ability");
      return;
    }

    // Apply abilities from here.
    this.enchanterAbilities.soothsayer(event);

    // Add experience to player.
    this.gainExperience(
      Skill.ENCHANTER,
      event.getEnchanter(),
      event.getItem(),
      event.getExpLevelCost()
    );
  }
}
