package gg.frog.mc.permissionstime.command;

import java.util.UUID;

import org.bukkit.command.CommandSender;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class RemoveCmd implements Runnable {

    private PluginMain pm;
    private SqlManager sm;
    private String[] args;
    private CommandSender sender;

    public RemoveCmd(PluginMain pm, CommandSender sender, String[] args) {
        this.pm = pm;
        this.sm = pm.getSqlManager();
        this.sender = sender;
        this.args = args;
    }

    @Override
    public void run() {
        if (args.length == 3) {
            String playerName = args[1];
            String packageName = args[2];
            PermissionPackageBean pack = PackagesCfg.PACKAGES.get(packageName);
            if (pack != null) {
                UUID uuid = pm.getPlayerUUIDByName(playerName);
                if (uuid != null) {
                    if (PluginCfg.IS_DEBUG) {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + uuid.toString() + "\n" + pack.toString()));
                    }
                    if (sm.removeTime(uuid.toString(), packageName)) {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "删除玩家 {0} 的 {1}", playerName, pack.getDisplayName()));
                    } else {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "未删除玩家 {0} 的 {1}", playerName, pack.getDisplayName()));
                    }
                } else {
                    sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "找不到名为''{0}''的玩家", playerName));
                }
            } else {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "找不到名为''{0}''的权限包", packageName));
            }
        } else {
            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "参数不正确"));
        }
    }
}
