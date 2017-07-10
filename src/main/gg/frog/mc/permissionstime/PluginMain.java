package gg.frog.mc.permissionstime;

import java.util.Locale;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import gg.frog.mc.permissionstime.command.TheCommand;
import gg.frog.mc.permissionstime.config.ConfigManager;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.listener.TheListener;
import gg.frog.mc.permissionstime.utils.StrUtil;
import lib.PatPeter.SQLibrary.SQLite;
import net.milkbowl.vault.permission.Permission;

public class PluginMain extends JavaPlugin {

    public static String PLUGIN_NAME;
    public static String PLUGIN_VERSION;
    public static String PLUGIN_NAME_LOWER_CASE;
    public static final String DEPEND_PLUGIN = "SQLibrary,Vault";
    public static Logger LOG = Logger.getLogger("Minecraft");

    public static PluginMain pm = null;
    public static ConfigManager cm = null;
    public static Permission permission = null;

    public PluginMain() {
        PLUGIN_NAME = getDescription().getName();
        PLUGIN_VERSION = getDescription().getVersion();
        PLUGIN_NAME_LOWER_CASE = PLUGIN_NAME.toLowerCase(Locale.ENGLISH);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        pm = this;
        cm = new ConfigManager();
        getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "==============================="));
        getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX));
        getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "    " + PluginMain.PLUGIN_NAME + " v" + PluginMain.PLUGIN_VERSION));
        getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "    author：GeekFrog QQ：324747460"));
        getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "    https://github.com/geekfrog/PermissionsTime/ "));
        getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX));
        getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "==============================="));
        if (PluginCfg.IS_METRICS) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            public void run() {
                if (!checkPluginDepends()) {
                    getServer().getPluginManager().disablePlugin(pm);
                } else {
                    registerListeners();
                    registerCommands();
                }
            }
        }, 0L, 432000L);
    }

    public static PluginMain getInstance() {
        return pm;
    }

    /**
     * 注册监听器 <br/>
     * 这里可以注册多个
     */
    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new TheListener(), this);
    }

    /**
     * 注册命令 <br/>
     * 这里可以注册多个，一般注册一个就够用
     */
    private void registerCommands() {
        this.getCommand(PLUGIN_NAME_LOWER_CASE).setExecutor(new TheCommand());
    }

    public ConfigManager getConfigManager() {
        return cm;
    }

    private boolean checkPluginDepends() {
        boolean needDepend = false;
        for (String name : DEPEND_PLUGIN.split(",")) {
            if (getServer().getPluginManager().getPlugin(name) == null) {
                getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "Need depend plugins : " + name));
                needDepend = true;
            }
        }
        if (!setupPermissions()) {
            getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "Cann't hook vault permission"));
            needDepend = true;
        }
        if (!setupDatabase()) {
            getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "Cann't setup database"));
            needDepend = true;
        }
        if (needDepend) {
            return false;
        }
        return true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getServer().getServicesManager().unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupDatabase() {
        try {
            SQLite sql = new SQLite(Logger.getLogger("Minecraft"), "[" + PLUGIN_NAME + "] ", this.getDataFolder().getAbsolutePath(), PLUGIN_NAME, ".sqlite");
            if(!sql.isOpen()){
                sql.open();
            }
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
