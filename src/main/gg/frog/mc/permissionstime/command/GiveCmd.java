package gg.frog.mc.permissionstime.command;

import java.util.UUID;

import org.bukkit.command.CommandSender;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.utils.PluginUtil;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class GiveCmd {

    private static SqlManager sm = PluginMain.sm;

    public static boolean onCommand(CommandSender sender, boolean isPlayer, String[] args) {
        if (args.length == 4) {
            String playerName = args[1];
            String packageName = args[2];
            String time = args[3];
            int days;
            try {
                days = Integer.parseInt(time);
                if (days <= 0) {
                    sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "时间参数不正确,请输入正整数"));
                    return false;
                }
            } catch (Exception e) {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "时间参数不正确,请输入正整数"));
                return false;
            }
            PermissionPackageBean pack = PackagesCfg.PACKAGES.get(packageName);
            if (pack != null) {
                UUID uuid = PluginUtil.getPlayerUUIDByName(playerName);
                if (uuid != null) {
                    if (PluginCfg.IS_DEBUG) {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + uuid.toString() + "\n" + pack.toString() + "\n" + time + "天"));
                    }
                    if (sm.giveTime(uuid.toString(), packageName, days)) {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "给予玩家{0} {1}天的{2}", playerName, time, pack.getDisplayName()));
                    } else {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "未给予玩家{0} {1}天的{2}", playerName, time, pack.getDisplayName()));
                    }
                } else {
                    sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "找不到名为'{0}'的玩家", playerName));
                }
            } else {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "找不到名为'{0}'的权限包", packageName));
            }
        } else {
            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "参数不正确"));
        }
        return false;
    }
}
