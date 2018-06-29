package gg.frog.mc.base.utils.nms;

import org.bukkit.Bukkit;

public class NMSUtil {

	private static String serverVersion;

	static {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		serverVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
	}

	public static String getServerVersion() {
		return serverVersion;
	}

	public static Class<?> getNmsClass(String name) throws ClassNotFoundException {
		return Class.forName("net.minecraft.server." + getServerVersion() + "." + name);
	}

	public static Class<?> getObcClass(String name) throws ClassNotFoundException {
		return Class.forName("org.bukkit.craftbukkit." + getServerVersion() + "." + name);
	}

	public static Class<?> getNmClass(String name) throws ClassNotFoundException {
		return Class.forName("net.minecraft." + name);
	}

}
