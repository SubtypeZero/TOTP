package me.subtypezero.auth.cmd;

import me.subtypezero.auth.TOTP;
import me.subtypezero.auth.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

public class RecoverCommand implements org.bukkit.command.CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		TOTP plugin = TOTP.instance;

		if ((sender instanceof Player)) {
			Player player = (Player)sender;
			player.sendMessage(org.bukkit.ChatColor.RED + "This command must be used by Console");
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage("Not enough arguments");
			sender.sendMessage("Usage: recover <name>");
			return true;
		}
		if (!plugin.isRegistered(args[0])) {
			sender.sendMessage("That user is not currently registered");
			return true;
		}

		String uuid = Util.getUUID(args[0]);
		if (plugin.getConfig().contains(uuid + ".secret")) {
			String secret = plugin.getConfig().getString(uuid + ".secret", "");
			sender.sendMessage("QR Code: https://chart.googleapis.com/chart?chs=400x400&cht=qr&chl=200x200&chld=M|0&cht=qr&chl=otpauth://totp/admin@" + plugin.getConfig().getString("settings.host") + "%3Fsecret%3D" + secret);
		} else {
			sender.sendMessage("Pass: " + plugin.getConfig().getString(new StringBuilder(String.valueOf(uuid)).append(".pass").toString()));
		}
		return true;
	}
}