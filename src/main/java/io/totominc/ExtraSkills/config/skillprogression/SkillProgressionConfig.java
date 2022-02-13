package io.totominc.ExtraSkills.config.skillprogression;

import io.totominc.ExtraSkills.ExtraSkills;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public final class SkillProgressionConfig {
  private ActionBarConfig actionBarConfig;
  private BossBarConfig bossBarConfig;

  public SkillProgressionConfig() {
    try {
      this.load();
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
  private void load() throws InvalidConfigurationException {
    FileConfiguration config = ExtraSkills.getInstance().getConfig();
    ConfigurationSection progressionSection = config.getConfigurationSection("skill-progression");

    if (progressionSection == null) {
      throw new InvalidConfigurationException("Invalid \"skill-progression\" configuration");
    }

    ConfigurationSection actionbar = progressionSection.getConfigurationSection("action-bar");
    ConfigurationSection bossBar = progressionSection.getConfigurationSection("boss-bar");

    if (actionbar == null || bossBar == null) {
      throw new InvalidConfigurationException("Invalid \"skill-progression\" configuration");
    }

    this.actionBarConfig = new ActionBarConfig(actionbar.getBoolean("enabled"), actionbar.getString("format"));

    this.bossBarConfig = new BossBarConfig(
      bossBar.getBoolean("enabled"),
      BossBar.Overlay.valueOf(bossBar.getString("segments")),
      BossBar.Color.valueOf(bossBar.getString("color")),
      bossBar.getString("format"),
      bossBar.getLong("remove-after")
    );
  }
}
