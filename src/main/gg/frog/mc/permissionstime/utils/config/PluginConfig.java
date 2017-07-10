package gg.frog.mc.permissionstime.utils.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.configuration.MemorySection;
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
            saveAndReloadConfig();
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
        } catch (IOException ex) {
            PluginMain.LOG.log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }
    
    public void saveAndReloadConfig() {
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
        saveConfig();
    }

    protected void saveObj(String path, Map<String, ? extends IConfigBean> o) {
        for (Entry<String, ? extends IConfigBean> configBean : o.entrySet()) {
            saveObj(path + "." + configBean.getKey(), configBean.getValue());
        }
    }

    protected void saveObj(String path, IConfigBean o) {
        getConfig().set(path, o.toConfig());
    }

    protected <T extends IConfigBean> Map<String, T> getObjMap(String path, Class<T> clazz) {
        Map<String, T> map = new HashMap<>();
        MemorySection configMap = (MemorySection) getConfig().get(path);
        if (configMap != null) {
            for (String key : configMap.getKeys(false)) {
                T bean = getObj(path + "." + key, clazz);
                if (bean != null) {
                    map.put(key, bean);
                }
            }
        }
        return map;
    }

    protected <T extends IConfigBean> T getObj(String path, Class<T> clazz) {
        Object beanConfig = getConfig().get(path);
        if (beanConfig != null && beanConfig instanceof MemorySection) {
            try {
                T bean = clazz.newInstance();
                bean.toConfigBean((MemorySection) beanConfig);
                return bean;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    protected String setGetDefault(String path, String def){
        if(!getConfig().contains(path)){
            getConfig().set(path, def);
            return def;
        }
        return getConfig().getString(path);
    }
    
    protected int setGetDefault(String path, int def){
        if(!getConfig().contains(path)){
            getConfig().set(path, def);
            return def;
        }
        return getConfig().getInt(path);
    }
    
    protected boolean setGetDefault(String path, boolean def){
        if(!getConfig().contains(path)){
            getConfig().set(path, def);
            return def;
        }
        return getConfig().getBoolean(path);
    }
}
