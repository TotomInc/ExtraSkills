package io.totominc.ExtraSkills.configuration;

public enum Option {
  ACTION_BAR_ENABLED("action_bar.enabled", OptionType.BOOLEAN),
  ACTION_BAR_ENABLE_SKILL_EXPERIENCE("action_bar.enable_for_skill_experience_gained", OptionType.BOOLEAN),
  ACTION_BAR_SKILL_EXPERIENCE_FORMAT("action_bar.skill_experience_gained_format", OptionType.STRING),
  BOSS_BAR_ENABLED("boss_bar.enabled", OptionType.BOOLEAN),
  BOSS_BAR_TYPE("boss_bar.type", OptionType.STRING),
  BOSS_BAR_COLOR("boss_bar.color", OptionType.STRING),
  BOSS_BAR_DISPLAY_TIME("boss_bar.display_time", OptionType.INT),
  BOSS_BAR_ENABLE_SKILL_EXPERIENCE("boss_bar.enable_for_skill_experience_gained", OptionType.BOOLEAN),
  BOSS_BAR_SKILL_EXPERIENCE_FORMAT("boss_bar.skill_experience_gained_format", OptionType.STRING),
  TITLE_ENABLED("title.enabled", OptionType.BOOLEAN),
  TITLE_ENABLE_SKILL_LEVELUP("title.enable_for_skill_level_up", OptionType.BOOLEAN),
  TITLE_SKILL_LEVELUP_TITLE("title.skill_level_up_title", OptionType.STRING),
  TITLE_SKILL_LEVELUP_DESCRIPTION("title.skill_level_up_description", OptionType.STRING),
  DISABLED_GAMEMODES("disabled_gamemodes", OptionType.LIST),
  DISABLED_WORLDS("disabled_worlds", OptionType.LIST),
  CHECK_PLAYER_BLOCK_PLACED("check_player_block_placed", OptionType.BOOLEAN);

  private final String path;
  private final OptionType type;

  Option(String path, OptionType type) {
    this.path = path;
    this.type = type;
  }

  public String getPath() {
    return this.path;
  }

  public OptionType getType() {
    return this.type;
  }
}
