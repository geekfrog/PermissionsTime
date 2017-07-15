package gg.frog.mc.permissionstime.command;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.gui.PlayerPermissionShow;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class MeCmd implements Runnable {

    private PluginMain pm;
    private SqlManager sm;
    private String[] args;
    private CommandSender sender;
    private boolean isPlayer;

    public MeCmd(PluginMain pm, CommandSender sender, boolean isPlayer, String[] args) {
        this.pm = pm;
        this.sm = pm.getSqlManager();
        this.sender = sender;
        this.isPlayer = isPlayer;
        this.args = args;
    }

    @Override
    public void run() {
        if (isPlayer) {
            if (args.length < 2) {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "执行中，请等待..."));
                // String page = args[1];
                Player p = (Player) sender;
                List<PlayerDataBean> ps = sm.getTime(p.getUniqueId().toString());
                if (ps.size() > 0) {
                    PlayerPermissionShow.show(p, ps);
                } else {
                    sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "暂时无数据"));
                }
            } else {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "参数不正确"));
            }
        } else {
            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "此命令只能玩家执行"));
        }
    }
}
