package io.totominc.extraskills.abilities;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.HashSet;

/**
 * Contains a custom configuration for an Ability. Ideally this custom configuration
 * is loaded from a configuration file (abilities.yml).
 */
public record AbilityOption(boolean isEnabled, double baseValue, double valueGainedPerLevel, double unlockLevel, double levelUpRate, double maxLevel, HashSet<Material> blocks, HashSet<EntityType> entities) {}
