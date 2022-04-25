package com.edgeburnmedia.playerfreeze;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor {
	private final PlayerFreeze plugin;

	public FreezeCommand(PlayerFreeze plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (command.getName()) {
			case "freeze":
				if (args.length == 1) {
					if (sender.hasPermission("playerfreeze.freeze")) {
						Player target = Bukkit.getPlayer(args[0]);
						if (target != null) {
							final boolean targetIsExempt = target.hasPermission("playerfreeze.exempt");
							sender.sendMessage(targetIsExempt ? "§c" + target.getDisplayName() + " is exempt from being frozen." : "§a" + target.getDisplayName() + " is now frozen.");
							if (!targetIsExempt) {
								plugin.freezePlayer(args[0]);
							} else if (plugin.isFrozen(target)) {
								sender.sendMessage("§c" + target.getDisplayName() + " is already frozen.");
							}
							return true;
						} else {
							sender.sendMessage("§cPlayer not found");
							return true;
						}
					}
				} else {
					return false;
				}
				break;
			case "unfreeze":
				if (args.length == 1) {
					if (sender.hasPermission("playerfreeze.freeze")) {
						Player target = Bukkit.getPlayer(args[0]);
						if (target != null) {
							if (plugin.isFrozen(target)) {
								plugin.unfreezePlayer(target);
								sender.sendMessage("§a" + target.getDisplayName() + "§a is now unfrozen.");
							} else {
								sender.sendMessage("§c" + target.getDisplayName() + "§c was not frozen. No action taken.");
							}
						} else {
							sender.sendMessage("§cPlayer not found");
						}
						return true;
					} else {
						return false;
					}
				}
				break;
			case "freezeall":
				if (sender.hasPermission("playerfreeze.freezeall")) {
					plugin.freezeAllPlayers();
					sender.sendMessage("§aAll players are now frozen.");
					return true;
				}
				break;
			case "unfreezeall":
				if (sender.hasPermission("playerfreeze.unfreezeall")) {
					plugin.unfreezeAllPlayers();
					sender.sendMessage("§aAll players are now unfrozen.");
				}
				break;
			default:
				return false;
		}
		return true;
	}
}
