package gg.frog.mc.permissionstime.model;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import gg.frog.mc.permissionstime.utils.config.IConfigBean;

/**
 * 权限包实体类
 * 
 * @author QiaoPengyu
 *
 */
public class PermissionPackageBean implements IConfigBean {

    private List<String> permissions = new ArrayList<>();
    private List<String> groups = new ArrayList<>();
    private List<String> prefixs = new ArrayList<>();
    private List<String> suffixs = new ArrayList<>();

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getPrefixs() {
        return prefixs;
    }

    public void setPrefixs(List<String> prefixs) {
        this.prefixs = prefixs;
    }

    public List<String> getSuffixs() {
        return suffixs;
    }

    public void setSuffixs(List<String> suffixs) {
        this.suffixs = suffixs;
    }

    @Override
    public YamlConfiguration toConfig() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("permissions", permissions);
        config.set("groups", groups);
        config.set("prefixs", prefixs);
        config.set("suffixs", suffixs);
        return config;
    }

    @Override
    public void toConfigBean(MemorySection config) {
        permissions = config.getStringList("permissions");
        groups = config.getStringList("groups");
        prefixs = config.getStringList("prefixs");
        suffixs = config.getStringList("suffixs");
    }

    @Override
    public String toString() {
        return "PermissionPackageBean [permissions=" + permissions + ", groups=" + groups + ", prefixs=" + prefixs + ", suffixs=" + suffixs + "]";
    }
}
