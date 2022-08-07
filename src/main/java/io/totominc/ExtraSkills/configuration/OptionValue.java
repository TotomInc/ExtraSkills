package io.totominc.ExtraSkills.configuration;

import java.util.ArrayList;
import java.util.List;

public final class OptionValue {
  private Object value;

  public OptionValue(Object value) {
    this.value = value;
  }

  public Object getValue() {
    return this.value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public boolean asBoolean() {
    return (boolean) this.value;
  }

  public int asInt() {
    return (int) this.value;
  }

  public double asDouble() {
    if (this.value instanceof Integer) {
      return ((Integer) this.value).doubleValue();
    }

    return (double) this.value;
  }

  public String asString() {
    if (this.value instanceof String) {
      return (String) this.value;
    }

    return String.valueOf(this.value);
  }

  public List<String> asList() {
    List<String> strings = new ArrayList<>();

    if (this.value instanceof List<?>) {
      for (Object object : (List<?>) value) {
        if (object instanceof String) {
          strings.add((String) object);
        }
      }
    }

    return strings;
  }
}
