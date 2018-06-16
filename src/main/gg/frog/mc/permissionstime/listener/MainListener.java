package gg.frog.mc.permissionstime.listener;

import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.config.TagNameCfg;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.model.cfg.PlayerTagBean;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

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
					List<PlayerDataBean> pdbList = pm.getSqlManager().getTime(event.getPlayer().getUniqueId().toString());
					PermissionPackageBean.reloadPlayerPermissions(event.getPlayer(), pdbList, pm, false);
				} catch (Exception e) {
					e.printStackTrace();
					event.getPlayer().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_FAIL_SET_PERMISSION));
				}
				if (PluginCfg.TAG_SYSTEM) {
					String uuid = event.getPlayer().getUniqueId().toString();
					PlayerTagBean playerTag = null;
					if (TagNameCfg.PLAYER_TAG.containsKey(uuid)) {
						playerTag = TagNameCfg.PLAYER_TAG.get(uuid);
						playerTag.setPlayerDisplayName(event.getPlayer());
					} else {
						playerTag = new PlayerTagBean("playerTag/" + uuid + ".yml", pm);
						playerTag.setPlayerDisplayName(event.getPlayer(), true);
					}
					TagNameCfg.PLAYER_TAG.put(uuid, playerTag);
				}
			}
		}).start();
	}

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
		if (StrUtil.messageFormat(LangCfg.INVENTORY_NAME + "&r&5&9&2&0&r").equals(event.getInventory().getName())) {
			event.setCancelled(true);
		}
		if (StrUtil.messageFormat(LangCfg.TAG_INVENTORY_NAME + "&r&5&9&2&0&r").equals(event.getInventory().getName())) {
			OfflinePlayer player = this.pm.getOfflinePlayer(event.getWhoClicked().getName());
			UUID uuid = player.getUniqueId();
			if (TagNameCfg.PLAYER_TAG.containsKey(uuid.toString()) && event.getCurrentItem() != null) {
				List<String> lores = event.getCurrentItem().getItemMeta().getLore();
				if (lores.size() == 6) {
					String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
					PlayerTagBean playerTag = TagNameCfg.PLAYER_TAG.get(uuid.toString());
					if (itemName.startsWith(StrUtil.messageFormat("&6&l昵称效果 &r"))) {
						playerTag.setNamecolor(lores.get(5));
					} else if (itemName.startsWith(StrUtil.messageFormat("&6&l昵称前缀 &r"))) {
						playerTag.setPrefix(lores.get(5));
					} else if (itemName.startsWith(StrUtil.messageFormat("&6&l昵称后缀 &r"))) {
						playerTag.setSuffix(lores.get(5));
					} else {
						event.setCancelled(true);
					}
					playerTag.setPlayerDisplayName(player.getPlayer(), true);
					playerTag.saveConfig();
					event.getWhoClicked().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_TAG_SET_SUCCESS));
				}
			}
			event.setCancelled(true);
		}
	}
}
