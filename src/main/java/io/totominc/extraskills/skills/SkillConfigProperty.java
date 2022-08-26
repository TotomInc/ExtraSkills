package io.totominc.extraskills.skills;

public enum SkillConfigProperty {
  ENABLED("enabled"),
  MAX_LEVEL("max_level"),
  EXPERIENCE_EXPRESSION("experience_expression"),
  SOURCES("sources");

  public final String path;

  SkillConfigProperty(String path) {
    this.path = path;
  }
}
