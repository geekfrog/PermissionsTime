package gg.frog.mc.permissionstime;

import java.util.Locale;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import gg.frog.mc.permissionstime.command.TheCommand;
import gg.frog.mc.permissionstime.config.ConfigManager;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.listener.TheListener;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class PluginMain extends JavaPlugin {

    public static final String PLUGIN_NAME = "PermissionsTime";
    public static final String PLUGIN_VERSION = "0.0.1";
    public static final String PLUGIN_NAME_LOWER_CASE = PLUGIN_NAME.toLowerCase(Locale.ENGLISH);
    public static final Logger LOG = Logger.getLogger(PluginMain.PLUGIN_NAME);

    private static PluginMain pm = null;
    private ConfigManager cm = null;
    
    @Override
    public void onEnable() {
        PluginMain.pm = this;
        registerListeners();
        registerCommands();
        cm = new ConfigManager();
        pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "==============================="));
        pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "    " + PluginMain.PLUGIN_NAME + " v" + PluginMain.PLUGIN_VERSION));
        pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "    作者：宅宅蛙 QQ：324747460"));
        pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "    https://github.com/geekfrog/PermissionsTime/ "));
        pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX));
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
}
