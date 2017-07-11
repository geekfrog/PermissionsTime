package gg.frog.mc.permissionstime.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.utils.FileUtil;
import gg.frog.mc.permissionstime.utils.FileUtil.FindFilesDo;
import gg.frog.mc.permissionstime.utils.config.PluginConfig;

/**
 * 配置文件管理
 * 
 * @author QiaoPengyu
 *
 */
public class ConfigManager {

    private PluginMain pm;
    private Map<String, PluginConfig> cfgMap = new LinkedHashMap<>();

    public ConfigManager(PluginMain pm) {
        this.pm = pm;
        File langFolder = new File(pm.getDataFolder(), "lang/");
        if (!langFolder.exists()) {
            copyLangFilesFromJar();
        }
        // 添加到配置列表
        cfgMap.put("plugin", new PluginCfg(pm));
        cfgMap.put("lang", new LangCfg("lang/" + PluginCfg.LANG + ".yml", pm));
        cfgMap.put("packages", new PackagesCfg("packages.yml", pm));
    }

    public void reloadConfig() {
        for (Entry<String, PluginConfig> entry : cfgMap.entrySet()) {
            if ("lang".equals(entry.getKey())) {
                entry.setValue(new LangCfg("lang/" + PluginCfg.LANG + ".yml", pm));
            }
            entry.getValue().reloadConfig();
        }
    }

    public Map<String, PluginConfig> getCfgMap() {
        return cfgMap;
    }

    private void copyLangFilesFromJar() {
        FileUtil.findFilesFromJar(new FindFilesDo() {

            @Override
            public void process(String fileName, InputStream is) {
                File f = new File(pm.getDataFolder(), fileName);
                File parentFolder = f.getParentFile();
                if (!parentFolder.exists()) {
                    parentFolder.mkdirs();
                }
                try {
                    OutputStream os = new FileOutputStream(f);
                    int bytesRead = 0;
                    byte[] buffer = new byte[8192];
                    while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    os.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean isProcess(String fileName) {
                if (fileName.matches("lang/.+\\.yml")) {
                    return true;
                }
                return false;
            }
        }, this.getClass());
    }
}
