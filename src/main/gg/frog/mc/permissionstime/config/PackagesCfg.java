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
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import gg.frog.mc.base.PluginMain;
import gg.frog.mc.base.config.LangCfg;
import gg.frog.mc.base.config.PluginCfg;
import gg.frog.mc.base.utils.StrUtil;
import gg.frog.mc.base.utils.config.PluginConfig;
import gg.frog.mc.base.utils.nms.ItemUtil;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;

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
	protected void init() {
	}

	@Override
	protected void loadToDo(CommandSender sender) {
		PACKAGES_VERSION = setGetDefault("version", "1.00");
		DEFAULT_GROUP = setGetDefault("defaultGroup", "Default");
		PACKAGES = getObjMap("packages", PermissionPackageBean.class);
		setObj("packages", PACKAGES);
		if (PluginCfg.IS_DEBUG) {
			System.out.println("packages vresion:" + PACKAGES_VERSION);
			System.out.println("defaultGroup:" + DEFAULT_GROUP);
			for (Entry<String, PermissionPackageBean> p : PACKAGES.entrySet()) {
				System.out.println(p.getKey() + ":" + p.getValue());
			}
		}
		PACKAGE_ITEMS.clear();
		for (Entry<String, PermissionPackageBean> e : PACKAGES.entrySet()) {
			ItemStack item = getPackageItem(e.getKey(), e.getValue());
			if (item != null) {
				PACKAGE_ITEMS.put(e.getKey(), item);
			} else {
				PluginMain.LOG.log(Level.SEVERE, "Packages of " + e.getKey() + " has problem.");
				if (sender != null) {
					sender.sendMessage("Packages of " + e.getKey() + " has problem.");
				}
			}
			allPermissions.addAll(e.getValue().getPermissions());
			allGroups.addAll(e.getValue().getGroups());
		}
		for (Player player : pm.getServer().getOnlinePlayers()) {
			try {
				String uuid = pm.getPlayerUUIDByName(player.getName());
				List<PlayerDataBean> pdbList = pm.getSqlManager().getTime(uuid);
				PermissionPackageBean.reloadPlayerPermissions(player, pdbList, pm, false);
			} catch (Exception e) {
				e.printStackTrace();
				sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_FAIL_SET_PERMISSION));
			}
		}
	}

	private ItemStack getPackageItem(String name, PermissionPackageBean ppb) {
		if (ppb != null) {
			Material type = null;
			int exid = 0;
			String skullOwner = null;
			if (ppb.getType() != null) {
				String[] args = ppb.getType().split(":");
				type = Material.getMaterial(args[0].toUpperCase(Locale.ENGLISH));
				if (args.length == 2) {
					try {
						exid = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						if (Material.SKULL_ITEM.equals(type)) {
							exid = 3;
							skullOwner = args[1];
						} else {
							e.printStackTrace();
						}
					}
				}
			} else if (ppb.getId() != null) {
				String[] args = ppb.getId().split(":");
				int id = Integer.parseInt(args[0]);
				type = Material.getMaterial(id);
				if (args.length == 2) {
					try {
						exid = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						if (Material.SKULL_ITEM.equals(type)) {
							exid = 3;
							skullOwner = args[1];
						} else {
							e.printStackTrace();
						}
					}
				}
			}
			if (type != null) {
				ItemStack item = new ItemStack(type, 1, (short) 0, (byte) exid);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(StrUtil.messageFormat(ppb.getDisplayName() + "§r(" + name + ")"));
				List<String> lores = new ArrayList<String>();
				for (String lore : ppb.getLores()) {
					lores.add(StrUtil.messageFormat(lore));
				}
				lores.add("");
				meta.setLore(lores);
				item.setItemMeta(meta);
				if (ppb.getGlowing() && !meta.hasEnchants()) {
					item = ItemUtil.addEnchantLight(item);
				}
				if (skullOwner != null) {
					item = ItemUtil.addSkullOwner(item, skullOwner);
				}
				return item;
			}
		}
		return null;
	}
}
