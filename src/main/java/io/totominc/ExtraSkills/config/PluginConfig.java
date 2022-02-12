package io.totominc.ExtraSkills.config;

import io.totominc.ExtraSkills.config.skillprogression.SkillProgressionConfig;
import org.bukkit.configuration.InvalidConfigurationException;

public final class PluginConfig {
  private final SkillProgressionConfig skillProgressionConfig;

  public PluginConfig() throws InvalidConfigurationException {
    this.skillProgressionConfig = new SkillProgressionConfig();
  }

  public SkillProgressionConfig getSkillProgressionConfig() {
    return this.skillProgressionConfig;
  }
}
