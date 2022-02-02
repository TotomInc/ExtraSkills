package io.totominc.ExtraSkills.skills;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public abstract class Skill implements Listener {
  private final ExtraSkills instance = ExtraSkills.getInstance();
  private final String id;
  private final EnumMap<Material, Double> rewards = new EnumMap<>(Material.class);
  private final SkillConfig skillConfig;

  private final int MAX_LEVEL;

  public Skill(String id) {
    this.id = id;
    this.skillConfig = new SkillConfig(id);
    this.MAX_LEVEL = this.skillConfig.getConfig().getInt("max-level");

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
    this.instance.getServer().getPluginManager().registerEvents(this, this.instance);
    this.afterUpdate();
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

  /**
   * Get the SkillConfig instance of this skill.
   *
   * @return SkillConfig instance for this skill.
   */
  public SkillConfig getConfig() {
    return this.skillConfig;
  }
}
