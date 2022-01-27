package io.totominc.ExtraSkills;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class ExtraSkills extends JavaPlugin {
  @Override
  public void onEnable() {
    this.getLogger().info("Hello, World!");
  }

  @Override
  public void onDisable() {
    this.getLogger().info("Goodbye, World...");
  }
}
