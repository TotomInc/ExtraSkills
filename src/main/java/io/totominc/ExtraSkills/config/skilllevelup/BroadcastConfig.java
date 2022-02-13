package io.totominc.ExtraSkills.config.skilllevelup;

import java.util.List;

public record BroadcastConfig(boolean isEnabled, String format, List<Integer> levels) {}
