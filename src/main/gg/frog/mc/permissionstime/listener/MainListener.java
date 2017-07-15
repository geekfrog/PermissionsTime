package gg.frog.mc.permissionstime.listener;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class MainListener implements Listener {

    private PluginMain pm;

    public MainListener(PluginMain pm) {
        this.pm = pm;
    }

    /**
     * 一个监听器例子
     * 
     * @param e
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<PlayerDataBean> pdbList = pm.getSqlManager().getTime(event.getPlayer().getUniqueId().toString());
                    PermissionPackageBean.reloadPlayerPermissions(event.getPlayer(), pdbList, pm);
                } catch (Exception e) {
                    e.printStackTrace();
                    event.getPlayer().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "修改权限失败, 请重新进入服务器!"));
                }
            }
        }).start();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PermissionPackageBean.delPlayerAllPermissions(event.getPlayer(), pm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PermissionPackageBean.delPlayerAllPermissions(event.getPlayer(), pm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        if (StrUtil.messageFormat("&4===权限仓库===" + "&r&5&9&2&0&r").equals(event.getInventory().getName())) {
            event.setCancelled(true);
        }
    }
}
