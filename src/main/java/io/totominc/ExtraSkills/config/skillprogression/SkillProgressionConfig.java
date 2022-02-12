package io.totominc.ExtraSkills.config.skillprogression;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public final class SkillProgressionConfig {
  private ActionBarConfig actionBarConfig;
  private SoundConfig soundConfig;
  private BossBarConfig bossBarConfig;

  public SkillProgressionConfig() {
    try {
      this.loadSkillProgression();
    } catch (InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the ActionBar configuration.
   *
   * @return ActionBar configuration.
   */
  public ActionBarConfig getActionBarConfig() {
    return this.actionBarConfig;
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
   * Get the BossBar configuration.
   *
   * @return BossBar configuration.
   */
  public BossBarConfig getBossBarConfig() {
    return this.bossBarConfig;
  }

  /**
   * Initialize skill-progression configuration from reading config.yml.
   *
   * @throws InvalidConfigurationException Thrown when configuration is invalid.
   */
  private void loadSkillProgression() throws InvalidConfigurationException {
    FileConfiguration config = ExtraSkills.getInstance().getConfig();
    ConfigurationSection progressionSection = config.getConfigurationSection("skill-progression");

    if (progressionSection == null) {
      throw new InvalidConfigurationException("Invalid \"skill-progression\" configuration");
    }

    ConfigurationSection actionbar = progressionSection.getConfigurationSection("action-bar");
    ConfigurationSection sound = progressionSection.getConfigurationSection("sound");
    ConfigurationSection bossBar = progressionSection.getConfigurationSection("boss-bar");

    if (actionbar == null || sound == null || bossBar == null) {
      throw new InvalidConfigurationException("Invalid \"skill-progression\" configuration");
    }

    this.actionBarConfig = new ActionBarConfig(actionbar.getBoolean("enabled"), actionbar.getString("format"));

    this.soundConfig = new SoundConfig(
      sound.getBoolean("enabled"),
      sound.getString("name"),
      (float) sound.getDouble("volume"),
      (float) sound.getDouble("pitch")
    );

    this.bossBarConfig = new BossBarConfig(
      bossBar.getBoolean("enabled"),
      BarStyle.valueOf(bossBar.getString("segments")),
      BarColor.valueOf(bossBar.getString("color")),
      bossBar.getString("format")
    );
  }
}
