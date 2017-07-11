package gg.frog.mc.permissionstime.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.utils.config.PluginConfig;

public class PackagesCfg extends PluginConfig {

    public static String PACKAGES_VERSION = null;
    public static String DEFAULT_GROUP = null;
    public static Map<String, PermissionPackageBean> PACKAGES = new HashMap<>();

    public PackagesCfg(String fileName, PluginMain pm) {
        super(fileName, pm);
    }

    @Override
    protected void init() {}

    @Override
    protected void loadToDo() {
        PACKAGES_VERSION = setGetDefault("version", "?");
        DEFAULT_GROUP = setGetDefault("defaultGroup", "Default");
        PACKAGES = getObjMap("packages", PermissionPackageBean.class);
        saveObj("packages", PACKAGES);
        if (PluginCfg.IS_DEBUG) {
            System.out.println("packages vresion:" + PACKAGES_VERSION);
            System.out.println("defaultGroup:" + DEFAULT_GROUP);
            for (Entry<String, PermissionPackageBean> p : PACKAGES.entrySet()) {
                System.out.println(p.getKey() + ":" + p.getValue());
            }
        }
    }

}
