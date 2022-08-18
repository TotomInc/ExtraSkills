package io.totominc.extraskills;

import io.totominc.extraskills.abilities.AbilityManager;
import io.totominc.extraskills.commands.ReloadCommand;
import io.totominc.extraskills.configuration.OptionManager;
import io.totominc.extraskills.data.BossBarManager;
import io.totominc.extraskills.data.PlayerDataManager;
import io.totominc.extraskills.data.storage.PlayerStorageManager;
import io.totominc.extraskills.data.storage.YamlPlayerStorageManager;
import io.totominc.extraskills.leveler.Leveler;
import io.totominc.extraskills.listeners.BlockListeners;
import io.totominc.extraskills.listeners.PlayerJoinQuitListeners;
import io.totominc.extraskills.skills.SkillManager;
import io.totominc.extraskills.skills.combat.CombatLeveler;
import io.totominc.extraskills.skills.enchanter.EnchanterLeveler;
import io.totominc.extraskills.skills.mining.MiningLeveler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class ExtraSkills extends JavaPlugin {
  private static ExtraSkills instance;
  private static BukkitAudiences adventure;
  private OptionManager optionManager;
  private BossBarManager bossBarManager;
  private PlayerStorageManager playerStorageManager;
  private PlayerDataManager playerDataManager;
  private SkillManager skillManager;
  private AbilityManager abilityManager;
  private Leveler leveler;

  @Override
  public void onEnable() {
    this.saveDefaultConfig();

    instance = this;
    adventure = BukkitAudiences.create(this);

    // OptionManager should be first, as others classes may depend on some options loaded.
    this.optionManager = new OptionManager();
    this.bossBarManager = new BossBarManager();
    // In the future, we should support more storage providers such as MySQL, JSON or MongoDB.
    this.playerStorageManager = new YamlPlayerStorageManager(this);
    this.playerDataManager = new PlayerDataManager();
    this.skillManager = new SkillManager();
    this.abilityManager = new AbilityManager();
    this.leveler = new Leveler(this);

    this.registerCommands();
    this.registerEvents();
    this.registerOnlinePlayers();
  }

  @Override
  public void onDisable() {
    this.saveOnlinePlayers();

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

  public OptionManager getOptionManager() {
    return this.optionManager;
  }

  public BossBarManager getBossBarManager() {
    return this.bossBarManager;
  }

  public PlayerStorageManager getPlayerStorageManager() {
    return this.playerStorageManager;
  }

  public PlayerDataManager getPlayerDataManager() {
    return this.playerDataManager;
  }

  public SkillManager getSkillManager() {
    return this.skillManager;
  }

  public AbilityManager getAbilityManager() {
    return this.abilityManager;
  }

  public Leveler getLeveler() {
    return this.leveler;
  }

  private void registerCommands() {
    PluginCommand esreload = this.getCommand("esreload");

    if (esreload != null) {
      esreload.setExecutor(new ReloadCommand());
    }
  }

  private void registerEvents() {
    PluginManager pluginManager = getServer().getPluginManager();

    pluginManager.registerEvents(new BlockListeners(this), this);
    pluginManager.registerEvents(new PlayerJoinQuitListeners(), this);

    pluginManager.registerEvents(new CombatLeveler(this), this);
    pluginManager.registerEvents(new EnchanterLeveler(this), this);
    pluginManager.registerEvents(new MiningLeveler(this), this);
  }

  private void registerOnlinePlayers() {
    // Loop on all online players and load their PlayerData from a save-file.
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (!player.isOnline()) {
        return;
      }

      this.getPlayerStorageManager().loadPlayerData(player);
    }
  }

  private void saveOnlinePlayers() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (!player.isOnline()) {
        return;
      }

      this.playerStorageManager.savePlayerData(player, false);
    }
  }
}
