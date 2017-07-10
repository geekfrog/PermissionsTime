package gg.frog.mc.permissionstime.config;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.utils.config.PluginConfig;

/**
 * 插件默认配置
 * 
 * @author QiaoPengyu
 *
 */
public class PluginCfg extends PluginConfig {
    
    public static String PLUGIN_PREFIX = null;
    public static Boolean IS_DEBUG = null;
    public static String LANG = null;
    public static String SQL_HOSTNAME;
    public static int SQL_PORT;
    public static String SQL_DATABASE;
    public static String SQL_USERNAME;
    public static String SQL_PASSWORD;
    
    public PluginCfg() {
        super();
    }

    @Override
    protected void init() {
        getConfig().set("lang","zh-cn");
        getConfig().set("debug", false);
        saveConfig();
    }

    @Override
    protected void loadToDo() {
        PLUGIN_PREFIX = getConfig().getString("pluginPrefix","&b["+PluginMain.PLUGIN_NAME+"]&r");
        IS_DEBUG = getConfig().getBoolean("debug", false);
        LANG = getConfig().getString("lang","zh-cn");
    }
    
}
