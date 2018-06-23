package gg.frog.mc.permissionstime.listener;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import gg.frog.mc.base.PluginMain;
import gg.frog.mc.base.config.LangCfg;
import gg.frog.mc.base.config.PluginCfg;
import gg.frog.mc.base.utils.StrUtil;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;

public class MainListener implements Listener {

	private PluginMain pm;

	public MainListener(PluginMain pm) {
		this.pm = pm;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerLoginEvent event) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String uuid = pm.getPlayerUUIDByName(event.getPlayer().getName());
					List<PlayerDataBean> pdbList = pm.getSqlManager().getTime(uuid);
					PermissionPackageBean.reloadPlayerPermissions(event.getPlayer(), pdbList, pm, false);
				} catch (Exception e) {
					e.printStackTrace();
					event.getPlayer().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_FAIL_SET_PERMISSION));
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
		pm.getPlayerMap().remove(event.getPlayer().getName());
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
		pm.getPlayerMap().remove(event.getPlayer().getName());
	}

	@EventHandler
	public void onPlayerClick(InventoryClickEvent event) {
		if (StrUtil.messageFormat(LangCfg.INVENTORY_NAME + "§r§5§9§2§0§r").equals(event.getInventory().getName())) {
			event.setCancelled(true);
		}
	}
}
