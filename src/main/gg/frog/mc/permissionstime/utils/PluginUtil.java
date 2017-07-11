package gg.frog.mc.permissionstime.utils;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import gg.frog.mc.permissionstime.PluginMain;

public class PluginUtil {

    private static PluginMain pm = PluginMain.getInstance();

    public static UUID getPlayerUUIDByName(String name) {
        for (Player p : pm.getServer().getOnlinePlayers()) {
            if (p.getName().equals(name)) {
                return p.getUniqueId();
            }
        }
        for (OfflinePlayer p : pm.getServer().getOfflinePlayers()) {
            if (p.getName().equals(name)) {
                return p.getUniqueId();
            }
        }
        return null;
    }
}
