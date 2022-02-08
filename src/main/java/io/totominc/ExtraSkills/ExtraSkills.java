package io.totominc.ExtraSkills;

import io.totominc.ExtraSkills.commands.ReloadCommand;
import io.totominc.ExtraSkills.config.PluginConfig;
import io.totominc.ExtraSkills.listeners.PlayerBlockListener;
import io.totominc.ExtraSkills.listeners.PlayerListeners;
import io.totominc.ExtraSkills.player.ExtraSkillsPlayerManager;
import io.totominc.ExtraSkills.skills.Skills;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class ExtraSkills extends JavaPlugin {
  private static ExtraSkills instance;
  private static BukkitAudiences adventure;
  private static PluginConfig pluginConfig;

  /**
   * Called when plugin is enabling.
   */
  @Override
  public void onEnable() {
    this.saveDefaultConfig();

    instance = this;
    adventure = BukkitAudiences.create(this);

    try {
      pluginConfig = new PluginConfig();
    } catch (InvalidConfigurationException e) {
      e.printStackTrace();
    }

    this.registerEvents();
    this.registerCommands();

    ExtraSkillsPlayerManager.addPlayers(this.getServer().getOnlinePlayers());
    Skills.update();
  }

  /**
   * Called when plugin is disabling.
   */
  @Override
  public void onDisable() {
    instance = null;
    adventure = null;
    pluginConfig = null;

    ExtraSkillsPlayerManager.clearPlayers();
  }

  /**
   * Returns the instance of the plugin.
   *
   * @return ExtraSkills plugin instance.
   */
  public static ExtraSkills getInstance() {
    return instance;
  }

  /**
   * Returns the instance of Adventure.
   *
   * @return BukkitAudiences instance.
   */
  public static BukkitAudiences getAdventure() {
    if (adventure == null) {
      throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
    }

    return adventure;
  }

  /**
   * Returns the instance of the PluginConfig.
   *
   * @return PluginConfig instance.
   */
  public static PluginConfig getPluginConfig() {
    return pluginConfig;
  }

  /**
   * Register various events listeners.
   */
  private void registerEvents() {
    this.getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
    this.getServer().getPluginManager().registerEvents(new PlayerBlockListener(), this);
  }

  /**
   * Register all the plugin commands.
   */
  private void registerCommands() {
    PluginCommand esreload = this.getCommand("esreload");

    if (esreload != null) {
      esreload.setExecutor(new ReloadCommand());
    }
  }
}
