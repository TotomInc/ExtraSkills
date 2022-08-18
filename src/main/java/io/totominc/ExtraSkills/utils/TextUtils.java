package io.totominc.extraskills.utils;

import org.apache.commons.text.WordUtils;

public final class TextUtils {
  public static String getCapitalizedString(String string) {
    return WordUtils.capitalize(string);
  }

  public static String get2DecimalsString(double value) {
    return String.format("%.2f", value);
  }

  public static String getDoubleWithoutDecimals(double value) {
    return String.valueOf(value).split("\\.")[0];
  }
}
