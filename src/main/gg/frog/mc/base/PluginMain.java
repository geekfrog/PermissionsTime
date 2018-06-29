package gg.frog.mc.base;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import gg.frog.mc.base.config.ConfigManager;
import gg.frog.mc.base.config.PluginCfg;
import gg.frog.mc.base.listener.BaseListener;
import gg.frog.mc.base.utils.FileUtil;
import gg.frog.mc.base.utils.StrUtil;
import gg.frog.mc.base.utils.UpdateCheck;
import gg.frog.mc.nametags.listener.TagsListener;
import gg.frog.mc.nametags.placeholder.TagPlaceholder;
import gg.frog.mc.permissionstime.command.PtCommand;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.listener.PtListener;
import net.milkbowl.vault.permission.Permission;

public class PluginMain extends JavaPlugin {

	public String PLUGIN_NAME;
	public String PLUGIN_VERSION;
	public String PLUGIN_NAME_LOWER_CASE;
	public static final String DEPEND_PLUGIN = "SQLibrary,Vault";
	public static final Logger LOG = Logger.getLogger("Minecraft");

	private ConfigManager cm = null;
	private PluginMain pm = null;
	private SqlManager sm = null;
	private Permission permission = null;
	public static boolean enabledHdPlugin;
	public static boolean enabledPlaceholder;
	private Map<String, String> PLAYER_UUID_MAP = new ConcurrentHashMap<>();
	private Map<String, OfflinePlayer> OFFLINE_PLAYER_MAP = new ConcurrentHashMap<>();

	public PluginMain() {
		PLUGIN_NAME = getDescription().getName();
		PLUGIN_VERSION = getDescription().getVersion();
		PLUGIN_NAME_LOWER_CASE = PLUGIN_NAME.toLowerCase(Locale.ENGLISH);
	}

	@Override
	public void onEnable() {
		pm = this;
		cm = new ConfigManager(pm);
		getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "==============================="));
		getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX));
		getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "    " + PLUGIN_NAME + " v" + PLUGIN_VERSION));
		getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "    author：GeekFrog QQ：324747460"));
		getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "    https://github.com/geekfrog/PermissionsTime/ "));
		getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX));
		getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "==============================="));
		if (!checkPluginDepends()) {
			getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Startup failure!"));
			getServer().getPluginManager().disablePlugin(pm);
		} else {
			cm.initConfig();
			registerListeners();
			registerCommands();
			getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§2Startup successful!"));
			getServer().getScheduler().runTask(pm, new Runnable() {
				public void run() {
					if (PluginCfg.IS_METRICS) {
						try {
							new Metrics(pm);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					getServer().getScheduler().runTaskAsynchronously(pm, new UpdateCheck(pm));
				}
			});
		}
	}

	/**
	 * 注册监听器 <br/>
	 * 这里可以注册多个
	 */
	private void registerListeners() {
		pm.getServer().getPluginManager().registerEvents(new BaseListener(pm), pm);
		pm.getServer().getPluginManager().registerEvents(new PtListener(pm), pm);
		pm.getServer().getPluginManager().registerEvents(new TagsListener(pm), pm);
	}

	/**
	 * 注册命令 <br/>
	 * 这里可以注册多个，一般注册一个就够用
	 */
	private void registerCommands() {
		PtCommand ptCmd = new PtCommand(pm);
		if (getDescription().getCommands().containsKey("permissionstime")) {
			getCommand("permissionstime").setExecutor(ptCmd);
		}
		if (getDescription().getCommands().containsKey("pt")) {
			getCommand("pt").setExecutor(ptCmd);
		}
	}

	public ConfigManager getConfigManager() {
		return cm;
	}

	public SqlManager getSqlManager() {
		return sm;
	}

	public Permission getPermission() {
		return permission;
	}

	public Map<String, OfflinePlayer> getOFFLINE_PLAYER_MAP() {
		return OFFLINE_PLAYER_MAP;
	}

	private boolean checkPluginDepends() {
		boolean needDepend = false;
		for (String name : DEPEND_PLUGIN.split(",")) {
			if (!getServer().getPluginManager().isPluginEnabled(name)) {
				getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Need depend plugins : " + name + "."));
				needDepend = true;
			}
		}
		if (!needDepend && !setupPermissions()) {
			getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Cann''t hook vault permission."));
			needDepend = true;
		}
		if (!needDepend && !setupDatabase()) {
			getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Cann''t setup database."));
			needDepend = true;
		}
		enabledPlaceholder = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
		if (!enabledPlaceholder) {
			getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§ePlaceholder is not installed or not enabled."));
			getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§eSome func will be disabled."));
		} else {
			boolean placeholdersHook = false;
			try {
				placeholdersHook = new TagPlaceholder(pm).register();
			} catch (Exception e) {

			}
			if (!placeholdersHook) {
				getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Cann''t hook placeholders"));
				getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4The placeholders '%permissionstime_displayname%' Cann''t use."));
			}
		}
		enabledHdPlugin = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");
		if (!enabledHdPlugin) {
			getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§eHolographicDisplays is not installed or not enabled."));
			getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§eSome func will be disabled."));
		}

		if (needDepend) {
			return false;
		}
		return true;
	}

	@Override
	public void onDisable() {
		getServer().getServicesManager().unregisterAll(pm);
		Bukkit.getScheduler().cancelTasks(pm);
		try {
			sm.getDb().close();
		} catch (Exception e) {
		}
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	private boolean setupDatabase() {
		sm = new SqlManager(pm);
		return sm.updateDatabase();
	}

	public OfflinePlayer getOfflinePlayer(String name) {
		if (name != null) {
			OfflinePlayer p = OFFLINE_PLAYER_MAP.get(name);
			if (p != null)
				return p;
			for (OfflinePlayer p2 : getServer().getOfflinePlayers()) {
				if (p2 == null)
					continue;
				if (p2.getName().equalsIgnoreCase(name)) {
					OFFLINE_PLAYER_MAP.put(name, p2);
					return p2;
				}
			}
		}
		return null;
	}

	public String getPlayerUUIDByName(Player p) {
		String uuid = getPlayerUUIDByName(p.getName());
		if (uuid == null) {
			return p.getUniqueId().toString();
		}
		return uuid;
	}

	public String getPlayerUUIDByName(String name) {
		if (name != null) {
			String uuid = PLAYER_UUID_MAP.get(name);
			if (uuid != null) {
				return uuid;
			} else {
				for (OfflinePlayer p : getServer().getOfflinePlayers()) {
					if (p == null)
						continue;
					if (p.getName().equalsIgnoreCase(name)) {
						uuid = p.getUniqueId().toString();
						PLAYER_UUID_MAP.put(name, uuid);
						return uuid;
					}
				}
			}
		}
		return null;
	}

	public void writeFailLog(String content, Object... args) {
		FileUtil.writeOnFile(getDataFolder() + "/failure.log", "[" + StrUtil.nowTimeString() + "] " + MessageFormat.format(content, args));
	}
}
