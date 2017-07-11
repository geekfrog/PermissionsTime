package gg.frog.mc.permissionstime.command;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.utils.PluginUtil;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class GiveCmd {

    private static PluginMain pm = PluginMain.getInstance();
    private static SqlManager sm = PluginMain.sm;
    
    public static boolean onCommand(CommandSender sender, Command command, boolean isPlayer, String[] args) {
        if (args.length == 4) {
            String playerName = args[1];
            String packageName = args[2];
            String time = args[3];
            PermissionPackageBean pack = PackagesCfg.PACKAGES.get(packageName);
            if (pack != null) {
                UUID uuid = PluginUtil.getPlayerUUIDByName(playerName);
                if (uuid != null) {
                    if (PluginCfg.IS_DEBUG) {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + uuid.toString() + "\n" + pack.toString() + "\n" + time));
                    }
                    return true;
                } else {
                    sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "找不到名为'" + playerName + "'的玩家"));
                }
            } else {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "找不到名为'" + packageName + "'的权限包"));
            }
        } else {
            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "参数不正确" + "<playerName> <packageName> <time>"));
        }
        return false;
    }
}
