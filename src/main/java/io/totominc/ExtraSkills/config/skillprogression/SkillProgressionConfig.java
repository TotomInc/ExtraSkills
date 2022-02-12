package io.totominc.ExtraSkills.config.skillprogression;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.Sound;
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

  public ActionBarConfig getActionBarConfig() {
    return this.actionBarConfig;
  }

  public SoundConfig getSoundConfig() {
    return this.soundConfig;
  }

  public BossBarConfig getBossBarConfig() {
    return this.bossBarConfig;
  }

  private void loadSkillProgression() throws InvalidConfigurationException {
    FileConfiguration config = ExtraSkills.getInstance().getConfig();
    ConfigurationSection progressionSection = config.getConfigurationSection("skill-progression");

    if (progressionSection == null) {
      throw new InvalidConfigurationException("Invalid \"skill-progression\" configuration");
    }

    ConfigurationSection actionbar = progressionSection.getConfigurationSection("action-bar");
    ConfigurationSection sound = progressionSection.getConfigurationSection("sound");
    ConfigurationSection bossBar = progressionSection.getConfigurationSection("boss-bar");

    if (actionbar != null) {
      this.actionBarConfig = new ActionBarConfig(actionbar.getBoolean("enabled"), actionbar.getString("format"));
    }

    if (sound != null) {
      Sound name = Sound.valueOf(sound.getString("name"));

      this.soundConfig = new SoundConfig(sound.getBoolean("enabled"), name, sound.getDouble("pitch"));
    }

    if (bossBar != null) {
      BarStyle segments = BarStyle.valueOf(bossBar.getString("segments"));
      BarColor color = BarColor.valueOf(bossBar.getString("color"));

      this.bossBarConfig = new BossBarConfig(bossBar.getBoolean("enabled"), segments, color, bossBar.getString("format"));
    }
  }
}
