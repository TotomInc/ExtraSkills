package io.totominc.ExtraSkills.skills;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public abstract class Skill implements Listener {
  private final String id;
  private final EnumMap<Material, Double> rewards = new EnumMap<>(Material.class);
  private final int MAX_LEVEL = 500;

  public Skill(String id) {
    this.id = id;

    Skills.registerSkill(this);
  }

  /**
   * Initializer method, should be called on plugin initialization. Make sure
   * to also call this method when plugin configuration has been reloaded.
   *
   * Register various events handlers inside the server plugin-manager.
   *
   * After skill has been initialized, it will call the `afterUpdate` method.
   */
  public void update() {
    ExtraSkills.getInstance().getServer().getPluginManager().registerEvents(this, ExtraSkills.getInstance());

    afterUpdate();
  }

  /**
   * Called after plugin has been initialized. Override if needed.
   */
  public abstract void afterUpdate();

  /**
   * Return the ID of this skill.
   *
   * @return ID of this skill.
   */
  @NotNull
  public String getID() {
    return this.id;
  }

  /**
   * Return the EnumMap of the potential rewards for this skill.
   *
   * @return EnumMap of rewards for this skill.
   */
  @NotNull
  public EnumMap<Material, Double> getRewards() {
    return this.rewards;
  }

  /**
   * Return the experience reward amount for a specified material.
   *
   * @param material A material.
   * @return Can return null if there is no material found.
   */
  public double getReward(Material material) {
    return this.rewards.get(material);
  }
}
