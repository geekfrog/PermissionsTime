package gg.frog.mc.permissionstime.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class PlayerPermissionShow {

	public static void show(Player p, List<PlayerDataBean> pdbList) {
		Inventory inventory = null;
		int size = 0;
		if (pdbList.size() > 0) {
			inventory = Bukkit.createInventory(null, (pdbList.size() % 9 == 0 ? pdbList.size() : (pdbList.size() / 9 + 1) * 9), StrUtil.messageFormat(LangCfg.TAG_INVENTORY_NAME + "&r&5&9&2&0&r"));
			for (PlayerDataBean pdb : pdbList) {
				PermissionPackageBean ppb = PackagesCfg.PACKAGES.get(pdb.getPackageName());
				if (ppb != null && pdb.getGlobal() == ppb.getGlobal()) {
					ItemStack item = PackagesCfg.PACKAGE_ITEMS.get(pdb.getPackageName());
					ItemStack tItem = item.clone();
					ItemMeta meta = tItem.getItemMeta();
					List<String> lores = meta.getLore();
					lores.add(StrUtil.messageFormat(LangCfg.EXPIRATION_TIME, StrUtil.timestampToString(pdb.getExpire())));
					lores.add(StrUtil.getLeftTime(pdb.getExpire()));
					meta.setLore(lores);
					tItem.setItemMeta(meta);
					inventory.addItem(tItem);
					size++;
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
