package me.subtypezero.auth.cmd;

import me.subtypezero.auth.TOTP;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LogoutCommand implements org.bukkit.command.CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
		if (plugin.authList.containsKey(name)) {
			player.sendMessage(ChatColor.RED + "You must log in first");
			return true;
		}
		plugin.authList.put(name, Boolean.valueOf(false));
		plugin.thread.session.remove(name);
		plugin.thread.timeout.put(name, Integer.valueOf(plugin.timeoutDelay));

		if (plugin.blindness) {
			player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS, 1728000, 15));
		}
		player.sendMessage(ChatColor.GREEN + "Successfully logged out");
		return true;
	}
}