package io.totominc.extraskills.skills.combat;

import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.leveler.SkillLeveler;
import io.totominc.extraskills.skills.Skill;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

public final class CombatLeveler extends SkillLeveler implements Listener {
  public CombatLeveler(ExtraSkills instance) {
    super(instance);
  }

  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onEntityDeathEvent(@NotNull EntityDeathEvent event) {
    // Don't allow entities killed by any other source than a player.
    if (event.getEntity().getKiller() == null) {
      System.out.println("CombatLeveler.onEntityDeathEvent: entity died from other source than player");
      return;
    }

    // Verify if player is eligible for experience gain.
    if (this.shouldPreventSkillAndAbility(event.getEntity().getLocation(), event.getEntity().getKiller())) {
      System.out.println("CombatLeveler.onEntityDeathEvent: prevented combat skill and ability");
      return;
    }

    // Add experience to player.
    this.gainExperience(Skill.COMBAT, event.getEntity().getKiller(), event.getEntity());
  }
}
