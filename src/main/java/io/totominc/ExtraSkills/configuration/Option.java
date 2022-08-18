package io.totominc.extraskills.configuration;

public enum Option {
  ACTION_BAR_ENABLED("action_bar.enabled", OptionType.BOOLEAN),
  ACTION_BAR_ENABLE_SKILL_EXPERIENCE("action_bar.enable_for_skill_experience_gained", OptionType.BOOLEAN),
  ACTION_BAR_DISABLE_MAX_LEVEL("action_bar.disable_on_max_level", OptionType.BOOLEAN),
  ACTION_BAR_SKILL_EXPERIENCE_FORMAT("action_bar.skill_experience_gained_format", OptionType.STRING),
  ACTION_BAR_SKILL_MAX_LEVEL_FORMAT("action_bar.skill_max_level_format", OptionType.STRING),
  BOSS_BAR_ENABLED("boss_bar.enabled", OptionType.BOOLEAN),
  BOSS_BAR_DISABLE_MAX_LEVEL("boss_bar.disable_on_max_level", OptionType.BOOLEAN),
  BOSS_BAR_TYPE("boss_bar.type", OptionType.STRING),
  BOSS_BAR_COLOR("boss_bar.color", OptionType.STRING),
  BOSS_BAR_DISPLAY_TIME("boss_bar.display_time", OptionType.INT),
  BOSS_BAR_ENABLE_SKILL_EXPERIENCE("boss_bar.enable_for_skill_experience_gained", OptionType.BOOLEAN),
  BOSS_BAR_SKILL_EXPERIENCE_FORMAT("boss_bar.skill_experience_gained_format", OptionType.STRING),
  BOSS_BAR_SKILL_MAX_LEVEL_FORMAT("boss_bar.skill_max_level_format", OptionType.STRING),
  TITLE_ENABLED("title.enabled", OptionType.BOOLEAN),
  TITLE_ENABLE_SKILL_LEVELUP("title.enable_for_skill_level_up", OptionType.BOOLEAN),
  TITLE_SKILL_LEVELUP_TITLE("title.skill_level_up_title", OptionType.STRING),
  TITLE_SKILL_LEVELUP_DESCRIPTION("title.skill_level_up_description", OptionType.STRING),
  SOUND_ENABLED("sound.enabled", OptionType.BOOLEAN),
  SOUND_ENABLE_SKILL_LEVELUP("sound.enabled_for_skill_level_up", OptionType.BOOLEAN),
  SOUND_SKILL_LEVELUP_SOUND("sound.skill_level_up_sound", OptionType.STRING),
  SOUND_SKILL_LEVELUP_SOURCE("sound.skill_level_up_sound_source", OptionType.STRING),
  SOUND_SKILL_LEVELUP_PITCH("sound.skill_level_up_pitch", OptionType.DOUBLE),
  SOUND_SKILL_LEVELUP_VOLUME("sound.skill_level_up_volume", OptionType.DOUBLE),
  ABILITY_HASTER_DURATION("abilities.haster.duration_in_ticks", OptionType.INT),
  ABILITY_HASTER_POTION_EFFECT("abilities.haster.potion_effect", OptionType.STRING),
  ABILITY_HASTER_AMPLIFIER("abilities.haster.amplifier", OptionType.INT),
  DISABLED_GAMEMODES("disabled_gamemodes", OptionType.LIST),
  DISABLED_WORLDS("disabled_worlds", OptionType.LIST),
  ENABLE_GAIN_MONEY_SKILL_LEVELUP("enable_gain_money_on_skill_levelup", OptionType.BOOLEAN),
  MONEY_SKILL_LEVELUP_EXPRESSION("money_skill_levelup_expression", OptionType.STRING),
  CHECK_PLAYER_BLOCK_PLACED("check_player_block_placed", OptionType.BOOLEAN),
  USE_GLOBAL_EXPERIENCE_EXPRESSION("use_global_experience_expression", OptionType.BOOLEAN),
  GLOBAL_EXPERIENCE_EXPRESSION("global_experience_expression", OptionType.STRING);

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
