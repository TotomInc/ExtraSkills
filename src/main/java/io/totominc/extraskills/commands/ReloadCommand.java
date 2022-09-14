package io.totominc.extraskills.commands;

import io.totominc.extraskills.ExtraSkills;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    Component message = MiniMessage.miniMessage().deserialize("<gold>The plugin configuration has been reloaded.");

    ExtraSkills.getInstance().reloadConfig();
    sender.sendMessage(message);

    return true;
  }
}
