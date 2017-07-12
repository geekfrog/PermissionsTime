package gg.frog.mc.permissionstime.command;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class GiveCmd implements Runnable {

    private PluginMain pm;
    private SqlManager sm;
    private String[] args;
    private CommandSender sender;

    public GiveCmd(PluginMain pm, CommandSender sender, String[] args) {
        this.pm = pm;
        this.sm = pm.getSqlManager();
        this.sender = sender;
        this.args = args;
    }

    @Override
    public void run() {
        if (args.length == 4) {
            String playerName = args[1];
            String packageName = args[2];
            String time = args[3];
            int days = 0;
            try {
                days = Integer.parseInt(time);
                if (days <= 0) {
                    sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "时间参数不正确,请输入正整数"));
                    return;
                }
            } catch (Exception e) {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "时间参数不正确,请输入正整数"));
                return;
            }
            PermissionPackageBean pack = PackagesCfg.PACKAGES.get(packageName);
            if (pack != null) {
                OfflinePlayer player = pm.getOfflinePlayer(playerName);
                if (player != null) {
                    UUID uuid = player.getUniqueId();
                    if (PluginCfg.IS_DEBUG) {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + uuid.toString() + "\n" + pack.toString() + "\n" + time + "天"));
                    }
                    if (sm.giveTime(uuid.toString(), packageName, days)) {
                        if (player.isOnline()) {
                            for (String groupName : pack.getGroups()) {
                                if (!pm.getPermission().playerAddGroup(null, player, groupName)) {
                                    sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "权限组{0}添加失败", groupName));
                                }
                            }
                        }
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "给予玩家 {0} {1}天的 {2}", playerName, time, pack.getDisplayName()));
                    } else {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "未给予玩家 {0} {1}天的 {2}", playerName, time, pack.getDisplayName()));
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
