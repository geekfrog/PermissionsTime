package gg.frog.mc.permissionstime.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.config.TagNameCfg;
import gg.frog.mc.permissionstime.config.TagNameCfg.TagType;
import gg.frog.mc.permissionstime.model.cfg.PlayerTagBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class PlayerTagShow {

	public static void show(Player p, TagType tagType, List<ItemStack> itemList) {
		Inventory inventory = null;
		int size = 0;
		if (itemList.size() > 0) {
			inventory = Bukkit.createInventory(null,
					(itemList.size() % 9 == 0 ? itemList.size() : (itemList.size() / 9 + 1) * 9),
					StrUtil.messageFormat(LangCfg.TAG_INVENTORY_NAME + "&r&5&9&2&0&r"));
			String uuid = p.getUniqueId().toString();
			PlayerTagBean playerTag = null;
			if (TagNameCfg.PLAYER_TAG.containsKey(uuid)) {
				playerTag = TagNameCfg.PLAYER_TAG.get(uuid);
				playerTag = playerTag.clone();
				if(playerTag != null) {
					for (ItemStack item : itemList) {
						ItemStack tItem = item.clone();
						ItemMeta meta = tItem.getItemMeta();
						List<String> lores = meta.getLore();
						String tag = lores.get(5);
						if(tagType == TagType.NAMECOLOR_TYPE) {
							playerTag.setNamecolor(tag);
			        	}else if(tagType == TagType.PREFIX_TYPE) {
			        		playerTag.setPrefix(tag);
			        	}else if(tagType == TagType.SUFFIX_TYPE) {
			        		playerTag.setSuffix(tag);
			        	}
						lores.set(1,"效果展示: "+playerTag.getDisplayNameStr(p));
						meta.setLore(lores);
						tItem.setItemMeta(meta);
						inventory.addItem(tItem);
						size++;
					}
				}
			}
		}
		if (inventory != null && size > 0) {
			p.openInventory(inventory);
		} else {
			p.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NO_DATA));
		}
	}
}
