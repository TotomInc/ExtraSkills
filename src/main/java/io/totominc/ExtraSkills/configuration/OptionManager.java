package io.totominc.extraskills.configuration;

import io.totominc.extraskills.ExtraSkills;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;

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

  public List<String> getList(Option option) {
    return this.optionsMap.get(option).asList();
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
      } else if (option.getType() == OptionType.LIST) {
        this.optionsMap.put(option, new OptionValue(config.getList(path)));
      }
    }
  }
}
