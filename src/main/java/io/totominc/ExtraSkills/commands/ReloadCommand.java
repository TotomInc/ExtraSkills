package io.totominc.ExtraSkills.commands;

import io.totominc.ExtraSkills.ExtraSkills;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    ExtraSkills.getInstance().reloadConfig();

    sender.sendMessage("Plugin configuration has been reloaded.");

    return true;
  }
}
