package io.totominc.ExtraSkills.configuration;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public final class OptionManager {
  private final HashMap<Option, OptionValue> optionsMap = new HashMap<>();

  public OptionManager() {
    try {
      this.loadOptions();
    } catch (InvalidConfigurationException ignored) {}
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

  private void loadOptions() throws InvalidConfigurationException {
    InputStream configStream = ExtraSkills.getInstance().getResource("config.yml");

    if (configStream == null) {
      throw new InvalidConfigurationException("Unable to load configuration, no \"config.yml\" file found.");
    }

    FileConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(configStream));

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
