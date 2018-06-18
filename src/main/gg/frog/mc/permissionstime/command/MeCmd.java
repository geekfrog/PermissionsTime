package gg.frog.mc.permissionstime.command;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.LangCfg;
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
			if (args.length == 1) {
				sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PROCESSING));
				String uuid = pm.getPlayerUUIDByName(sender.getName());
				List<PlayerDataBean> ps = sm.getTime(uuid);
				PlayerPermissionShow.show((Player) sender, ps);
			} else {
				sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PARAMETER_MISMATCH));
				sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_ME, pm.PLUGIN_NAME_LOWER_CASE));
			}
		} else {
			sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "&4Only player can use this command."));
		}
	}
}
