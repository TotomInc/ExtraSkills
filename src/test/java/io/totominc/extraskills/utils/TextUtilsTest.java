package io.totominc.extraskills.utils;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.totominc.extraskills.ExtraSkills;
import org.junit.jupiter.api.*;

public class TextUtilsTest {
  private static ServerMock server;
  private static ExtraSkills instance;

  @BeforeAll
  public static void load() {
    server = MockBukkit.mock();
    instance = MockBukkit.load(ExtraSkills.class);
  }

  @AfterAll
  public static void unload() {
    MockBukkit.unmock();
  }

  @Test
  @DisplayName("It should format properly a capitalized string.")
  public void testCapitalizedString() {
    Assertions.assertEquals(TextUtils.getCapitalizedString("hello world"), "Hello World");
  }

  @Test
  @DisplayName("It should format properly a 2 decimals string with rounding up.")
  public void test2dString() {
    Assertions.assertEquals(TextUtils.get2DecimalsString(250.986), "250.99");
  }

  @Test
  @DisplayName("It should format properly a double into a string without decimals.")
  public void test0dString() {
    Assertions.assertEquals(TextUtils.getDoubleWithoutDecimals(250.986), "250");
  }
}
