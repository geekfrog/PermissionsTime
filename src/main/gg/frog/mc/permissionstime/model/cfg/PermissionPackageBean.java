package gg.frog.mc.permissionstime.model.cfg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
import gg.frog.mc.permissionstime.utils.StrUtil;
import gg.frog.mc.permissionstime.utils.config.IConfigBean;
import net.milkbowl.vault.permission.Permission;

/**
 * 权限包实体类
 * 
 * @author QiaoPengyu
 *
 */
public class PermissionPackageBean implements IConfigBean {

	private String displayName = null;
	private String id;
	private String type;
	private Boolean glowing;
	private List<String> lores = new ArrayList<>();
	private Boolean global;
	private List<String> permissions = new ArrayList<>();
	private List<String> groups = new ArrayList<>();
	private List<String> expireCommands = new ArrayList<>();
	private static Map<String, BukkitTask> taskMap = new ConcurrentHashMap<>();

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getGlowing() {
		return glowing;
	}

	public void setGlowing(Boolean glowing) {
		this.glowing = glowing;
	}

	public List<String> getLores() {
		return lores;
	}

	public void setLores(List<String> lores) {
		this.lores = lores;
	}

	public Boolean getGlobal() {
		return global;
	}

	public void setGlobal(Boolean global) {
		this.global = global;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public List<String> getExpireCommands() {
		return expireCommands;
	}

	public void setExpireCommands(List<String> expireCommands) {
		this.expireCommands = expireCommands;
	}

	@Override
	public YamlConfiguration toConfig() {
		YamlConfiguration config = new YamlConfiguration();
		config.set("displayName", displayName);
		config.set("id", id);
		config.set("type", type);
		config.set("glowing", glowing);
		config.set("lores", lores);
		config.set("global", global);
		config.set("permissions", permissions);
		config.set("groups", groups);
		config.set("expireCommands", expireCommands);
		return config;
	}

	@Override
	public void toConfigBean(MemorySection config) {
		displayName = config.getString("displayName");
		if (displayName == null) {
			displayName = "No Name";
		}
		id = config.getString("id");
		type = config.getString("type");
		if (id == null && type == null) {
			type = "NETHER_STAR";
		}
		glowing = config.getBoolean("glowing");
		lores = config.getStringList("lores");
		global = config.getBoolean("global");
		permissions = config.getStringList("permissions");
		groups = config.getStringList("groups");
		expireCommands = config.getStringList("expireCommands");
	}

	@Override
	public String toString() {
		return "PermissionPackageBean [displayName=" + displayName + ", id=" + id + ", type=" + type + ", glowing=" + glowing + ", lores=" + lores + ", global=" + global + ", permissions=" + permissions + ", groups=" + groups + ", expireCommands=" + expireCommands + "]";
	}

	private void givePlayer(OfflinePlayer player, Server server, Permission permission) {
		List<World> worlds = server.getWorlds();
		for (String pem : permissions) {
			String[] args = pem.split(":");
			pem = args[0];
			if (args.length > 1) {
				for (int i = 1; i < args.length; i++) {
					String worldName = args[i];
					permission.playerAdd(worldName, player, pem);
				}
			} else {
				for (World world : worlds) {
					String worldName = world.getName();
					permission.playerAdd(worldName, player, pem);
				}
			}
		}
		for (String groupName : groups) {
			String[] args = groupName.split(":");
			groupName = args[0];
			if (args.length > 1) {
				for (int i = 1; i < args.length; i++) {
					String worldName = args[i];
					permission.playerAddGroup(worldName, player, groupName);
				}
			} else {
				for (World world : worlds) {
					String worldName = world.getName();
					permission.playerAddGroup(worldName, player, groupName);
				}
			}
		}
	}

	private void clearPlayer(OfflinePlayer player, Server server, Permission permission) {
		List<World> worlds = server.getWorlds();
		for (String pem : permissions) {
			String[] args = pem.split(":");
			pem = args[0];
			if (args.length > 1) {
				for (int i = 1; i < args.length; i++) {
					String worldName = args[i];
					permission.playerRemove(worldName, player, pem);
				}
			} else {
				for (World world : worlds) {
					String worldName = world.getName();
					permission.playerRemove(worldName, player, pem);
				}
			}
		}
		for (String groupName : groups) {
			String[] args = groupName.split(":");
			groupName = args[0];
			if (args.length > 1) {
				for (int i = 1; i < args.length; i++) {
					String worldName = args[i];
					permission.playerRemoveGroup(worldName, player, groupName);
				}
			} else {
				for (World world : worlds) {
					String worldName = world.getName();
					permission.playerRemoveGroup(worldName, player, groupName);
				}
			}
		}
	}

	public static void reloadPlayerPermissions(OfflinePlayer player, List<PlayerDataBean> pdbList, PluginMain plugin) {
		reloadPlayerPermissions(player, pdbList, plugin, true);
	}

	public static void reloadPlayerPermissions(OfflinePlayer player, List<PlayerDataBean> pdbList, PluginMain plugin, boolean async) {
		long delay = -1;
		long now = new Date().getTime();
		PermissionPackageBean addPpb = new PermissionPackageBean();
		addPpb.getGroups().add(PackagesCfg.DEFAULT_GROUP);
		PermissionPackageBean subPpb = new PermissionPackageBean();
		subPpb.getPermissions().addAll(PackagesCfg.allPermissions);
		subPpb.getGroups().addAll(PackagesCfg.allGroups);
		for (PlayerDataBean pdb : pdbList) {
			long leftTime = pdb.getExpire() - now;
			if (leftTime > 0) {
				if (delay == -1) {
					delay = leftTime;
				} else if (delay > leftTime) {
					delay = leftTime;
				}
			}
			PermissionPackageBean p = PackagesCfg.PACKAGES.get(pdb.getPackageName());
			if (p != null && pdb.getGlobal() == p.getGlobal()) {
				addPpb.getPermissions().addAll(p.getPermissions());
				subPpb.getPermissions().removeAll(p.getPermissions());
				addPpb.getGroups().addAll(p.getGroups());
				subPpb.getGroups().removeAll(p.getGroups());
			}
		}
		if (async) {
			plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
				@Override
				public void run() {
					try {
						subPpb.clearPlayer(player, plugin.getServer(), plugin.getPermission());
						addPpb.givePlayer(player, plugin.getServer(), plugin.getPermission());
					} catch (Exception e) {
						e.printStackTrace();
						player.getPlayer().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_FAIL_SET_PERMISSION));
					}
				}
			});
		} else {
			subPpb.clearPlayer(player, plugin.getServer(), plugin.getPermission());
			addPpb.givePlayer(player, plugin.getServer(), plugin.getPermission());
		}
		checkExpire(player, plugin);
		BukkitTask task = taskMap.get(player.getUniqueId().toString());
		if (pdbList.size() > 0) {
			delay = (delay / 1000 + 1) * 20;// 1秒=20ticks
			task = plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
				@Override
				public void run() {
					List<PlayerDataBean> tpdbList = plugin.getSqlManager().getTime(player.getUniqueId().toString());
					reloadPlayerPermissions(player, tpdbList, plugin);
				}
			}, delay);
			taskMap.put(player.getUniqueId().toString(), task);
		}
	}

	public static void delPlayerAllPermissions(OfflinePlayer player, PluginMain plugin) throws Exception {
		PermissionPackageBean subPpb = new PermissionPackageBean();
		subPpb.getPermissions().addAll(PackagesCfg.allPermissions);
		subPpb.getGroups().addAll(PackagesCfg.allGroups);
		plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
			@Override
			public void run() {
				subPpb.clearPlayer(player, plugin.getServer(), plugin.getPermission());
			}
		});
		BukkitTask task = taskMap.get(player.getUniqueId().toString());
		if (task != null) {
			plugin.getServer().getScheduler().cancelTask(task.getTaskId());
		}
	}

	public static void checkExpire(OfflinePlayer player, PluginMain plugin) {
		List<PlayerDataBean> playerDataList = plugin.getSqlManager().getAllTime(player.getUniqueId().toString());
		long now = new Date().getTime();
		for (PlayerDataBean playerData : playerDataList) {
			if (playerData.getExpire() < now) {
				PermissionPackageBean packageBean = PackagesCfg.PACKAGES.get(playerData.getPackageName());
				if ((packageBean == null && !playerData.getGlobal()) || (packageBean != null && playerData.getGlobal() == packageBean.getGlobal())) {
					plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
						@Override
						public void run() {
							Player p = player.getPlayer();
							if (p != null) {
								p.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_IS_EXPIRATION_DATE, packageBean != null ? packageBean.getDisplayName() : LangCfg.MSG_UNKNOWN_PACKAGE, playerData.getPackageName()));
								if(packageBean !=null) {
									for (String commands : packageBean.getExpireCommands()) {
										try {
											commands = StrUtil.messageFormat(player.getPlayer(), commands);
											plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), commands);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
					});
					plugin.getSqlManager().delById(playerData.getId());
				}
			}
		}
	}
}
