package gg.frog.mc.nametags.listener;

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

import gg.frog.mc.base.PluginMain;
import gg.frog.mc.base.config.LangCfg;
import gg.frog.mc.base.config.PluginCfg;
import gg.frog.mc.base.utils.StrUtil;
import gg.frog.mc.nametags.config.TagNameCfg;
import gg.frog.mc.nametags.model.PlayerTagBean;

public class TagsListener implements Listener {

	private PluginMain pm;

	public TagsListener(PluginMain pm) {
		this.pm = pm;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerLoginEvent event) {
		if (PluginCfg.TAG_SYSTEM) {
			PlayerTagBean.initPlayerTag(event.getPlayer(), pm);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (PluginCfg.TAG_SYSTEM && TagNameCfg.USE_HD_PLUGIN) {
			String uuid = pm.getPlayerUUIDByName(event.getPlayer().getName());
			PlayerTagBean playerTag = TagNameCfg.PLAYER_TAG.get(uuid);
			playerTag.delHologramsName();
		}
	}

	@EventHandler
	public void onKick(PlayerKickEvent event) {
		if (PluginCfg.TAG_SYSTEM && TagNameCfg.USE_HD_PLUGIN) {
			String uuid = pm.getPlayerUUIDByName(event.getPlayer().getName());
			PlayerTagBean playerTag = TagNameCfg.PLAYER_TAG.get(uuid);
			playerTag.delHologramsName();
		}
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
		if (StrUtil.messageFormat(LangCfg.TAG_INVENTORY_NAME + "§r§5§9§2§0§2§r").equals(event.getInventory().getName())) {
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
							if (itemName.startsWith(StrUtil.messageFormat(LangCfg.TAG_COLOR_ITEM_NAME + "§1§r "))) {
								playerTag.setNamecolor(lores.get(lores.size() - 1).substring(2));
							} else if (itemName.startsWith(StrUtil.messageFormat(LangCfg.TAG_PREFIX_ITEM_NAME + "§2§r "))) {
								playerTag.setPrefix(lores.get(lores.size() - 1).substring(2));
							} else if (itemName.startsWith(StrUtil.messageFormat(LangCfg.TAG_SUFFIX_ITEM_NAME + "§3§r "))) {
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
