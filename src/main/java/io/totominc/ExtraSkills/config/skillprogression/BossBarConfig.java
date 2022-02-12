package io.totominc.ExtraSkills.config.skillprogression;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public record BossBarConfig(boolean isEnabled, BarStyle segments, BarColor color, String format) {}
