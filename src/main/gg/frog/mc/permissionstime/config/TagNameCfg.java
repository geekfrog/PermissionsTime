package gg.frog.mc.permissionstime.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.model.cfg.PlayerTagBean;
import gg.frog.mc.permissionstime.model.cfg.TagPackageBean;
import gg.frog.mc.permissionstime.utils.StrUtil;
import gg.frog.mc.permissionstime.utils.config.PluginConfig;
import gg.frog.mc.permissionstime.utils.nms.ItemUtil;

public class TagNameCfg extends PluginConfig {

	public static String DEFAULT_NAMECOLOR = null;
	public static String DEFAULT_PREFIX = null;
	public static String DEFAULT_SUFFIX = null;
	public static Map<String, TagPackageBean> PACKAGES = new ConcurrentHashMap<>();

	public static Map<String, List<ItemStack>> NAMECOLOR_ITEMS = new ConcurrentHashMap<>();
	public static Map<String, List<ItemStack>> PREFIX_ITEMS = new ConcurrentHashMap<>();
	public static Map<String, List<ItemStack>> SUFFIX_ITEMS = new ConcurrentHashMap<>();

	public static Map<String, List<String>> NAMECOLOR_PERMISSIONS = new ConcurrentHashMap<>();
	public static Map<String, List<String>> PREFIX_PERMISSIONS = new ConcurrentHashMap<>();
	public static Map<String, List<String>> SUFFIX_PERMISSIONS = new ConcurrentHashMap<>();

	public static Map<String, PlayerTagBean> PLAYER_TAG = new ConcurrentHashMap<>();

	public enum TagType {
		NAMECOLOR_TYPE, PREFIX_TYPE, SUFFIX_TYPE
	}

	public TagNameCfg(String fileName, PluginMain pm) {
		super(fileName, pm);
	}

	@Override
	protected void init() {
	}

	@Override
	protected void loadToDo() {
		DEFAULT_NAMECOLOR = setGetDefault("defaultNamecolor", "");
		DEFAULT_PREFIX = setGetDefault("defaultPrefix", "");
		DEFAULT_SUFFIX = setGetDefault("defaultSuffix", "");
		PACKAGES = getObjMap("packages", TagPackageBean.class);
		saveObj("packages", PACKAGES);
		if (PluginCfg.IS_DEBUG) {
			System.out.println("defaultNamecolor:" + DEFAULT_NAMECOLOR);
			System.out.println("defaultPrefix:" + DEFAULT_PREFIX);
			System.out.println("defaultSuffix:" + DEFAULT_SUFFIX);
			for (Entry<String, TagPackageBean> p : PACKAGES.entrySet()) {
				System.out.println(p.getKey() + ":" + p.getValue());
			}
		}
		NAMECOLOR_ITEMS.clear();
		PREFIX_ITEMS.clear();
		SUFFIX_ITEMS.clear();

		NAMECOLOR_PERMISSIONS.clear();
		PREFIX_PERMISSIONS.clear();
		SUFFIX_PERMISSIONS.clear();

		for (Entry<String, TagPackageBean> e : PACKAGES.entrySet()) {
			List<ItemStack> items = getTagItem(TagType.NAMECOLOR_TYPE, e.getValue());
			if (NAMECOLOR_ITEMS.containsKey(e.getValue().getPermissions())) {
				NAMECOLOR_ITEMS.get(e.getValue().getPermissions()).addAll(items);
			} else {
				NAMECOLOR_ITEMS.put(e.getValue().getPermissions(), items);
			}
			items = getTagItem(TagType.PREFIX_TYPE, e.getValue());
			if (PREFIX_ITEMS.containsKey(e.getValue().getPermissions())) {
				PREFIX_ITEMS.get(e.getValue().getPermissions()).addAll(items);
			} else {
				PREFIX_ITEMS.put(e.getValue().getPermissions(), items);
			}
			items = getTagItem(TagType.SUFFIX_TYPE, e.getValue());
			if (SUFFIX_ITEMS.containsKey(e.getValue().getPermissions())) {
				SUFFIX_ITEMS.get(e.getValue().getPermissions()).addAll(items);
			} else {
				SUFFIX_ITEMS.put(e.getValue().getPermissions(), items);
			}
		}
	}

	private List<ItemStack> getTagItem(TagType tagType, TagPackageBean tpb) {
		List<ItemStack> items = new ArrayList<>();
		if (tpb != null) {
			List<String> tags = null;
			String itemDisplayName = "";
			Map<String, List<String>> tagPermissions = null;
			if (tagType == TagType.NAMECOLOR_TYPE) {
				tags = tpb.getNamecolor();
				itemDisplayName = "&6&l昵称效果 &r";
				tagPermissions = NAMECOLOR_PERMISSIONS;
			} else if (tagType == TagType.PREFIX_TYPE) {
				tags = tpb.getPrefix();
				itemDisplayName = "&6&l昵称前缀 &r";
				tagPermissions = PREFIX_PERMISSIONS;
			} else if (tagType == TagType.SUFFIX_TYPE) {
				tags = tpb.getSuffix();
				itemDisplayName = "&6&l昵称后缀 &r";
				tagPermissions = SUFFIX_PERMISSIONS;
			}
			if (tags != null) {
				for (String tag : tags) {
					String[] args = tag.split(":");
					tag = args[0];
					Material type = null;
					int exid = 0;
					String skullOwner = null;
					if (args.length > 1) {
						type = Material.getMaterial(args[1].toUpperCase(Locale.ENGLISH));
						if (type == null) {
							int id = Integer.parseInt(args[1]);
							type = Material.getMaterial(id);
						}
						if (args.length > 2) {
							try {
								exid = Integer.parseInt(args[2]);
							} catch (NumberFormatException e) {
								if (Material.SKULL_ITEM.equals(type)) {
									exid = 3;
									skullOwner = args[2];
								} else {
									e.printStackTrace();
								}
							}
						}
					} else {
						type = Material.getMaterial("NAME_TAG");
					}
					if (type != null) {
						ItemStack item = new ItemStack(type, 1, (short) 0, (byte) exid);
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName(StrUtil.messageFormat(itemDisplayName + tag));
						List<String> lores = new ArrayList<String>();
						lores.add("");
						lores.add("");// 效果展示
						lores.add("");
						lores.add(StrUtil.messageFormat(tpb.getDescription()));// 描述
						lores.add("");
						lores.add(tag);// 称号
						meta.setLore(lores);
						item.setItemMeta(meta);
						if (skullOwner != null) {
							item = ItemUtil.addSkullOwner(item, skullOwner);
						}
						items.add(item);
					}
					if (!tagPermissions.containsKey(tag)) {
						tagPermissions.put(tag, new ArrayList<>());
					}
					tagPermissions.get(tag).add(tpb.getPermissions());
				}
			}
		}
		return items;
	}
}