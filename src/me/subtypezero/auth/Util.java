package me.subtypezero.auth;

import org.json.*;
import java.net.*;
import java.io.*;

public class Util {

	public static String getUUID(final String name) {
		final String source = getHtmlSource("https://api.mojang.com/users/profiles/minecraft/" + name);
		final JSONObject ob = new JSONObject(source);
		return ob.getString("id");
	}

	private static String getHtmlSource(final String host) {
		final StringBuilder sb = new StringBuilder();
		InputStream is;

		try {
			final URL url = new URL(host);
			is = url.openStream();
			final BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
