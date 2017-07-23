package gg.frog.mc.permissionstime.command;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class GetCmd implements Runnable {

    private PluginMain pm;
    private SqlManager sm;
    private String[] args;
    private CommandSender sender;

    public GetCmd(PluginMain pm, CommandSender sender, String[] args) {
        this.pm = pm;
        this.sm = pm.getSqlManager();
        this.sender = sender;
        this.args = args;
    }

    @Override
    public void run() {
        if (args.length == 2) {
            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PROCESSING));
            OfflinePlayer p = pm.getOfflinePlayer(args[1]);
            List<PlayerDataBean> ps = sm.getTime(p.getUniqueId().toString());
            if (ps.size() > 0) {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NUM_OF_PACKAGES, args[1], ps.size()));
                for (PlayerDataBean pdb : ps) {
                    String expireString = StrUtil.timestampToString(pdb.getExpire());
                    PermissionPackageBean pc = PackagesCfg.PACKAGES.get(pdb.getPackageName());
                    if (pc != null) {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_EXPIRATION_DATE, pdb.getGlobal() ? "*" : "", pc.getDisplayName(), pdb.getPackageName(), expireString));
                    } else {
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_EXPIRATION_DATE, pdb.getGlobal() ? "*" : "", LangCfg.MSG_UNKNOWN_PACKAGE, pdb.getPackageName(), expireString));
                    }
                }
            } else {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NO_DATA));
            }
        } else {
            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PARAMETER_MISMATCH));
            sender.sendMessage(StrUtil.messageFormat("&6/" + pm.PLUGIN_NAME_LOWER_CASE + " get <playerName> \n&8  - View player packages."));
        }
    }
}
