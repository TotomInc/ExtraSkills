package io.totominc.ExtraSkills.config;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public final class PluginConfig {
  private static final ExtraSkills instance = ExtraSkills.getInstance();
  private static final Map<SkillProgressionTypes, SkillProgressionConfig> skillProgressionConfig = new HashMap<>();

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
  public static Map<SkillProgressionTypes, SkillProgressionConfig> getSkillProgressionConfig() {
    return skillProgressionConfig;
  }

  /**
   * Load all possibles types of SkillProgressionConfig into the map.
   */
  private void loadSkillProgression() {
    FileConfiguration config = getConfig();
    ConfigurationSection skillProgression = config.getConfigurationSection("skill-progression");

    if (skillProgression != null) {
      for (SkillProgressionTypes type : SkillProgressionTypes.values()) {
        ConfigurationSection progression = skillProgression.getConfigurationSection(type.name());

        if (progression != null) {
          skillProgressionConfig.put(
            type,
            new SkillProgressionConfig(progression.getBoolean("is-enabled"), progression.getString("format"))
          );
        }
      }
    }
  }
}
