package io.totominc.ExtraSkills.configuration;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public final class OptionManager {
  private final HashMap<Option, OptionValue> optionsMap = new HashMap<>();

  public OptionManager() {
    this.loadOptions();
  }

  public boolean getBoolean(Option option) {
    return this.optionsMap.get(option).asBoolean();
  }

  public int getInt(Option option) {
    return this.optionsMap.get(option).asInt();
  }

  public double getDouble(Option option) {
    return this.optionsMap.get(option).asDouble();
  }

  public String getString(Option option) {
    return this.optionsMap.get(option).asString();
  }

  private void loadOptions() {
    FileConfiguration config = ExtraSkills.getInstance().getConfig();

    for (Option option : Option.values()) {
      String path = option.getPath();

      if (option.getType() == OptionType.BOOLEAN) {
        this.optionsMap.put(option, new OptionValue(config.getBoolean(path)));
      } else if (option.getType() == OptionType.INT) {
        this.optionsMap.put(option, new OptionValue(config.getInt(path)));
      } else if (option.getType() == OptionType.DOUBLE) {
        this.optionsMap.put(option, new OptionValue(config.getDouble(path)));
      } else if (option.getType() == OptionType.STRING) {
        this.optionsMap.put(option, new OptionValue(config.getString(path)));
      }
    }
  }
}
