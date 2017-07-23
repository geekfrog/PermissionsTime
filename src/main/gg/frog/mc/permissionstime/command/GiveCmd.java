package gg.frog.mc.permissionstime.command;

import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
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
            } catch (Exception e) {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_DAYS_PARAMETER_INCORRECT));
                return;
            }
            PermissionPackageBean pack = PackagesCfg.PACKAGES.get(packageName);
            if (pack != null) {
                OfflinePlayer player = pm.getOfflinePlayer(playerName);
                if (player != null) {
                    sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PROCESSING));
                    UUID uuid = player.getUniqueId();
                    if (PluginCfg.IS_DEBUG) {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + uuid.toString() + "\n" + pack.toString() + "\n" + time + " days."));
                    }
                    if (sm.giveTime(((PluginCfg.USE_MYSQL && pack.getGlobal()) ? "g:" : "") + uuid.toString(), packageName, days)) {
                        if (player.isOnline()) {
                            Player p = player.getPlayer();
                            try {
                                List<PlayerDataBean> pdbList = sm.getTime(player.getUniqueId().toString());
                                PermissionPackageBean.reloadPlayerPermissions(player, pdbList, pm);
                            } catch (Exception e) {
                                e.printStackTrace();
                                p.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_FAIL_SET_PERMISSION));
                            }
                            p.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_TELL_GIVE_PACKAGE, sender.getName(), time, pack.getDisplayName()));
                        }
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_GIVE_PACKAGE, playerName, time, pack.getDisplayName()));
                    } else {
                        pm.getServer().getScheduler().runTask(pm, new Runnable() {
                            @Override
                            public void run() {
                                pm.writeFailLog("Command execution failed. Give {0}({1}) {2}days {3} Executor: {4}", playerName, player.getUniqueId().toString(), time, pack.getDisplayName(), sender.getName());
                            }
                        });
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_GIVE_PACKAGE_FAIL, playerName, time, pack.getDisplayName()));
                    }
                } else {
                    sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NO_FIND_PLAYER, playerName));
                }
            } else {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NO_FIND_PACKAGE, packageName));
            }
        } else {
            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PARAMETER_MISMATCH));
            sender.sendMessage(StrUtil.messageFormat("&6/" + pm.PLUGIN_NAME_LOWER_CASE + " give <playerName> <packageName> <time> \n&8  - Give player package <time>day."));
        }
    }
}
