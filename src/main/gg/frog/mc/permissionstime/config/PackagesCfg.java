package gg.frog.mc.permissionstime.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.utils.StrUtil;
import gg.frog.mc.permissionstime.utils.config.PluginConfig;

public class PackagesCfg extends PluginConfig {

    public static String PACKAGES_VERSION = null;
    public static String DEFAULT_GROUP = null;
    public static Map<String, PermissionPackageBean> PACKAGES = new ConcurrentHashMap<>();
    public static Map<String, ItemStack> PACKAGE_ITEMS = new ConcurrentHashMap<>();
    public static Set<String> allPermissions = Collections.synchronizedSet(new HashSet<String>());
    public static Set<String> allGroups = Collections.synchronizedSet(new HashSet<String>());

    public PackagesCfg(String fileName, PluginMain pm) {
        super(fileName, pm);
    }

    @Override
    protected void init() {}

    @Override
    protected void loadToDo() {
        PACKAGES_VERSION = setGetDefault("version", "?");
        DEFAULT_GROUP = setGetDefault("defaultGroup", "Default");
        PACKAGES = getObjMap("packages", PermissionPackageBean.class);
        saveObj("packages", PACKAGES);
        if (PluginCfg.IS_DEBUG) {
            System.out.println("packages vresion:" + PACKAGES_VERSION);
            System.out.println("defaultGroup:" + DEFAULT_GROUP);
            for (Entry<String, PermissionPackageBean> p : PACKAGES.entrySet()) {
                System.out.println(p.getKey() + ":" + p.getValue());
            }
        }
        PACKAGE_ITEMS.clear();
        for (Entry<String, PermissionPackageBean> e : PACKAGES.entrySet()) {
            PACKAGE_ITEMS.put(e.getKey(), getPackageItem(e.getKey(), e.getValue()));
            allPermissions.addAll(e.getValue().getPermissions());
            allGroups.addAll(e.getValue().getGroups());
        }
    }

    private ItemStack getPackageItem(String name, PermissionPackageBean ppb) {
        if (ppb != null) {
            Material type = null;
            int exid = 0;
            if (ppb.getType() != null) {
                String[] args = ppb.getType().split(":");
                try {
                    switch (args.length) {
                        case 2:
                            exid = Integer.parseInt(args[1]);
                        case 1:
                            type = Material.getMaterial(args[0].toUpperCase(Locale.ENGLISH));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else if (ppb.getId() != null) {
                String[] args = ppb.getId().split(":");
                try {
                    switch (args.length) {
                        case 2:
                            exid = Integer.parseInt(args[1]);
                        case 1:
                            int id = Integer.parseInt(args[0]);
                            type = Material.getMaterial(id);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if (type != null) {
                ItemStack items = new ItemStack(type, 1, (short) 0, (byte) exid);
                ItemMeta meta = items.getItemMeta();
                meta.setDisplayName(StrUtil.messageFormat(ppb.getDisplayName() + "&r(" + name + ")"));
                if (ppb.getLores().size() > 0) {
                    List<String> lores = new ArrayList<String>();
                    for (String lore : ppb.getLores()) {
                        lores.add(StrUtil.messageFormat(lore));
                    }
                    meta.setLore(lores);
                }
                items.setItemMeta(meta);
                return items;
            }
        }
        return null;
    }
}
