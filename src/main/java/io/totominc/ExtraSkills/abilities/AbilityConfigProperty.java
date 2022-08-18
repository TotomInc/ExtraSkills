package io.totominc.extraskills.abilities;

public enum AbilityConfigProperty {
  ENABLED("enabled"),
  BASE_VALUE("base_value"),
  VALUE_GAINED_PER_LEVEL("value_gained_per_level"),
  UNLOCK_LEVEL("unlock_level"),
  LEVEL_UP_RATE("level_up_rate"),
  MAX_LEVEL("max_level"),
  BLOCKS("blocks"),
  ENTITIES("entities");

  public final String path;

  AbilityConfigProperty(String path) {
    this.path = path;
  }
}
