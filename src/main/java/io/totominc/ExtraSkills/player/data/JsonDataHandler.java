package io.totominc.ExtraSkills.player.data;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.player.PlayerSkill;
import io.totominc.ExtraSkills.skills.Skill;
import io.totominc.ExtraSkills.skills.Skills;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class JsonDataHandler {
  private static final ExtraSkills instance = ExtraSkills.getInstance();
  private static final String userdata = "userdata";

  /**
   * Get a file pointing to the userdata folder of the plugin.
   *
   * @return File.
   */
  private static File getPlayerSaveDirectory() {
    return new File(instance.getDataFolder(), userdata + File.separator);
  }

  /**
   * Get a file pointing to the player save file using its UUID.
   *
   * @param playerUuid Bukkit UUID of a Player instance.
   * @return File.
   */
  private static File getPlayerSaveFile(UUID playerUuid) {
    return new File(instance.getDataFolder(), userdata + File.separator + playerUuid + ".json");
  }

  /**
   * Make sure the "userdata" folder and the player save file are already
   * created. If they do not exist, automatically create necessary folders and
   * files.
   *
   * @param playerUuid Bukkit UUID of a Player instance.
   * @return True if everything has been created or is already existing.
   * @throws IOException Thrown if it cannot read/write to file/folders.
   */
  private static boolean isPlayerSaveSafe(@NotNull UUID playerUuid) throws IOException {
    File saveDirectory = getPlayerSaveDirectory();
    File saveFile = getPlayerSaveFile(playerUuid);

    if (!saveDirectory.exists()) {
      boolean isCreated = saveDirectory.mkdirs();

      if (!isCreated) {
        throw new IOException("Unable to create userdata folder");
      }
    }

    if (!saveFile.exists()) {
      boolean isCreated = saveFile.createNewFile();

      if (!isCreated) {
        throw new IOException("Unable to create a save-file for the player: " + playerUuid);
      }
    }

    return true;
  }

  /**
   * Try to load the player save file into a JSONObject.
   *
   * @param playerUuid Bukkit UUID of a Player instance.
   * @return JSONObject.
   * @throws IOException Thrown if it cannot read/write to file/folders.
   */
  private static JSONObject loadPlayerSave(@NotNull UUID playerUuid) throws IOException {
    File playerSaveFile = getPlayerSaveFile(playerUuid);

    if (!playerSaveFile.exists()) {
      return null;
    }

    String fileContent = FileUtils.readFileToString(playerSaveFile, "ISO-8859-1");

    if (fileContent == null) {
      return null;
    }

    return new JSONObject(fileContent);
  }

  /**
   * Save everything related to an ExtraSkillsPlayer instance into its own
   * player save file using its UUID as a file name.
   *
   * @param playerSkillMap Map of PlayerSkill.
   * @param playerUuid Bukkit UUID of a Player instance.
   * @throws IOException Thrown if it cannot read/write to file/folders.
   */
  public static void savePlayerState(@NotNull Map<String, PlayerSkill> playerSkillMap, @NotNull UUID playerUuid) throws IOException {
    if (isPlayerSaveSafe(playerUuid)) {
      PlayerData playerData = new PlayerData(playerUuid, playerSkillMap);
      FileUtils.writeStringToFile(getPlayerSaveFile(playerUuid), playerData.toJSON().toString(), "ISO-8859-1");
    }
  }

  /**
   * Load player save file and create a PlayerData instance with updated skills
   * from save file.
   *
   * @param playerUuid Bukkit UUID of a Player instance.
   * @return PlayerData instance.
   * @throws IOException Thrown if it cannot read/write to file/folders.
   */
  public static PlayerData loadPlayerState(@NotNull UUID playerUuid) throws IOException {
    JSONObject playerSave = loadPlayerSave(playerUuid);

    if (playerSave != null) {
      UUID savedUuid = UUID.fromString(playerSave.getString("uuid"));
      Map<String, PlayerSkill> savedSkills = new HashMap<>();

      for (Skill skill : Skills.values()) {
        JSONObject savedSkill = playerSave.getJSONObject("skills").getJSONObject(skill.getID());
        PlayerSkill playerSkill = new PlayerSkill(skill.getID());

        if (savedSkill != null) {
          playerSkill.setLevel(savedSkill.getInt("level"));
          playerSkill.setExperience(savedSkill.getDouble("experience"));
        }

        savedSkills.put(playerSkill.id, playerSkill);
      }

      return new PlayerData(savedUuid, savedSkills);
    }

    return null;
  }
}
