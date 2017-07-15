package gg.frog.mc.permissionstime.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class PlayerPermissionShow {

    public static void show(Player p, List<PlayerDataBean> pdbList) {
        Inventory inventory = Bukkit.createInventory(null, (pdbList.size() % 9 == 0 ? pdbList.size() : (pdbList.size() / 9 + 1) * 9), StrUtil.messageFormat("&4你共有{0}种权限包", pdbList.size()));
        for (PlayerDataBean pdb : pdbList) {
            ItemStack item = PackagesCfg.PACKAGE_ITEMS.get(pdb.getPackageName());
            if (item != null) {
                inventory.addItem(item);
            }
        }
        p.openInventory(inventory);
    }
}
