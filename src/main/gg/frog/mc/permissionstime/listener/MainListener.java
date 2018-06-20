package gg.frog.mc.permissionstime.listener;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.config.TagNameCfg;
import gg.frog.mc.permissionstime.model.PlayerTagBean;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
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
					String uuid = pm.getPlayerUUIDByName(event.getPlayer().getName());
					List<PlayerDataBean> pdbList = pm.getSqlManager().getTime(uuid);
					PermissionPackageBean.reloadPlayerPermissions(event.getPlayer(), pdbList, pm, false);
				} catch (Exception e) {
					e.printStackTrace();
					event.getPlayer().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_FAIL_SET_PERMISSION));
				}
				if (PluginCfg.TAG_SYSTEM) {
					PlayerTagBean.initPlayerTag(event.getPlayer(), pm);
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
					if (PluginCfg.TAG_SYSTEM && TagNameCfg.USE_HD_PLUGIN) {
						String uuid = pm.getPlayerUUIDByName(event.getPlayer().getName());
						PlayerTagBean playerTag = TagNameCfg.PLAYER_TAG.get(uuid);
						playerTag.delHologramsName();
					}
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
	public void onMove(PlayerMoveEvent event) {
		if (PluginCfg.TAG_SYSTEM && TagNameCfg.USE_HD_PLUGIN) {
			String uuid = this.pm.getPlayerUUIDByName(event.getPlayer().getName());
			PlayerTagBean playerTag = TagNameCfg.PLAYER_TAG.get(uuid);
			if (playerTag != null) {
				playerTag.moveHologramsName(event.getPlayer());
			}
		}
	}

	@EventHandler
	public void onPlayerClick(InventoryClickEvent event) {
		if (StrUtil.messageFormat(LangCfg.INVENTORY_NAME + "&r&5&9&2&0&r").equals(event.getInventory().getName())) {
			event.setCancelled(true);
		}
		if (StrUtil.messageFormat(LangCfg.TAG_INVENTORY_NAME + "&r&5&9&2&0&r").equals(event.getInventory().getName())) {
			if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().hasLore()) {
				List<String> lores = event.getCurrentItem().getItemMeta().getLore();
				if (lores.size() > 1) {
					String permissions = lores.get(lores.size() - 2);
					permissions = permissions.length() > 4 ? permissions.substring(4) : "";
					OfflinePlayer player = this.pm.getOfflinePlayer(event.getWhoClicked().getName());
					String uuid = player.getUniqueId().toString();
					if (permissions.length() == 0 || player.getPlayer().hasPermission(permissions)) {
						if (TagNameCfg.PLAYER_TAG.containsKey(uuid)) {
							String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
							PlayerTagBean playerTag = TagNameCfg.PLAYER_TAG.get(uuid);
							if (itemName.startsWith("§6§l昵称效果§r ")) {
								playerTag.setNamecolor(lores.get(lores.size() - 1).substring(2));
							} else if (itemName.startsWith("§6§l昵称前缀§r ")) {
								playerTag.setPrefix(lores.get(lores.size() - 1).substring(2));
							} else if (itemName.startsWith("§6§l昵称后缀§r ")) {
								playerTag.setSuffix(lores.get(lores.size() - 1).substring(2));
							} else {
								event.setCancelled(true);
								return;
							}
							playerTag.setPlayerDisplayName(player.getPlayer(), true);
							playerTag.saveConfig();
							player.getPlayer().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_TAG_SET_SUCCESS));
						}
					} else {
						player.getPlayer().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NO_PERMISSION));
					}
				}
			}
			event.setCancelled(true);
		}
	}
}
