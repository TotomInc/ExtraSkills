package io.totominc.ExtraSkills.abilities;

/**
 * Contains a custom configuration for an Ability. Ideally this custom configuration
 * is loaded from a configuration file (abilities.yml).
 */
public record AbilityOption(boolean isEnabled, double baseValue, double valueGainedPerLevel, double unlockLevel, double levelUpRate, double maxLevel) {}
