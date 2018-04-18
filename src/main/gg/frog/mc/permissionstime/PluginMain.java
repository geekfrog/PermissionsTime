package gg.frog.mc.permissionstime;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import gg.frog.mc.permissionstime.command.MainCommand;
import gg.frog.mc.permissionstime.config.ConfigManager;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.listener.MainListener;
import gg.frog.mc.permissionstime.utils.FileUtil;
import gg.frog.mc.permissionstime.utils.StrUtil;
import gg.frog.mc.permissionstime.utils.UpdateCheck;
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
        getServer().getScheduler().runTask(pm, new UpdateCheck(pm));
        getServer().getScheduler().runTask(pm, new Runnable() {
            public void run() {
                if (!checkPluginDepends()) {
                    getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "&4Startup failure!"));
                    getServer().getPluginManager().disablePlugin(pm);
                } else {
                    registerListeners();
                    registerCommands();
                    getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "&2Startup successful!"));
                }
                if (PluginCfg.IS_METRICS) {
                    try {
                        Metrics metrics = new Metrics(pm);
//                        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 注册监听器 <br/>
     * 这里可以注册多个
     */
    private void registerListeners() {
        pm.getServer().getPluginManager().registerEvents(new MainListener(pm), pm);
    }

    /**
     * 注册命令 <br/>
     * 这里可以注册多个，一般注册一个就够用
     */
    private void registerCommands() {
        MainCommand mcmd = new MainCommand(pm);
        if (getDescription().getCommands().containsKey(PLUGIN_NAME_LOWER_CASE)) {
            getCommand(PLUGIN_NAME_LOWER_CASE).setExecutor(mcmd);
        }
        if (getDescription().getCommands().containsKey("pt")) {
            getCommand("pt").setExecutor(mcmd);
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
            if (getServer().getPluginManager().getPlugin(name) == null) {
                getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "&4Need depend plugins : " + name));
                needDepend = true;
            }
        }
        if (!needDepend && !setupPermissions()) {
            getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "&4Cann''t hook vault permission"));
            needDepend = true;
        }
        if (!needDepend && !setupDatabase()) {
            getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "&4Cann''t setup database"));
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

    public UUID getPlayerUUIDByName(String name) {
        OfflinePlayer p = getOfflinePlayer(name);
        if (p == null) {
            return null;
        } else {
            return p.getUniqueId();
        }
    }

    public OfflinePlayer getOfflinePlayer(String name) {
        for (OfflinePlayer p : getServer().getOfflinePlayers()) {
        	if(p==null)
        		continue;
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public void writeFailLog(String content, Object... args) {
        FileUtil.writeOnFile(getDataFolder() + "/failure.log", "[" + StrUtil.nowTimeString() + "] " + MessageFormat.format(content, args));
    }
}
