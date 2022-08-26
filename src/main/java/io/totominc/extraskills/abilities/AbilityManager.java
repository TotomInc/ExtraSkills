package io.totominc.extraskills.abilities;

import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.skills.Skill;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public final class AbilityManager {
  private final HashMap<Ability, AbilityOption> abilityOptionMap = new HashMap<>();

  public AbilityManager() {
    try {
      this.loadOptionsFromConfig();
    } catch (InvalidConfigurationException ignored) {}
  }

  public AbilityOption getAbilityOption(Ability ability) {
    return this.abilityOptionMap.get(ability);
  }

  public void loadOptionsFromConfig() throws InvalidConfigurationException {
    ExtraSkills plugin = ExtraSkills.getInstance();
    File file = new File(plugin.getDataFolder(), "abilities.yml");

    // Create config if it doesn't exist.
    if (!file.exists()) {
      plugin.saveResource("abilities.yml", false);
    }

    FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
    ConfigurationSection abilitiesSection = fileConfig.getConfigurationSection("abilities");

    if (abilitiesSection == null) {
      throw new InvalidConfigurationException("Missing \"abilities\" section in \"abilities.yml\" configuration file.");
    }

    // Loop on all skill properties.
    for (Skill skill : Skill.values()) {
      String skillName = skill.name().toLowerCase();
      ConfigurationSection skillSection = abilitiesSection.getConfigurationSection(skillName);

      if (skillSection == null) {
        throw new InvalidConfigurationException("Missing \"" + skillName + "\" section in \"abilities.yml\" configuration file.");
      }

      // Loop on all abilities from a skill.
      for (Ability ability : skill.getAbilities()) {
        String abilityName = ability.name().toLowerCase();
        String abilityPath = "abilities." + skillName + "." + abilityName;

        if (!fileConfig.contains(abilityPath)) {
          throw new InvalidConfigurationException("Missing \"" + abilityPath + "\" section in \"abilities.yml\" configuration file.");
        }

        // Parse ability option values from config section.
        boolean isEnabled = fileConfig.getBoolean(abilityPath + "." + AbilityConfigProperty.ENABLED.path);
        double baseValue = fileConfig.getDouble(abilityPath + "." + AbilityConfigProperty.BASE_VALUE.path);
        double valueGainedPerLevel = fileConfig.getDouble(abilityPath + "." + AbilityConfigProperty.VALUE_GAINED_PER_LEVEL.path);
        double unlockLevel = fileConfig.getInt(abilityPath + "." + AbilityConfigProperty.UNLOCK_LEVEL.path);
        double levelUpRate = fileConfig.getInt(abilityPath + "." + AbilityConfigProperty.LEVEL_UP_RATE.path);
        double maxLevel = fileConfig.getInt(abilityPath + "." + AbilityConfigProperty.MAX_LEVEL.path);
        List<String> blocks = fileConfig.getStringList(abilityPath + "." + AbilityConfigProperty.BLOCKS.path);
        List<String> entities = fileConfig.getStringList(abilityPath + "." + AbilityConfigProperty.ENTITIES.path);

        this.abilityOptionMap.put(
          Ability.valueOf(ability.name()),
          new AbilityOption(
            isEnabled, baseValue, valueGainedPerLevel, unlockLevel, levelUpRate, maxLevel,
            this.loadAbilityBlocks(ability, blocks),
            this.loadAbilityEntities(ability, entities)
          )
        );
      }
    }
  }

  private HashSet<Material> loadAbilityBlocks(Ability ability, List<String> blocks) {
    HashSet<Material> blocksMap = new HashSet<>();

    if (ability.getHasBlockConfig() && blocks.size() > 0) {
      for (String block : blocks) {
        blocksMap.add(Material.valueOf(block.toUpperCase()));
      }
    }

    return blocksMap;
  }

  private HashSet<EntityType> loadAbilityEntities(Ability ability, List<String> entities) {
    HashSet<EntityType> entitiesMap = new HashSet<>();

    if (ability.getHasEntityConfig() && entities.size() > 0) {
      for (String block : entities) {
        entitiesMap.add(EntityType.valueOf(block.toUpperCase()));
      }
    }

    return entitiesMap;
  }
}
