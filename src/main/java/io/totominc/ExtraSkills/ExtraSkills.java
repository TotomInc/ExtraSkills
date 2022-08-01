package io.totominc.ExtraSkills;

import io.totominc.ExtraSkills.commands.ReloadCommand;
import io.totominc.ExtraSkills.data.PlayerDataManager;
import io.totominc.ExtraSkills.listeners.BlockListeners;
import io.totominc.ExtraSkills.listeners.PlayerJoinQuitListeners;
import io.totominc.ExtraSkills.skills.mining.MiningLeveler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class ExtraSkills extends JavaPlugin {
  private static ExtraSkills instance;
  private static BukkitAudiences adventure;
  private PlayerDataManager playerDataManager;

  @Override
  public void onEnable() {
    this.saveDefaultConfig();

    instance = this;
    adventure = BukkitAudiences.create(this);

    this.playerDataManager = new PlayerDataManager();

    this.registerCommands();
    this.registerEvents();
  }

  @Override
  public void onDisable() {
    instance = null;

    if (adventure != null) {
      adventure.close();
      adventure = null;
    }
  }

  public static ExtraSkills getInstance() {
    return instance;
  }

  public static BukkitAudiences getAdventure() {
    if (adventure == null) {
      throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
    }

    return adventure;
  }

  public PlayerDataManager getPlayerDataManager() {
    return playerDataManager;
  }

  private void registerCommands() {
    PluginCommand esreload = this.getCommand("esreload");

    if (esreload != null) {
      esreload.setExecutor(new ReloadCommand());
    }
  }

  private void registerEvents() {
    PluginManager pluginManager = getServer().getPluginManager();

    pluginManager.registerEvents(new BlockListeners(), this);
    pluginManager.registerEvents(new PlayerJoinQuitListeners(), this);

    pluginManager.registerEvents(new MiningLeveler(), this);
  }
}
