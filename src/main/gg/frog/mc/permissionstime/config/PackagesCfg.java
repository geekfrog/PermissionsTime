package gg.frog.mc.permissionstime.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.model.PermissionPackageBean;
import gg.frog.mc.permissionstime.utils.config.PluginConfig;

public class PackagesCfg extends PluginConfig {

    public static String PACKAGES_VERSION = null;
    public static String DEFAULT_GROUP = null;
    public static Map<String, PermissionPackageBean> PACKAGES = new HashMap<>();

    public PackagesCfg(String fileName) {
        super(fileName);
    }

    @Override
    protected void init() {
        getConfig().set("version", PluginMain.PLUGIN_VERSION);
        getConfig().set("defaultGroup", "Default");
        saveConfig();
    }

    @Override
    protected void loadToDo() {
        PACKAGES_VERSION = getConfig().getString("version", "?");
        DEFAULT_GROUP = getConfig().getString("defaultGroup", "Default");
        PACKAGES = getObjMap("packages", PermissionPackageBean.class);
        if (PluginCfg.IS_DEBUG) {
            System.out.println("packages vresion:" + PACKAGES_VERSION);
            System.out.println("defaultGroup:" + DEFAULT_GROUP);
            for (Entry<String, PermissionPackageBean> p : PACKAGES.entrySet()) {
                System.out.println(p);
            }
        }
    }

}
