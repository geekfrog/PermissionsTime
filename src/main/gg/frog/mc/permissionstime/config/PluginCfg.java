package gg.frog.mc.permissionstime.config;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.utils.PluginConfig;

/**
 * 插件默认配置
 * 
 * @author QiaoPengyu
 *
 */
public class PluginCfg extends PluginConfig {

    private static PluginMain pm = PluginMain.getInstance();
    
    public static String PLUGIN_PREFIX = null;
    public static Boolean IS_DEBUG = null;
    
    
    public PluginCfg() {
        super();
    }

    @Override
    protected void init() {
        getConfig().set("debug", false);
        saveConfig();
    }

    @Override
    protected void loadToDo() {
        PLUGIN_PREFIX = (String)getConfig().get("pluginPrefix","&b["+PluginMain.PLUGIN_NAME+"]&r");
        IS_DEBUG = getConfig().getBoolean("debug", false);
    }
    
}
