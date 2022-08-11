package io.totominc.ExtraSkills.leveler;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.configuration.Option;
import io.totominc.ExtraSkills.skills.Skill;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class SkillLeveler {
  public final ExtraSkills instance;
  public final List<String> disabledWorlds = new ArrayList<>();
  public final List<GameMode> disabledGamemodes = new ArrayList<>();

  public SkillLeveler(ExtraSkills instance) {
    this.instance = instance;
    this.disabledWorlds.addAll(instance.getOptionManager().getList(Option.DISABLED_WORLDS));

    for (String gamemode : instance.getOptionManager().getList(Option.DISABLED_GAMEMODES)) {
      this.disabledGamemodes.add(GameMode.valueOf(gamemode));
    }
  }

  // TODO: Implement blocked regions with WorldGuard.
  public boolean shouldPreventSkillAndAbility(Location location, Player player) {
    if (location.getWorld() == null) {
      System.out.println("SkillLeveler.shouldBlockExperienceGain: no world found");
      return true;
    }

    // Verify if the location is from a disabled world from the configuration file.
    if (this.disabledWorlds.contains(location.getWorld().getName())) {
      System.out.println("SkillLeveler.shouldBlockExperienceGain: disabled world");
      return true;
    }

    // Allow only in game-modes defined in the configuration file.
    if (this.disabledGamemodes.contains(player.getGameMode())) {
      System.out.println("SkillLeveler.shouldBlockExperienceGain: player in disabled gamemode \"" + player.getGameMode() + "\"");
      return true;
    }

    // Don't allow silk-touch interaction, too risky.
    if (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
      System.out.println("SkillLeveler.shouldBlockExperienceGain: player has silk-touch");
      return true;
    }

    return false;
  }

  @Nullable
  public Double getExperience(Skill skill, Block block) {
    return this.instance.getSkillManager().getSkillOption(skill).blocks().get(block.getType());
  }

  @Nullable
  public Double getExperience(Skill skill, Entity entity) {
    return this.instance.getSkillManager().getSkillOption(skill).entities().get(entity.getType());
  }

  public void gainExperience(Skill skill, Player player, Block block) {
    Double experience = this.getExperience(skill, block);

    if (experience == null) {
      System.out.println("SkillLeveler.gainExperience: no experience reward found for " + skill.name() + " skill");
      return;
    }

    System.out.println("SkillLeveler.gainExperience: experience reward for " + skill.name() + " skill");

    this.instance.getLeveler().addExperience(player, skill, experience);
  }

  public void gainExperience(Skill skill, Player player, Entity entity) {
    Double experience = this.getExperience(skill, entity);

    if (experience == null) {
      System.out.println("SkillLeveler.gainExperience: no experience reward found for " + skill.name() + " skill");
      return;
    }

    System.out.println("SkillLeveler.gainExperience: experience reward for " + skill.name() + " skill");

    this.instance.getLeveler().addExperience(player, skill, experience);
  }
}
