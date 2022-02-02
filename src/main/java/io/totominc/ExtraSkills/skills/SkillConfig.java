package io.totominc.ExtraSkills.skills;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SkillConfig {
  private final ExtraSkills instance = ExtraSkills.getInstance();
  private final String name;
  private final YamlConfiguration config;

  public SkillConfig(String name) {
    this.name = name.toLowerCase();
    this.config = new YamlConfiguration();

    this.loadConfig();
  }

  /**
   * Based on the current skill name, try to load the config file associated to
   * this skill. If it doesn't exist, make sure to create a fresh copy of this
   * skill config from resources folder.
   */
  private void loadConfig() {
    File configFile = new File(this.getSkillsFolder(), this.getConfigFileName());

    if (!configFile.exists()) {
      boolean isCreated = configFile.getParentFile().mkdirs();

      if (isCreated) {
        this.instance.saveResource("skills/" + this.getConfigFileName(), false);
      }
    }

    try {
      this.config.load(configFile);
    } catch (IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the skill YamlConfiguration.
   *
   * @return YamlConfiguration for this skill.
   */
  public YamlConfiguration getConfig() {
    return this.config;
  }

  /**
   * Get the folder where skills configurations are stored.
   *
   * @return Relative path to the skills configurations folder.
   */
  private String getSkillsFolder() {
    return this.instance.getDataFolder() + File.separator + "skills";
  }

  /**
   * Get the config file name with the extension.
   *
   * @return Config file name with extension.
   */
  private String getConfigFileName() {
    return this.name + ".yml";
  }
}
