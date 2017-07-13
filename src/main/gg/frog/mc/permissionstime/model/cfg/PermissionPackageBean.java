package gg.frog.mc.permissionstime.model.cfg;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import gg.frog.mc.permissionstime.utils.config.IConfigBean;
import net.milkbowl.vault.permission.Permission;

/**
 * 权限包实体类
 * 
 * @author QiaoPengyu
 *
 */
public class PermissionPackageBean implements IConfigBean {

    private String displayName = null;
    private Integer days = null;
    private Boolean global = null;
    private List<String> permissions = new ArrayList<>();
    private List<String> groups = new ArrayList<>();

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

    public Boolean getGlobal() {
        return global;
    }

    public void setGlobal(Boolean global) {
        this.global = global;
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

    @Override
    public YamlConfiguration toConfig() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("displayName", displayName);
        config.set("days", days);
        config.set("global", global);
        config.set("permissions", permissions);
        config.set("groups", groups);
        return config;
    }

    @Override
    public void toConfigBean(MemorySection config) {
        displayName = config.getString("displayName");
        if (displayName == null) {
            displayName = "No Name";
        }
        days = config.getInt("days");
        global = config.getBoolean("global");
        permissions = config.getStringList("permissions");
        groups = config.getStringList("groups");
    }

    @Override
    public String toString() {
        return "PermissionPackageBean [displayName=" + displayName + ", days=" + days + ", global=" + global + ", permissions=" + permissions + ", groups=" + groups + "]";
    }

    public void givePlayer(OfflinePlayer player, CommandSender sender, Permission permission) {
        List<World> worlds = sender.getServer().getWorlds();
        for (String pem : permissions) {
            String[] args = pem.split(":");
            pem = args[0];
            if (args.length > 1) {
                for (int i = 1; i < args.length; i++) {
                    String worldName = args[i];
                    permission.playerAdd(worldName, player, pem);
                }
            } else {
                for (World world : worlds) {
                    String worldName = world.getName();
                    permission.playerAdd(worldName, player, pem);
                }
            }
        }
        for (String groupName : groups) {
            String[] args = groupName.split(":");
            groupName = args[0];
            if (args.length > 1) {
                for (int i = 1; i < args.length; i++) {
                    String worldName = args[i];
                    permission.playerAddGroup(worldName, player, groupName);
                }
            } else {
                for (World world : worlds) {
                    String worldName = world.getName();
                    permission.playerAddGroup(worldName, player, groupName);
                }
            }
        }
    }

    public void clearPlayer(OfflinePlayer player, CommandSender sender, Permission permission) {
        List<World> worlds = sender.getServer().getWorlds();
        for (String pem : permissions) {
            String[] args = pem.split(":");
            pem = args[0];
            if (args.length > 1) {
                for (int i = 1; i < args.length; i++) {
                    String worldName = args[i];
                    permission.playerAdd(worldName, player, pem);
                }
            } else {
                for (World world : worlds) {
                    String worldName = world.getName();
                    permission.playerAdd(worldName, player, pem);
                }
            }
        }
        for (String groupName : groups) {
            String[] args = groupName.split(":");
            groupName = args[0];
            if (args.length > 1) {
                for (int i = 1; i < args.length; i++) {
                    String worldName = args[i];
                    permission.playerRemoveGroup(worldName, player, groupName);
                }
            } else {
                for (World world : worlds) {
                    String worldName = world.getName();
                    permission.playerRemoveGroup(worldName, player, groupName);
                }
            }
        }
    }

}
