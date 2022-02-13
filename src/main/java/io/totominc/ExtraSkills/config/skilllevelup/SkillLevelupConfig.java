package io.totominc.ExtraSkills.config.skilllevelup;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public final class SkillLevelupConfig {
  private SoundConfig soundConfig;
  private ChatConfig chatConfig;
  private BroadcastConfig broadcastConfig;

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
   * Get the Chat configuration.
   *
   * @return Chat configuration.
   */
  public ChatConfig getChatConfig() {
    return this.chatConfig;
  }

  /**
   * Get the Broadcast configuration.
   *
   * @return Broadcast configuration.
   */
  public BroadcastConfig getBroadcastConfig() {
    return this.broadcastConfig;
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
    ConfigurationSection chat = progressionSection.getConfigurationSection("chat");
    ConfigurationSection broadcast = progressionSection.getConfigurationSection("broadcast");

    if (sound == null || chat == null || broadcast == null) {
      throw new InvalidConfigurationException("Invalid \"skill-level-up\" configuration");
    }

    this.soundConfig = new SoundConfig(
      sound.getBoolean("enabled"),
      sound.getString("name"),
      (float) sound.getDouble("volume"),
      (float) sound.getDouble("pitch")
    );

    this.chatConfig = new ChatConfig(chat.getBoolean("enabled"), chat.getString("format"));

    this.broadcastConfig = new BroadcastConfig(
      broadcast.getBoolean("enabled"),
      broadcast.getString("format"),
      broadcast.getIntegerList("levels")
    );
  }
}
