package gg.frog.mc.permissionstime.config;

import gg.frog.mc.permissionstime.utils.config.PluginConfig;

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
    }

    @Override
    protected void loadToDo() {
        NO_PERMISSION = setGetDefault("nopermission","&4你没有权限这么做");
        CONFIG_RELOADED = setGetDefault("configReloaded","&a配置重载完成");
    }
    
}
