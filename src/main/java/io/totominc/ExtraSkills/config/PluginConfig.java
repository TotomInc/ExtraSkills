package io.totominc.ExtraSkills.config;

import io.totominc.ExtraSkills.config.skilllevelup.SkillLevelupConfig;
import io.totominc.ExtraSkills.config.skillprogression.SkillProgressionConfig;
import org.bukkit.configuration.InvalidConfigurationException;

public final class PluginConfig {
  private final SkillProgressionConfig skillProgressionConfig;
  private final SkillLevelupConfig skillLevelupConfig;

  public PluginConfig() throws InvalidConfigurationException {
    this.skillProgressionConfig = new SkillProgressionConfig();
    this.skillLevelupConfig = new SkillLevelupConfig();
  }

  public SkillProgressionConfig getSkillProgressionConfig() {
    return this.skillProgressionConfig;
  }

  public SkillLevelupConfig getSkillLevelupConfig() {
    return this.skillLevelupConfig;
  }
}
