package io.totominc.ExtraSkills.config;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public final class PluginConfig {
  private static final ExtraSkills instance = ExtraSkills.getInstance();
  private final Map<SkillProgressionTypes, SkillProgressionConfig> skillProgressionConfig = new HashMap<>();

  public PluginConfig() {
    this.loadSkillProgression();
  }

  /**
   * Get the "config.yml" FileConfiguration of the plugin.
   *
   * @return FileConfiguration "config.yml".
   */
  public FileConfiguration getConfig() {
    return instance.getConfig();
  }

  /**
   * Return a map containing all possibles SkillProgressionConfig values as
   * defined in the config.
   *
   * @return Map containing all SkillProgressionConfig.
   */
  public Map<SkillProgressionTypes, SkillProgressionConfig> getSkillProgressionConfig() {
    return this.skillProgressionConfig;
  }

  /**
   * Load all possibles types of SkillProgressionConfig into the map.
   */
  private void loadSkillProgression() {
    FileConfiguration config = getConfig();
    ConfigurationSection skillProgression = config.getConfigurationSection("skill-progression");

    if (skillProgression != null) {
      for (SkillProgressionTypes type : SkillProgressionTypes.values()) {
        String typeName = type.name().toLowerCase().replace("_", "-");
        ConfigurationSection progression = skillProgression.getConfigurationSection(typeName);

        if (progression != null) {
          this.skillProgressionConfig.put(
            type,
            new SkillProgressionConfig(progression.getBoolean("enabled"), progression.getString("format"))
          );
        }
      }
    }
  }
}
