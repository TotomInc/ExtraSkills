package io.totominc.extraskills.skills;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

/**
 * Contains a custom configuration for a Skill. Ideally this custom configuration
 * is loaded from a configuration file like `skills.yml`.
 */
public record SkillOption(
  boolean isEnabled,
  double maxLevel,
  String experienceExpression,
  HashMap<Material, Double> blocks,
  HashMap<EntityType, Double> entities,
  HashMap<Material, Double> items
) {}
