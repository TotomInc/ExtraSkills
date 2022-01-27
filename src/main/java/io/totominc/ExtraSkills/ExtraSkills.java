package io.totominc.ExtraSkills;

import io.totominc.ExtraSkills.commands.ReloadCommand;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class ExtraSkills extends JavaPlugin {
  private static ExtraSkills instance;

  /**
   * Called when plugin is enabling.
   */
  @Override
  public void onEnable() {
    instance = this;

    registerCommands();
  }

  /**
   * Called when plugin is disabling.
   */
  @Override
  public void onDisable() {
    instance = null;
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
   * Register all the plugin commands.
   */
  private void registerCommands() {
    PluginCommand esreload = this.getCommand("esreload");

    if (esreload != null) {
      esreload.setExecutor(new ReloadCommand());
    }
  }
}
