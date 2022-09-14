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
import io.totominc.extraskills.skills.harvester.HarvesterLeveler;
import io.totominc.extraskills.skills.lumberjack.LumberjackLeveler;
import io.totominc.extraskills.skills.mining.MiningLeveler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;

@SuppressWarnings("unused")
public class ExtraSkills extends JavaPlugin {
  private static ExtraSkills instance;
  private static Economy vaultEconomy;
  private static boolean vaultEnabled;
  private BukkitAudiences adventure;

  private OptionManager optionManager;
  private BossBarManager bossBarManager;
  private PlayerStorageManager playerStorageManager;
  private PlayerDataManager playerDataManager;
  private SkillManager skillManager;
  private AbilityManager abilityManager;
  private Leveler leveler;

  public ExtraSkills() {
    super();
  }

  // Protected constructor needed for MockBukkit unit-testing.
  protected ExtraSkills(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
    super(loader, description, dataFolder, file);
  }

  @Override
  public void onEnable() {
    this.saveDefaultConfig();

    instance = this;

    // Initialize Adventure.
    // https://docs.adventure.kyori.net/platform/bukkit.html
    this.adventure = BukkitAudiences.create(this);

    vaultEnabled = this.setupEconomy();

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

    System.out.println("on disable");

    if (this.adventure != null) {
      this.adventure.close();
      this.adventure = null;
    }
  }

  public static ExtraSkills getInstance() {
    return instance;
  }

  public @NonNull BukkitAudiences getAdventure() {
    if (this.adventure == null) {
      throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
    }

    return this.adventure;
  }

  public static Economy getVaultEconomy() {
    return vaultEconomy;
  }

  public static boolean getIsVaultEnabled() {
    return vaultEnabled;
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

  private boolean setupEconomy() {
    if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
      return false;
    }

    RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);

    if (rsp == null) {
      return false;
    }

    vaultEconomy = rsp.getProvider();

    return true;
  }

  private void registerCommands() {
    PluginCommand esreload = this.getCommand("esreload");

    if (esreload != null) {
      esreload.setExecutor(new ReloadCommand());
    }
  }

  private void registerEvents() {
    PluginManager pluginManager = getServer().getPluginManager();

    // Global listeners.
    pluginManager.registerEvents(new BlockListeners(this), this);
    pluginManager.registerEvents(new PlayerJoinQuitListeners(), this);

    // Per skill listeners.
    pluginManager.registerEvents(new CombatLeveler(this), this);
    pluginManager.registerEvents(new EnchanterLeveler(this), this);
    pluginManager.registerEvents(new HarvesterLeveler(this), this);
    pluginManager.registerEvents(new LumberjackLeveler(this), this);
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
