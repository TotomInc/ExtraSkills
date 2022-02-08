package io.totominc.ExtraSkills.config;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public final class PluginConfig {
  private final Map<SkillProgressionTypes, SkillProgressionConfig> skillProgressionConfig = new HashMap<>();

  public PluginConfig() throws InvalidConfigurationException {
    this.loadSkillProgression();
  }

  /**
   * Return a map containing all possibles SkillProgressionConfig values as
   * defined in the config.
   *
   * @return Map containing all SkillProgressionConfig.
   */
  public Map<SkillProgressionTypes, SkillProgressionConfig> getSkillProgressionConfig() {
    return skillProgressionConfig;
  }

  /**
   * Load all possibles types of SkillProgressionConfig into the map.
   */
  private void loadSkillProgression() throws InvalidConfigurationException {
    FileConfiguration config = ExtraSkills.getInstance().getConfig();
    ConfigurationSection progressionSection = config.getConfigurationSection("skill-progression");

    if (progressionSection == null) {
      throw new InvalidConfigurationException("Invalid \"skill-progression\" configuration");
    }

    for (SkillProgressionTypes type : SkillProgressionTypes.values()) {
      String typeName = type.name().toLowerCase().replace("_", "-");
      ConfigurationSection progression = progressionSection.getConfigurationSection(typeName);

      if (progression == null) {
        throw new InvalidConfigurationException("Invalid \"skill-progression\" configuration");
      }

      skillProgressionConfig.put(
        type,
        new SkillProgressionConfig(progression.getBoolean("enabled"), progression.getString("format"))
      );
    }
  }
}
