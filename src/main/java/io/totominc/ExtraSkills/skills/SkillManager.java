package io.totominc.extraskills.skills;

import io.totominc.extraskills.ExtraSkills;
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

  /**
   * Get the associated SkillOption of a Skill. A Skill can only have 1 SkillOption.
   *
   * @param skill Skill.
   * @return SkillOption of a Skill.
   */
  public SkillOption getSkillOption(Skill skill) {
    return this.skillOptionMap.get(skill);
  }

  /**
   * Load `skills.yml` configuration file, which will parse eligible skills
   * rewards into a SkillOption object.
   *
   * @throws InvalidConfigurationException When the configuration is invalid.
   */
  private void loadOptionsFromConfig() throws InvalidConfigurationException {
    ExtraSkills plugin = ExtraSkills.getInstance();
    File file = new File(plugin.getDataFolder(), "skills.yml");

    // Create config if it doesn't exist.
    if (!file.exists()) {
      plugin.saveResource("skills.yml", false);
    }

    FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
    ConfigurationSection skillsSection = fileConfig.getConfigurationSection("skills");

    // Handle invalid configuration when `skills` property is missing from the configuration file.
    if (skillsSection == null) {
      throw new InvalidConfigurationException("Missing \"skills\" section in \"skills.yml\" configuration file.");
    }

    // Loop on all skills.
    for (Skill skill : Skill.values()) {
      String skillName = skill.name().toLowerCase();
      ConfigurationSection skillSection = skillsSection.getConfigurationSection(skillName);

      // Handle invalid configuration when the `Skill.name()` property is missing from the configuration file.
      if (skillSection == null) {
        throw new InvalidConfigurationException("Missing \"" + skillName + "\" section in \"skills.yml\" configuration file.");
      }

      String skillPath = "skills." + skillName;

      boolean isEnabled = fileConfig.getBoolean(skillPath + "." + SkillConfigProperty.ENABLED.path);
      double maxLevel = fileConfig.getDouble(skillPath + "." + SkillConfigProperty.MAX_LEVEL.path);
      String experienceExpression = fileConfig.getString(skillPath + "." + SkillConfigProperty.EXPERIENCE_EXPRESSION.path);
      ConfigurationSection sources = fileConfig.getConfigurationSection(skillPath + "." + SkillConfigProperty.SOURCES.path);

      // Handle invalid configuration when the `sources` property is missing from the configuration file.
      if (sources == null) {
        throw new InvalidConfigurationException("Missing \"sources\" section in \"skills.yml\" configuration file.");
      }

      // Load all sources values that should be in the format `key:experience_amount`.
      Map<String, Object> sourceValues = sources.getValues(false);

      // Create a new SkillOption for this skill that contains eligible rewards/entities/actions
      // with the amount of experience that should be awarded.
      this.skillOptionMap.put(
        Skill.valueOf(skill.name()),
        new SkillOption(
          isEnabled,
          maxLevel,
          experienceExpression,
          this.loadSkillBlocksSource(skill, sourceValues),
          this.loadSkillEntitiesSource(skill, sourceValues),
          this.loadSkillEnchanterOptionsSource(skill, sourceValues)
        )
      );
    }
  }

  /**
   * Load Materials that should reward experience from the configuration file.
   *
   * @param skill Skill.
   * @param blocks `key:value` map.
   * @return HashMap containing `Material:ExperienceAmount`
   */
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

  /**
   * Load Entities that should reward experience from the configuration file.
   *
   * @param skill Skill.
   * @param entities `key:value` map.
   * @return HashMap containing `Entity:ExperienceAmount`.
   */
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

  /**
   * Load Materials that should reward experience for an item-related action, from the configuration file.
   * For example, this is used for the Enchanter skill.
   *
   * @param skill Skill.
   * @param materials `key:value` map.
   * @return HashMap containing `Material:ExperienceAmount`.
   */
  private HashMap<Material, Double> loadSkillEnchanterOptionsSource(Skill skill, Map<String, Object> materials) {
    HashMap<Material, Double> materialsMap = new HashMap<>();

    if (skill.getSourceType().equals(SkillSourceType.ITEM) && materials.size() > 0) {
      for (String material : materials.keySet()) {
        materialsMap.put(
          Material.valueOf(material.toUpperCase()),
          Double.valueOf(materials.get(material).toString())
        );
      }
    }

    return materialsMap;
  }
}
