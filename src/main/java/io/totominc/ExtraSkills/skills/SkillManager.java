package io.totominc.ExtraSkills.skills;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class SkillManager {
  private final HashMap<Skill, SkillOption> skillOptionMap = new HashMap<>();

  public SkillManager() {
    try {
      this.loadOptionsFromConfig();
    } catch (InvalidConfigurationException ignored) {}
  }

  public SkillOption getSkillOption(Skill skill) {
    return this.skillOptionMap.get(skill);
  }

  private void loadOptionsFromConfig() throws InvalidConfigurationException {
    ExtraSkills plugin = ExtraSkills.getInstance();
    File file = new File(plugin.getDataFolder(), "skills.yml");

    // Create config if it doesn't exist.
    if (!file.exists()) {
      plugin.saveResource("skills.yml", false);
    }

    FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
    ConfigurationSection skillsSection = fileConfig.getConfigurationSection("skills");

    if (skillsSection == null) {
      throw new InvalidConfigurationException("Missing \"skills\" section in \"skills.yml\" configuration file.");
    }

    // Loop on all skills.
    for (Skill skill : Skill.values()) {
      String skillName = skill.name().toLowerCase();
      ConfigurationSection skillSection = skillsSection.getConfigurationSection(skillName);

      if (skillSection == null) {
        throw new InvalidConfigurationException("Missing \"" + skillName + "\" section in \"skills.yml\" configuration file.");
      }

      String skillPath = "skills." + skillName;

      boolean isEnabled = fileConfig.getBoolean(skillPath + "." + SkillConfigProperty.ENABLED.path);
      double maxLevel = fileConfig.getDouble(skillPath + "." + SkillConfigProperty.MAX_LEVEL.path);
      String experienceExpression = fileConfig.getString(skillPath + "." + SkillConfigProperty.EXPERIENCE_EXPRESSION.path);
      ConfigurationSection sources = fileConfig.getConfigurationSection(skillPath + "." + SkillConfigProperty.SOURCES.path);

      if (sources == null) {
        throw new InvalidConfigurationException("Missing \"sources\" section in \"skills.yml\" configuration file.");
      }

      if (skill.getSourceType().equals(SkillSourceType.BLOCK)) {
        this.skillOptionMap.put(
          Skill.valueOf(skill.name()),
          new SkillOption(
            isEnabled, maxLevel, experienceExpression,
            this.loadSkillBlocksSource(skill, sources.getValues(false)),
            this.loadSkillEntitiesSource(skill, sources.getValues(false))
          )
        );
      }
    }
  }

  private HashMap<Material, Double> loadSkillBlocksSource(Skill skill, Map<String, Object> blocks) {
    HashMap<Material, Double> blocksMap = new HashMap<>();

    if (skill.getSourceType().equals(SkillSourceType.BLOCK) && blocks.size() > 0) {
      for (String block : blocks.keySet()) {
        blocksMap.put(
          Material.valueOf(block.toUpperCase()),
          Double.valueOf(blocks.get(block).toString())
        );
      }
    }

    return blocksMap;
  }

  private HashMap<EntityType, Double> loadSkillEntitiesSource(Skill skill, Map<String, Object> entities) {
    HashMap<EntityType, Double> entitiesMap = new HashMap<>();

    if (skill.getSourceType().equals(SkillSourceType.ENTITY) && entities.size() > 0) {
      for (String entity : entities.keySet()) {
        entitiesMap.put(
          EntityType.valueOf(entity.toUpperCase()),
          Double.valueOf(entities.get(entity).toString())
        );
      }
    }

    return entitiesMap;
  }
}
