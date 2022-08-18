package io.totominc.extraskills.data.storage;

import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.data.PlayerData;
import io.totominc.extraskills.skills.Skill;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public final class YamlPlayerStorageManager extends PlayerStorageManager {
  private final ArrayList<UUID> saveLock = new ArrayList<>();
  private final ArrayList<UUID> saveDisabled = new ArrayList<>();

  public YamlPlayerStorageManager(ExtraSkills instance) {
    super(instance);
  }

  @Override
  public void loadPlayerData(Player player) {
    UUID uuid = player.getUniqueId();
    File file = this.getPlayerFile(uuid);
    PlayerData playerData = this.createPlayerData(player);

    // No save file found for the player, abort and create fresh PlayerData.
    if (!file.exists()) {
      this.instance.getPlayerDataManager().addPlayerData(uuid, playerData);
      return;
    }

    FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    try {
      UUID configUuid = UUID.fromString(config.getString("uuid", uuid.toString()));

      // Verify if the player UUID is the same as the one stored inside the player save-file.
      if (!uuid.equals(configUuid)) {
        throw new IllegalArgumentException("Player UUID doesn't match the UUID field inside the player-save");
      }

      // Load all skills.
      for (Skill skill : Skill.values()) {
        String skillPath = "skills." + skill.name().toLowerCase();

        playerData.setSkillLevel(skill, config.getDouble(skillPath + ".level"));
        playerData.setSkillExperience(skill, config.getDouble(skillPath + ".experience"));
      }

      // Load PlayerData inside PlayerDataManager.
      this.instance.getPlayerDataManager().addPlayerData(uuid, playerData);
    } catch (Exception err) {
      // In case of loading error, start a "guest" session: empty PlayerData, no save allowed.
      this.saveDisabled.add(uuid);
      this.instance.getPlayerDataManager().addPlayerData(uuid, new PlayerData(player));

      Bukkit.getLogger().warning("Unable to load player save " + player.getDisplayName() + "(UUID: " + uuid + "). Please, send the error below to the developer for a fix, thanks!");
      err.printStackTrace();
    }
  }

  @Override
  public void savePlayerData(Player player, boolean isLeaving) {
    UUID uuid = player.getUniqueId();

    if (this.saveDisabled.contains(uuid) && isLeaving) {
      this.saveDisabled.remove(uuid);
      return;
    }

    if (this.saveLock.contains(uuid) || this.saveDisabled.contains(uuid)) {
      return;
    }

    PlayerData playerData = this.instance.getPlayerDataManager().getPlayerData(uuid);

    if (playerData == null) {
      return;
    }

    File file = this.getPlayerFile(uuid);
    FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    // Set the save lock for this player.
    this.saveLock.add(uuid);

    try {
      config.set("uuid", uuid.toString());

      // Save all skills.
      for (Skill skill : Skill.values()) {
        String skillPath = "skills." + skill.name().toLowerCase();

        config.set(skillPath + ".level", playerData.getSkillLevel(skill));
        config.set(skillPath + ".experience", playerData.getSkillExperience(skill));
      }

      // Apply changes from the in-memory FileConfiguration to the File.
      config.save(file);

      // Remove the save lock, because everything seems to be good at this point.
      this.saveLock.remove(uuid);
    } catch (Exception err) {
      // In case of any error, keep the save lock to true.
      Bukkit.getLogger().warning("Unable to save player " + player.getDisplayName() + "(UUID: " + uuid + "). Please, send the error below to the developer for a fix, thanks!");
      err.printStackTrace();
    }

    this.instance.getPlayerDataManager().removePlayerData(player.getUniqueId());
  }

  private File getPlayerFile(UUID uuid) {
    return new File(this.instance.getDataFolder() + "/players/" + uuid + ".yml");
  }
}
