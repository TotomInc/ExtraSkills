package io.totominc.ExtraSkills;

import io.totominc.ExtraSkills.abilities.AbilityManager;
import io.totominc.ExtraSkills.commands.ReloadCommand;
import io.totominc.ExtraSkills.configuration.OptionManager;
import io.totominc.ExtraSkills.data.BossBarManager;
import io.totominc.ExtraSkills.data.PlayerDataManager;
import io.totominc.ExtraSkills.leveler.Leveler;
import io.totominc.ExtraSkills.listeners.BlockListeners;
import io.totominc.ExtraSkills.listeners.PlayerJoinQuitListeners;
import io.totominc.ExtraSkills.skills.SkillManager;
import io.totominc.ExtraSkills.skills.combat.CombatLeveler;
import io.totominc.ExtraSkills.skills.mining.MiningLeveler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class ExtraSkills extends JavaPlugin {
  private static ExtraSkills instance;
  private static BukkitAudiences adventure;
  private OptionManager optionManager;
  private BossBarManager bossBarManager;
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
    this.playerDataManager = new PlayerDataManager();
    this.skillManager = new SkillManager();
    this.abilityManager = new AbilityManager();
    this.leveler = new Leveler(this);

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

  public OptionManager getOptionManager() {
    return this.optionManager;
  }

  public BossBarManager getBossBarManager() {
    return this.bossBarManager;
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
    pluginManager.registerEvents(new MiningLeveler(this), this);
  }
}
