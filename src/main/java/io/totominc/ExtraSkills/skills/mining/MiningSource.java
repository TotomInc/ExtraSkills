package io.totominc.ExtraSkills.skills.mining;

import org.bukkit.Material;

import java.util.EnumMap;

public final class MiningSource {
  private final EnumMap<Material, Double> rewards = new EnumMap<>(Material.class);

  public MiningSource() {
    this.loadRewards();
  }

  public EnumMap<Material, Double> getRewards() {
    return rewards;
  }

  private void loadRewards() {
    this.rewards.put(Material.getMaterial("stone"), 1.00);
    this.rewards.put(Material.getMaterial("cobblestone"), 0.25);
  }
}
