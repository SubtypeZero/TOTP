package me.subtypezero.auth.cmd;

import me.subtypezero.auth.TOTP;
import me.subtypezero.auth.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements org.bukkit.command.CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		TOTP plugin = TOTP.instance;

		if ((sender instanceof Player)) {
			Player player = (Player)sender;

			if ((player.isOp()) || (player.hasPermission("*")) || (player.hasPermission("TOTP.verify"))) {
				player.sendMessage(ChatColor.RED + "This command must be used by Console");
				return true;
			}
			if (args.length < 1) {
				player.sendMessage(ChatColor.RED + "Not enough arguments");
				player.sendMessage(ChatColor.RED + "Usage: /register <password>");
				return true;
			}
			if (plugin.isRegistered(player)) {
				player.sendMessage(ChatColor.RED + "You are already registered!");
				return true;
			}
			plugin.getConfig().set(Util.getUUID(player.getName()) + ".pass", args[0]);
			plugin.saveConfig();
			player.sendMessage(ChatColor.GREEN + "You have been succesfully registered.");
			return true;
		}

		if (args.length < 1) {
			sender.sendMessage("Not enough arguments");
			sender.sendMessage("Usage: register <name>");
			return true;
		}

		String uuid = Util.getUUID(args[0]);
		if (plugin.getConfig().contains(uuid + ".secret")) {
			sender.sendMessage("That user is already registered!");
			return true;
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			if ((player.getName().equalsIgnoreCase(args[0])) && (!player.isOp()) && (!player.hasPermission("*")) && (!player.hasPermission("TOTP.verify"))) {
				sender.sendMessage("Normal users must be registered in-game.");
				return true;
			}
		}

		if (plugin.getConfig().contains(uuid + ".pass")) {
			sender.sendMessage("Upgrading user to two-factor authentication.");
		}

		String secret = plugin.generateSecret();
		plugin.getConfig().set(uuid + ".secret", secret);
		plugin.saveConfig();
		sender.sendMessage("QR Code: https://chart.googleapis.com/chart?chs=400x400&cht=qr&chl=200x200&chld=M|0&cht=qr&chl=otpauth://totp/admin@" + plugin.getConfig().getString("settings.host") + "%3Fsecret%3D" + secret);
		return true;
	}
}