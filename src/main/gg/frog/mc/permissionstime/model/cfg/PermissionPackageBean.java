package gg.frog.mc.permissionstime.model.cfg;

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

    private String displayName = null;
    private Integer days = null;
    private List<String> permissions = new ArrayList<>();
    private List<String> groups = new ArrayList<>();
    private List<String> prefixs = new ArrayList<>();
    private List<String> suffixs = new ArrayList<>();

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

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
        config.set("displayName", displayName);
        config.set("days", days);
        config.set("permissions", permissions);
        config.set("groups", groups);
        config.set("prefixs", prefixs);
        config.set("suffixs", suffixs);
        return config;
    }

    @Override
    public void toConfigBean(MemorySection config) {
        displayName = config.getString("displayName");
        if (displayName == null) {
            displayName = "No Name";
        }
        days = config.getInt("days");
        permissions = config.getStringList("permissions");
        groups = config.getStringList("groups");
        prefixs = config.getStringList("prefixs");
        suffixs = config.getStringList("suffixs");
    }

    @Override
    public String toString() {
        return "PermissionPackageBean [displayName=" + displayName + ", days=" + days + ", permissions=" + permissions + ", groups=" + groups + ", prefixs=" + prefixs + ", suffixs=" + suffixs + "]";
    }
}
