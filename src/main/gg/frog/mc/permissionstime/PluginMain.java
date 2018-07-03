package gg.frog.mc.permissionstime;

import java.text.MessageFormat;
import java.util.Locale;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import gg.frog.mc.base.utils.FileUtil;
import gg.frog.mc.base.utils.StrUtil;
import gg.frog.mc.base.utils.UpdateCheck;
import gg.frog.mc.base.utils.PluginBase;
import gg.frog.mc.permissionstime.command.PtCommand;
import gg.frog.mc.permissionstime.config.ConfigManager;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.listener.PtListener;
import net.milkbowl.vault.permission.Permission;

public class PluginMain extends PluginBase {

	public static final String DEPEND_PLUGIN = "Vault";

	private ConfigManager cm = null;
	private PluginMain pm = null;
	private SqlManager sm = null;
	private Permission permission = null;

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
					getServer().getScheduler().runTaskAsynchronously(pm, new UpdateCheck(pm, "https://raw.githubusercontent.com/geekfrog/PermissionsTime/master/src/resources/plugin.yml"));
				}
			});
		}
	}

	/**
	 * 注册监听器 <br/>
	 * 这里可以注册多个
	 */
	private void registerListeners() {
		pm.getServer().getPluginManager().registerEvents(new PtListener(pm), pm);
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

	public void writeFailLog(String content, Object... args) {
		FileUtil.writeOnFile(getDataFolder() + "/failure.log", "[" + StrUtil.nowTimeString() + "] " + MessageFormat.format(content, args));
	}
}
