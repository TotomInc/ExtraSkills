package io.totominc.ExtraSkills.skills.enchanter;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.leveler.SkillLeveler;
import io.totominc.ExtraSkills.skills.Skill;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public final class EnchanterLeveler extends SkillLeveler implements Listener {
  public EnchanterLeveler(ExtraSkills instance) {
    super(instance);
  }

  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onEnchantItemEvent(EnchantItemEvent event) {
    // Verify if player is eligible for experience gain.
    if (this.shouldPreventSkillAndAbility(event.getEnchanter().getLocation(), event.getEnchanter())) {
      System.out.println("EnchanterLeveler.onEnchantItemEvent: prevented enchant skill and ability");
      return;
    }

    // Add experience to player.
    this.gainExperience(
      Skill.ENCHANTER,
      event.getEnchanter(),
      event.getItem(),
      event.getExpLevelCost()
    );
  }
}
