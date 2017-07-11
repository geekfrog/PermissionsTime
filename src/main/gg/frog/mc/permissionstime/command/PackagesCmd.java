package gg.frog.mc.permissionstime.command;

import java.util.Map.Entry;

import org.bukkit.command.CommandSender;

import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class PackagesCmd {

    public static boolean onCommand(CommandSender sender, boolean isPlayer, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "共有{0}种权限包", PackagesCfg.PACKAGES.size()));
            for (Entry<String, PermissionPackageBean> e : PackagesCfg.PACKAGES.entrySet()) {
                PermissionPackageBean p = e.getValue();
                sender.sendMessage(StrUtil.messageFormat("PackgeName: {0}, DisplayName: {1}", e.getKey(), p.getDisplayName()));
            }
        } else if (args.length == 2) {
            String packageName = args[1];
            if (PackagesCfg.PACKAGES.containsKey(packageName)) {
                PermissionPackageBean p = PackagesCfg.PACKAGES.get(packageName);
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "PackgeName: {0}, DisplayName: {1}\n" + "Permissions: {2}\n" + "Groups: {3}\n" + "Prefixs: {4}\n" + "Suffixs: {5}\n" + "", packageName, p.getDisplayName(), p.getPermissions(), p.getGroups(), p.getPrefixs(), p.getSuffixs()));
            } else {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "找不到名为'{0}'的权限包",packageName));
            }
        }
        return true;
    }
}
