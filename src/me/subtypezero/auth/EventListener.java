package me.subtypezero.auth;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class EventListener implements org.bukkit.event.Listener {
	private TOTP plugin;

	public EventListener(TOTP plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();

		if ((this.plugin.sessionUse) && (this.plugin.thread.session.containsKey(name)) && (this.plugin.checkLastIp(player))) {
			player.sendMessage(ChatColor.GREEN + "Extended session from last login");
			return;
		}

		if (this.plugin.isRegistered(player)) {
			if ((!player.isOp()) && (!player.hasPermission("*")) && (!player.hasPermission("TOTP.verify"))) {
				String uuid = Util.getUUID(player.getName());

				if (this.plugin.getConfig().contains(uuid + ".secret")) {
					this.plugin.getConfig().set(uuid + ".secret", null);
					this.plugin.saveConfig();
					player.sendMessage(ChatColor.RED + "Your 2-factor authentication has been removed.");

					if (this.plugin.getConfig().contains(uuid + ".pass")) {
						player.sendMessage(ChatColor.RED + "Your password will be used instead.");
					} else {
						player.sendMessage(ChatColor.RED + "Please register a password to protect your account.");
						return;
					}
				}
			}
			this.plugin.authList.put(name, Boolean.valueOf(false));
			player.sendMessage(ChatColor.RED + "Please login using /login <password>");

			if (this.plugin.blindness) {
				player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS, 1728000, 15));
			}
		} else if ((player.isOp()) || (player.hasPermission("*")) || (player.hasPermission("TOTP.verify"))) {
			player.kickPlayer("TOTP registration required");
			this.plugin.getLogger().warning("TOTP registration is required for player: " + player.getName());
			this.plugin.getLogger().warning("Please use the register command in console to verify this user.");

			for (Player p : Bukkit.getOnlinePlayers()) {
				if ((p.isOp()) || (p.hasPermission("*")) || (player.hasPermission("TOTP.verify"))) {
					p.sendMessage("TOTP registration is required for player: " + player.getName());
					p.sendMessage("Please use the register command in console to verify this user.");
				}
			}
		} else {
			return;
		}

		if (this.plugin.timeoutUse) {
			this.plugin.thread.timeout.put(player.getName().toLowerCase(), Integer.valueOf(this.plugin.timeoutDelay));
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		String name = event.getName();
		for (Player p : Bukkit.getOnlinePlayers()) {
			String pname = p.getName().toLowerCase();
			if ((!this.plugin.authList.containsKey(pname)) &&
					(pname.equalsIgnoreCase(name))) {
				event.setLoginResult(org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
				event.setKickMessage("A player with this name is already online!");
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();

		if (plugin.isRegistered(player)) {
			plugin.updateIp(player);
		}
		if ((plugin.sessionUse) && (!this.plugin.authList.containsKey(name)) && (plugin.isRegistered(player))) {
			plugin.thread.session.put(name, plugin.sessionDelay);
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();

		if (plugin.authList.containsKey(name)) {
			player.teleport(event.getFrom());
		} else if ((player.hasPotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS)) && (this.plugin.blindness)) {
			player.removePotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();

		if (plugin.authList.containsKey(name)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();

		if (plugin.authList.containsKey(name)) {
			event.setCancelled(false);
		}
	}

	@EventHandler
	public void playerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();

		if (plugin.authList.containsKey(name)) {
			event.setCancelled(false);
		}
	}

	@EventHandler
	public void playerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();

		if (plugin.authList.containsKey(name)) {
			event.setCancelled(false);
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();

		if (plugin.authList.containsKey(name)) {
			event.setCancelled(false);
		}
	}

	@EventHandler
	public void onHealthRegain(EntityRegainHealthEvent event) {
		Entity entity = event.getEntity();

		if (!(entity instanceof Player)) {
			return;
		}
		Player player = (Player) entity;
		String name = player.getName().toLowerCase();

		if (plugin.authList.containsKey(name)) {
			event.setCancelled(false);
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		Entity entity = event.getEntity();

		if (!(entity instanceof Player)) {
			return;
		}
		Player player = (Player) entity;
		String name = player.getName().toLowerCase();

		if (plugin.authList.containsKey(name)) {
			event.setCancelled(false);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Entity entity = event.getWhoClicked();

		if (!(entity instanceof Player)) {
			return;
		}
		Player player = (Player) entity;
		String name = player.getName().toLowerCase();

		if (plugin.authList.containsKey(name)) {
			event.setCancelled(false);
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity defender = event.getEntity();
		Entity damager = event.getDamager();

		if ((defender instanceof Player)) {
			Player p1 = (Player) defender;
			String n1 = p1.getName().toLowerCase();

			if (plugin.authList.containsKey(n1)) {
				event.setCancelled(true);
				return;
			}
			if ((damager instanceof Player)) {
				Player p2 = (Player) damager;
				String n2 = p2.getName().toLowerCase();

				if (plugin.authList.containsKey(n2)) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerComamndPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String name = player.getName().toLowerCase();

		if ((plugin.authList.containsKey(name)) && (!event.getMessage().startsWith("/login"))) {
			if (event.getMessage().startsWith("/f")) {
				event.setMessage("/" + RandomStringUtils.randomAscii(name.length()));
			}
			event.setCancelled(true);
		}
	}
}