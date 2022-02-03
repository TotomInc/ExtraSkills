package io.totominc.ExtraSkills.player.data;

import io.totominc.ExtraSkills.ExtraSkills;
import io.totominc.ExtraSkills.player.PlayerSkill;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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
  public static File getPlayerSaveDirectory() {
    return new File(instance.getDataFolder(), userdata + File.separator);
  }

  /**
   * Get a file pointing to the player save file using its UUID.
   *
   * @param playerUuid Bukkit UUID of a Player instance.
   * @return File.
   */
  public static File getPlayerSaveFile(UUID playerUuid) {
    return new File(instance.getDataFolder(), userdata + File.separator + playerUuid + ".json");
  }

  /**
   * Given a Map of PlayerSkill, return a JSONObject containing the value of
   * each skill. Each PlayerSkill converted into JSON using
   * `PlayerSkill.toJSON`.
   *
   * @param playerSkillMap Map of PlayerSkill.
   * @return JSONObject.
   */
  public static JSONObject getPlayerSkillMapJson(@NotNull Map<String, PlayerSkill> playerSkillMap) {
    JSONObject root = new JSONObject();

    for (PlayerSkill playerSkill : playerSkillMap.values()) {
      root.put(playerSkill.id, playerSkill.toJson());
    }

    return root;
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
    if (checkPlayerSave(playerUuid)) {
      JSONObject playerSkillMapJson = getPlayerSkillMapJson(playerSkillMap);
      JSONObject root = new JSONObject();

      root.put("uuid", playerUuid.toString());
      root.put("skills", playerSkillMapJson);

      FileUtils.writeStringToFile(getPlayerSaveFile(playerUuid), root.toString(), "ISO-8859-1");
    }
  }

  /**
   * Try to load the player save file into a JSONObject.
   *
   * @param playerUuid Bukkit UUID of a Player instance.
   * @return JSONObject.
   * @throws IOException Thrown if it cannot read/write to file/folders.
   */
  public static JSONObject loadPlayerSave(@NotNull UUID playerUuid) throws IOException {
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

  public static PlayerData loadPlayerState(@NotNull UUID playerUuid) throws IOException {
    JSONObject playerSave = loadPlayerSave(playerUuid);

    if (playerSave != null) {
      if (playerSave.has("skills")) {
        JSONObject skills = playerSave.getJSONObject("skills");
      }

      return new PlayerData();
    }

    return null;
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
  private static boolean checkPlayerSave(@NotNull UUID playerUuid) throws IOException {
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
}
