package io.totominc.ExtraSkills.config.skilllevelup;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public final class SkillLevelupConfig {
  private SoundConfig soundConfig;

  public SkillLevelupConfig() {
    try {
      this.load();
    } catch (InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the Sound configuration.
   *
   * @return Sound configuration.
   */
  public SoundConfig getSoundConfig() {
    return this.soundConfig;
  }

  /**
   * Initialize skill-level-up configuration from reading config.yml.
   *
   * @throws InvalidConfigurationException Thrown when configuration is invalid.
   */
  private void load() throws InvalidConfigurationException {
    FileConfiguration config = ExtraSkills.getInstance().getConfig();
    ConfigurationSection progressionSection = config.getConfigurationSection("skill-level-up");

    if (progressionSection == null) {
      throw new InvalidConfigurationException("Invalid \"skill-level-up\" configuration");
    }

    ConfigurationSection sound = progressionSection.getConfigurationSection("sound");

    if (sound == null) {
      throw new InvalidConfigurationException("Invalid \"skill-level-up\" configuration");
    }

    this.soundConfig = new SoundConfig(
        sound.getBoolean("enabled"),
        sound.getString("name"),
        (float) sound.getDouble("volume"),
        (float) sound.getDouble("pitch")
    );
  }
}
