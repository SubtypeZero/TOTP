 package me.subtypezero.auth.cmd;

import me.subtypezero.auth.TOTP;
import me.subtypezero.auth.Util;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class UnregisterCommand implements org.bukkit.command.CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		TOTP plugin = TOTP.instance;

		if ((sender instanceof Player)) {
			Player player = (Player) sender;

			if ((player.isOp()) || (player.hasPermission("*")) || (player.hasPermission("TOTP.verify"))) {
				player.sendMessage(ChatColor.RED + "This command must be used by Console");
				return true;
			}

			if (args.length < 1) {
				player.sendMessage(ChatColor.RED + "Not enough arguments");
				player.sendMessage(ChatColor.RED + "Usage: /unregister <password>");
				return true;
			}

			if (!plugin.isRegistered(player)) {
				player.sendMessage(ChatColor.RED + "You are not registered!");
				return true;
			}

			if (plugin.checkCode(player, args[0])) {
				plugin.getConfig().set(Util.getUUID(player.getName()) + ".pass", null);
				plugin.saveConfig();
				player.sendMessage(ChatColor.GREEN + "You have been succesfully unregistered.");
			} else {
				player.sendMessage(ChatColor.RED + "Invalid password");
			}

			return true;
		}

		if (args.length < 1) {
			sender.sendMessage("Not enough arguments");
			sender.sendMessage("Usage: unregister <name>");
			return true;
		}

		if (!plugin.isRegistered(args[0])) {
			sender.sendMessage("That user is not registered!");
			return true;
		}

		plugin.authList.remove(args[0].toLowerCase());
		plugin.thread.timeout.remove(args[0].toLowerCase());
		plugin.getConfig().set(Util.getUUID(args[0]) + ".secret", null);
		plugin.getConfig().set(Util.getUUID(args[0]) + ".pass", null);
		plugin.saveConfig();

		Player player = org.bukkit.Bukkit.getPlayer(args[0]);
		if ((player != null) && (plugin.blindness) && (player.hasPotionEffect(PotionEffectType.BLINDNESS))) {
			player.removePotionEffect(PotionEffectType.BLINDNESS);
		}
		sender.sendMessage("Successfully unregistered user.");
		return true;
	}
}
