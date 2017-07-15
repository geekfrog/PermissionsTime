package gg.frog.mc.permissionstime.command;

import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class SetCmd implements Runnable {

    private PluginMain pm;
    private SqlManager sm;
    private String[] args;
    private CommandSender sender;

    public SetCmd(PluginMain pm, CommandSender sender, String[] args) {
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
                    sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "执行中，请等待..."));
                    UUID uuid = player.getUniqueId();
                    if (PluginCfg.IS_DEBUG) {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + uuid.toString() + "\n" + pack.toString() + "\n" + time + "天"));
                    }
                    if (sm.setTime(((PluginCfg.USE_MYSQL && pack.getGlobal()) ? "g:" : "") + uuid.toString(), packageName, days)) {
                        if (player.isOnline()) {
                            Player p = player.getPlayer();
                            try {
                                List<PlayerDataBean> pdbList = sm.getTime(player.getUniqueId().toString());
                                PermissionPackageBean.reloadPlayerPermissions(player, pdbList, pm);
                            } catch (Exception e) {
                                e.printStackTrace();
                                p.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "修改权限失败, 请重新进入服务器!"));
                            }
                            p.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "{0}设置你 {1}天的 {2}", sender.getName(), time, pack.getDisplayName()));
                        }
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "设置玩家 {0} {1}天的 {2}", playerName, time, pack.getDisplayName()));
                    } else {
                        pm.getServer().getScheduler().runTask(pm, new Runnable() {
                            @Override
                            public void run() {
                                pm.writeFailLog("命令执行失败  设置玩家 {0}({1}) {2}天的 {3} 执行人: {4}", playerName, player.getUniqueId().toString(), time, pack.getDisplayName(), sender.getName());
                            }
                        });
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "未设置玩家 {0} {1}天的 {2}", playerName, time, pack.getDisplayName()));
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
