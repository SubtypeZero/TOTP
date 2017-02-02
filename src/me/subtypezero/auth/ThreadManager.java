package me.subtypezero.auth;

import java.util.Map;

import org.bukkit.entity.Player;

public class ThreadManager {
	private TOTP plugin;
	private int ses = -1;
	private int to = -1;
	public Map<String, Integer> session = new java.util.HashMap();
	public Map<String, Integer> timeout = new java.util.HashMap();

	public ThreadManager(TOTP plugin) {
		this.plugin = plugin;
	}

	public void startSessionTask() {
		ses = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				for (String user : session.keySet()) {
					int current = session.get(user);
					if (current >= 1) {
						current--;
						session.put(user, Integer.valueOf(current));
					} else {
						session.remove(user);
					}
				}
			}
		}, 20L, 20L);
	}

	public void stopSessionTask() {
		if (ses >= 0) {
			plugin.getServer().getScheduler().cancelTask(ses);
		}
		ses = -1;
	}

	public void startTimeoutTask() {
		to = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				for (String user : timeout.keySet()) {
					int current = timeout.get(user);
					if (current >= 1) {
						current--;
						ThreadManager.this.timeout.put(user, Integer.valueOf(current));
					} else {
						ThreadManager.this.session.remove(user);
						Player player = org.bukkit.Bukkit.getPlayer(user);
						if ((player != null) && (player.isOnline()) && (plugin.authList.containsKey(player.getName().toLowerCase()))) {
							player.kickPlayer("Login timed out");
						}
					}
				}
			}
		}, 20L, 20L);
	}

	public void stopTimeoutTask() {
		if (this.to >= 0) {
			this.plugin.getServer().getScheduler().cancelTask(this.to);
		}
		this.to = -1;
	}
}