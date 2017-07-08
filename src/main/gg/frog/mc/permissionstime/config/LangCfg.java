package gg.frog.mc.permissionstime.config;

import gg.frog.mc.permissionstime.utils.PluginConfig;

/**
 * 语言支持
 * 
 * @author QiaoPengyu
 *
 */
public class LangCfg extends PluginConfig {

    public static String NO_PERMISSION = null;
    public static String CONFIG_RELOADED = null;

    public LangCfg(String fileName) {
        super(fileName);
    }

    @Override
    protected void init() {
        saveConfig();
    }

    @Override
    protected void loadToDo() {
        NO_PERMISSION = getConfig().getString("nopermission","&4你没有权限这么做");
        CONFIG_RELOADED = getConfig().getString("configReloaded","&a配置重载完成");
    }
    
}
