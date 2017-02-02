package me.subtypezero.auth.cmd;

import me.subtypezero.auth.TOTP;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class LoginCommand implements org.bukkit.command.CommandExecutor {

	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		TOTP plugin = TOTP.instance;

		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player!");
			return true;
		}

		Player player = (Player)sender;
		String name = player.getName().toLowerCase();
		if (!plugin.isRegistered(player)) {
			player.sendMessage(ChatColor.RED + "You are not registered.");
			return true;
		}
		if (!plugin.authList.containsKey(name)) {
			player.sendMessage(ChatColor.RED + "You are already logged in!");
		}
		if (args.length < 1) {
			player.sendMessage(ChatColor.RED + "Not enough arguments");
			player.sendMessage("Usage: /login <password>");
			return true;
		}

		if (plugin.checkCode(player, args[0])) {
			plugin.authList.remove(name);
			player.sendMessage(ChatColor.GREEN + "Successfully logged in");
			plugin.thread.timeout.remove(name);

			if ((plugin.blindness) && (player.hasPotionEffect(PotionEffectType.BLINDNESS))) {
				player.removePotionEffect(PotionEffectType.BLINDNESS);
			}
		} else {
			player.sendMessage(ChatColor.RED + "Invalid password");
		}
		return true;
	}
}
