package io.totominc.ExtraSkills.config.skillprogression;

import net.kyori.adventure.bossbar.BossBar;

public record BossBarConfig(boolean isEnabled, BossBar.Overlay segments, BossBar.Color color, String format, long removeAfterSeconds) {}
