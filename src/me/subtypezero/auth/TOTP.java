package me.subtypezero.auth;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import me.subtypezero.auth.cmd.*;
import org.apache.commons.codec.binary.Base32;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class TOTP extends org.bukkit.plugin.java.JavaPlugin {
	public static TOTP instance;
	public HashMap<String, Boolean> authList = new HashMap();
	public HashMap<String, org.bukkit.command.CommandExecutor> commandMap = new HashMap();
	public ThreadManager thread;
	public boolean blindness;
	public boolean sessionUse;
	public boolean timeoutUse;
	public int sessionDelay;
	public int timeoutDelay;

	public void onEnable() {
		FileConfiguration config = getConfig();
		PluginManager pm = getServer().getPluginManager();
		config.addDefault("settings.blindness", Boolean.valueOf(true));
		config.addDefault("settings.session.use", Boolean.valueOf(true));
		config.addDefault("settings.session.delay", Integer.valueOf(60));
		config.addDefault("settings.timeout.use", Boolean.valueOf(true));
		config.addDefault("settings.timeout.delay", Integer.valueOf(60));
		config.addDefault("settings.host", "host.com");
		config.options().copyDefaults(true);
		saveConfig();

		instance = (TOTP)pm.getPlugin("TOTP");
		thread = new ThreadManager(this);
		blindness = config.getBoolean("settings.blindness", true);
		sessionUse = config.getBoolean("settings.session.use", true);
		sessionDelay = config.getInt("settings.session.delay", 60);
		timeoutUse = config.getBoolean("settings.timeout.use", true);
		timeoutDelay = config.getInt("settings.timeout.delay", 60);

		if (this.sessionUse) {
			this.thread.startSessionTask();
		}
		if (this.timeoutUse) {
			this.thread.startTimeoutTask();
		}

		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		getCommand("login").setExecutor(new LoginCommand());
		getCommand("logout").setExecutor(new LogoutCommand());
		getCommand("register").setExecutor(new RegisterCommand());
		getCommand("unregister").setExecutor(new UnregisterCommand());
		getCommand("recover").setExecutor(new RecoverCommand());
		getLogger().info("TOTP Enabled!");
	}

	public void onDisable() {
		getLogger().info("TOTP Disabled!");
	}

	public String generateSecret() {
		byte[] buffer = new byte[10];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(buffer);
		Base32 base32 = new Base32();
		return new String(base32.encode(buffer));
	}

	private long getTimeIndex() {
		return System.currentTimeMillis() / 1000L / 30L;
	}

	private long getCode(byte[] secret, long timeIndex) throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signKey = new javax.crypto.spec.SecretKeySpec(secret, "HmacSHA1");
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(timeIndex);
		byte[] timeBytes = buffer.array();
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(timeBytes);
		int offset = hash[19] & 0xF;
		long truncatedHash = hash[offset] & 0x7F;
		for (int i = 1; i < 4; i++) {
			truncatedHash <<= 8;
			truncatedHash |= hash[(offset + i)] & 0xFF;
		}
		return truncatedHash %= 1000000L;
	}

	public boolean checkCode(Player player, String password) {
		String uuid = Util.getUUID(player.getName());
		if (getConfig().contains(uuid + ".secret")) {
			byte[] secret;
			Base32 base32 = new Base32();
			secret = base32.decode(getConfig().getString(uuid + ".secret", ""));
			long code = -1L;
			try {
				code = getCode(secret, getTimeIndex());
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			if (Long.parseLong(password) == code) {
				return true;
			}
			return false;
		}

		if (getConfig().getString(uuid + ".pass").equals(password)) {
			return true;
		}
		return false;
	}

	public boolean isRegistered(Player player) {
		String uuid = Util.getUUID(player.getName());
		if ((getConfig().contains(uuid + ".secret")) || (getConfig().contains(uuid + ".pass"))) {
			return true;
		}
		return false;
	}

	public boolean isRegistered(String name) {
		String uuid = Util.getUUID(name);
		if ((getConfig().contains(uuid + ".secret")) || (getConfig().contains(uuid + ".pass"))) {
			return true;
		}
		return false;
	}

	public boolean checkLastIp(Player player) {
		if (isRegistered(player)) {
			String uuid = Util.getUUID(player.getName());
			String lastIp = getConfig().getString(uuid + ".lastIp", "");
			String currentIp = player.getAddress().getAddress().toString();
			return lastIp.equalsIgnoreCase(currentIp);
		}
		return false;
	}

	public void updateIp(Player player) {
		if (isRegistered(player)) {
			String uuid = Util.getUUID(player.getName());
			String ip = player.getAddress().getAddress().toString();
			getConfig().set(uuid + ".lastIp", ip);
			saveConfig();
		}
	}
}