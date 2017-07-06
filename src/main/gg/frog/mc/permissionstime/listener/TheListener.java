package gg.frog.mc.permissionstime.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import gg.frog.mc.permissionstime.PluginMain;

public class TheListener implements Listener {
    
    private PluginMain pm = PluginMain.getInstance();

    /**
     * 一个监听器例子
     * 
     * @param e
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        pm.getServer().broadcastMessage("[" + e.getPlayer().getName() + "]进入了服务器!");
        e.setJoinMessage("[" + e.getPlayer().getName() + "]进入了服务器!");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        pm.getServer().broadcastMessage("[" + e.getPlayer().getName() + "]退出了服务器!");
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        pm.getServer().broadcastMessage("[" + e.getPlayer().getName() + "]被踢出了服务器!");
    }
}
