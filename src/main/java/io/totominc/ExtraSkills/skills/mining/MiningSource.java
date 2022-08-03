package io.totominc.ExtraSkills.skills.mining;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

public final class MiningSource {
  private final EnumMap<Material, Double> rewards = new EnumMap<>(Material.class);

  public MiningSource() {
    this.loadRewards();
  }

  @Nullable
  public Double getReward(Material material) {
    return this.rewards.get(material);
  }

  // TODO: load rewards from config file.
  private void loadRewards() {
    this.rewards.put(Material.getMaterial("STONE"), 1.00);
    this.rewards.put(Material.getMaterial("COBBLESTONE"), 0.25);
  }
}
