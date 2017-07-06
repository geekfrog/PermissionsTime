package gg.frog.mc.permissionstime.config;

import java.util.LinkedHashMap;
import java.util.Map;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.utils.PluginConfig;

/**
 * 配置文件管理
 * 
 * @author QiaoPengyu
 *
 */
public class ConfigManager {

    private PluginMain pm = PluginMain.getInstance();
    private Map<String, PluginConfig> cfgMap = new LinkedHashMap<>();

    public ConfigManager(PluginMain pm) {

        // 添加到配置列表
        this.cfgMap.put("plugin", new PluginCfg());
        this.cfgMap.put("lang", new LangCfg());
    }

    public void reloadConfig() {
        for (PluginConfig cfg : cfgMap.values()) {
            cfg.reloadConfig();
        }
    }

    public Map<String, PluginConfig> getCfgMap() {
        return cfgMap;
    }

}
