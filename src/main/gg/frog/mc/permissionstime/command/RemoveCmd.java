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
        if (args.length == 3 || args.length == 4) {
            String playerName = args[1];
            String packageName = args[2];
            boolean delGlobal = false;
            if (args.length == 4 && "t".equalsIgnoreCase(args[3]) && PluginCfg.USE_MYSQL) {
                delGlobal = true;
            }
            PermissionPackageBean pack = PackagesCfg.PACKAGES.get(packageName);
            if (pack != null) {
                OfflinePlayer player = pm.getOfflinePlayer(playerName);
                if (player != null) {
                    sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "执行中，请等待..."));
                    UUID uuid = player.getUniqueId();
                    if (PluginCfg.IS_DEBUG) {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + uuid.toString() + "\n" + pack.toString()));
                    }
                    if (sm.removeTime((delGlobal ? "g:" : "") + uuid.toString(), packageName)) {
                        if (player.isOnline()) {
                            Player p = player.getPlayer();
                            try {
                                List<PlayerDataBean> pdbList = sm.getTime(player.getUniqueId().toString());
                                PermissionPackageBean.reloadPlayerPermissions(player, pdbList, pm);
                            } catch (Exception e) {
                                e.printStackTrace();
                                p.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "修改权限失败, 请重新进入服务器!"));
                            }
                            p.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "{0}删除了你的 {1}权限包", sender.getName(), pack.getDisplayName()));
                        }
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "删除玩家 {0} 的 {1}", playerName, pack.getDisplayName()));
                    } else {
                        pm.getServer().getScheduler().runTask(pm, new Runnable() {
                            @Override
                            public void run() {
                                pm.writeFailLog("命令执行失败  删除玩家 {0}({1}) 的 {2} 执行人: {3}", playerName, player.getUniqueId().toString(), pack.getDisplayName(), sender.getName());
                            }
                        });
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
