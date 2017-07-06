package gg.frog.mc.permissionstime.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Charsets;

import gg.frog.mc.permissionstime.PluginMain;

/**
 * 配置操作
 * 
 * @author QiaoPengyu
 *
 */
public abstract class PluginConfig {

    private PluginMain pm = PluginMain.getInstance();
    private FileConfiguration config = null;
    private File folder = null;
    private String fileName = null;
    private File configFile = null;


    protected PluginConfig() {
        initConfig(pm.getDataFolder(), "config.yml");
    }

    protected PluginConfig(String fileName) {
        initConfig(pm.getDataFolder(), fileName);
    }

    protected PluginConfig(File folder, String fileName) {
        initConfig(folder, fileName);
    }

    /**
     * 初始化
     * 
     * @param folder
     * @param fileName
     */
    private void initConfig(File folder, String fileName) {
        this.folder = folder;
        this.fileName = fileName;
        configFile = new File(folder, fileName);
        if (!configFile.exists()) {
            getConfig(folder, fileName).options().copyDefaults(true);
            init();
        } else {
            reloadConfig();
        }
    }
    
    /**
     * 首次生成文件调用
     */
    protected abstract void init();
    
    /**
     * 加载配置后调用
     */
    protected abstract void loadToDo();

    /**
     * 获取配置(首次)
     * 
     * @return
     */
    private FileConfiguration getConfig(File folder, String fileName) {
        if (config == null) {
            reloadConfig(folder, fileName);
        }
        return config;
    }

    /**
     * 获取配置
     * 
     * @return
     */
    protected FileConfiguration getConfig() {
        return config;
    }

    /**
     * 保存配置
     */
    public void saveConfig() {
        try {
            getConfig().save(configFile);
            reloadConfig();
        } catch (IOException ex) {
            PluginMain.LOG.log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    /**
     * 配置重载
     */
    public void reloadConfig() {
        reloadConfig(folder, fileName);
    }

    /**
     * 配置重载
     */
    private void reloadConfig(File folder, String fileName) {
        config = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = PluginMain.getInstance().getResource(fileName);
        if (defConfigStream == null) {
            return;
        }
        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
        loadToDo();
    }
}
