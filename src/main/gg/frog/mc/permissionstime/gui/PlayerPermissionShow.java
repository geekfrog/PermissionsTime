package gg.frog.mc.permissionstime.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class PlayerPermissionShow {

    public static void show(Player p, List<PlayerDataBean> pdbList) {
        Inventory inventory = Bukkit.createInventory(null, (pdbList.size() % 9 == 0 ? pdbList.size() : (pdbList.size() / 9 + 1) * 9), StrUtil.messageFormat(LangCfg.INVENTORY_NAME + "&r&5&9&2&0&r"));
        for (PlayerDataBean pdb : pdbList) {
            ItemStack item = PackagesCfg.PACKAGE_ITEMS.get(pdb.getPackageName());
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                List<String> lores = meta.getLore();
                lores.add("");
                lores.add(StrUtil.messageFormat(LangCfg.EXPIRATION_DATE, StrUtil.timestampToString(pdb.getExpire())));
                meta.setLore(lores);
                item.setItemMeta(meta);
                inventory.addItem(item);
            }
        }
        p.openInventory(inventory);
    }
}
