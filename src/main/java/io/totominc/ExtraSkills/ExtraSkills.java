package io.totominc.ExtraSkills;

import io.totominc.ExtraSkills.commands.ReloadCommand;
import io.totominc.ExtraSkills.listeners.PlayerListeners;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class ExtraSkills extends JavaPlugin {
  private static ExtraSkills instance;
  private static BukkitAudiences adventure;

  /**
   * Called when plugin is enabling.
   */
  @Override
  public void onEnable() {
    instance = this;
    adventure = BukkitAudiences.create(this);

    registerEvents();
    registerCommands();
  }

  /**
   * Called when plugin is disabling.
   */
  @Override
  public void onDisable() {
    instance = null;
    adventure = null;
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
   * Register various events listeners.
   */
  private void registerEvents() {
    this.getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
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
