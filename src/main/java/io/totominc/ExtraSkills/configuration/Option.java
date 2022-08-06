package io.totominc.ExtraSkills.configuration;

public enum Option {
  ACTION_BAR_ENABLED("action_bar.enabled", OptionType.BOOLEAN),
  ACTION_BAR_ENABLE_SKILL_EXPERIENCE("action_bar.enable_for_skill_experience_gained", OptionType.BOOLEAN),
  ACTION_BAR_SKILL_EXPERIENCE_FORMAT("action_bar.skill_experience_gained_format", OptionType.STRING);

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
