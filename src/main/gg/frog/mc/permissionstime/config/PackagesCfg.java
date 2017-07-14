package gg.frog.mc.permissionstime.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.utils.config.PluginConfig;

public class PackagesCfg extends PluginConfig {

    public static String PACKAGES_VERSION = null;
    public static String DEFAULT_GROUP = null;
    public static Map<String, PermissionPackageBean> PACKAGES = new ConcurrentHashMap<>();
    public static Set<String> allPermissions = Collections.synchronizedSet(new HashSet<String>());
    public static Set<String> allGroups = Collections.synchronizedSet(new HashSet<String>());

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
        for (PermissionPackageBean p : PACKAGES.values()) {
            allPermissions.addAll(p.getPermissions());
            allGroups.addAll(p.getGroups());
        }
    }
}
