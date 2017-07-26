package gg.frog.mc.permissionstime.command;

import java.util.Map.Entry;

import org.bukkit.command.CommandSender;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class PackagesCmd implements Runnable {

    private PluginMain pm;
    private CommandSender sender;
    private String[] args;

    public PackagesCmd(PluginMain pm, CommandSender sender, String[] args) {
        this.pm = pm;
        this.sender = sender;
        this.args = args;
    }

    @Override
    public void run() {
        if (args.length == 1) {
            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PACKAGE_NUM, PackagesCfg.PACKAGES.size()));
            for (Entry<String, PermissionPackageBean> e : PackagesCfg.PACKAGES.entrySet()) {
                PermissionPackageBean p = e.getValue();
                sender.sendMessage(StrUtil.messageFormat(LangCfg.MSG_PACKAGE_LIST, p.getGlobal() ? "*" : "", e.getKey(), p.getDisplayName()));
            }
        } else if (args.length == 2) {
            String packageName = args[1];
            if (PackagesCfg.PACKAGES.containsKey(packageName)) {
                PermissionPackageBean p = PackagesCfg.PACKAGES.get(packageName);
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PACKAGE_DETAIL, packageName, p.getDisplayName(), p.getPermissions(), p.getGroups()));
            } else {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NO_FIND_PACKAGE, packageName));
            }
        } else {
            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PARAMETER_MISMATCH));
            sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_PACKAGES, pm.PLUGIN_NAME_LOWER_CASE));
        }
    }
}
