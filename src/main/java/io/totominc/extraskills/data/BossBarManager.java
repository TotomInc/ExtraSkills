package io.totominc.extraskills.data;

import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.configuration.Option;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.util.Ticks;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BossBarManager {
  private final Map<UUID, BossBar> bossBarMap = new HashMap<>();

  // Increment by 1 each time a boss-bar is displayed. This is used with the schedule task.
  // In other words, this won't make the `i + 1` boss-bar disappear because the schedule
  // task of the `i` previous boss-bar has been finished and triggered.
  private final Map<UUID, Integer> actionIndexMap = new HashMap<>();

  // Get display time in seconds from configuration file and convert it to ticks duration.
  private final double displayTime = ExtraSkills.getInstance().getOptionManager().getDouble(Option.BOSS_BAR_DISPLAY_TIME) * Ticks.TICKS_PER_SECOND;

  public void sendBossBar(Player player, BossBar bossBar) {
    UUID uuid = player.getUniqueId();
    BossBar activeBossBar = this.bossBarMap.get(uuid);
    Integer actionIndex = this.actionIndexMap.get(uuid);

    if (activeBossBar == null) {
      this.bossBarMap.put(uuid, bossBar);
    } else {
      ExtraSkills.getInstance().getAdventure().player(player).hideBossBar(activeBossBar);

      this.bossBarMap.replace(uuid, bossBar);
    }

    if (actionIndex == null) {
      this.actionIndexMap.put(uuid, 0);
    } else {
      this.actionIndexMap.replace(uuid, actionIndex + 1);
    }

    ExtraSkills.getInstance().getAdventure().player(player).showBossBar(bossBar);

    this.scheduleHideBossBar(player);
  }

  public void removePlayer(Player player) {
    UUID uuid = player.getUniqueId();
    BossBar bossBar = this.bossBarMap.get(uuid);
    Integer actionIndex = this.actionIndexMap.get(uuid);

    if (bossBar != null) {
      ExtraSkills.getInstance().getAdventure().player(player).hideBossBar(bossBar);
      this.bossBarMap.remove(uuid);
    }

    if (actionIndex != null) {
      this.actionIndexMap.remove(uuid);
    }
  }

  private void scheduleHideBossBar(Player player) {
    UUID uuid = player.getUniqueId();
    int staleActionIndex = this.actionIndexMap.get(uuid);

    new BukkitRunnable() {
      @Override
      public void run() {
        // Verify if the data is present, because if player leaves too fast the data
        // that we try to access below is null.
        if (!bossBarMap.containsKey(uuid) || !actionIndexMap.containsKey(uuid)) {
          return;
        }

        BossBar currentBossBar = bossBarMap.get(uuid);
        int currentActionIndex = actionIndexMap.get(uuid);

        if (staleActionIndex == currentActionIndex && currentBossBar != null) {
          hideBossBar(player);
        }
      }
    }.runTaskLater(ExtraSkills.getInstance(), (long) this.displayTime);
  }

  private void hideBossBar(Player player) {
    BossBar bossBar = this.bossBarMap.get(player.getUniqueId());

    if (bossBar != null) {
      ExtraSkills.getInstance().getAdventure().player(player).hideBossBar(bossBar);
    }
  }
}
