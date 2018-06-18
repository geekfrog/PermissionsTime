package gg.frog.mc.permissionstime.command;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class RemoveAllCmd implements Runnable {

	private PluginMain pm;
	private SqlManager sm;
	private String[] args;
	private CommandSender sender;

	public RemoveAllCmd(PluginMain pm, CommandSender sender, String[] args) {
		this.pm = pm;
		this.sm = pm.getSqlManager();
		this.sender = sender;
		this.args = args;
	}

	@Override
	public void run() {
		if (args.length == 2 || args.length == 3) {
			String playerName = args[1];
			boolean delGlobal = false;
			if (args.length == 3 && "t".equalsIgnoreCase(args[2]) && PluginCfg.USE_MYSQL) {
				delGlobal = true;
			}
			OfflinePlayer player = pm.getOfflinePlayer(playerName);
			if (player != null) {
				sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PROCESSING));
				String uuid = player.getUniqueId().toString();
				if (PluginCfg.IS_DEBUG) {
					sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + uuid));
				}
				if (sm.removeAllTime((delGlobal ? "g:" : "") + uuid)) {
					if (player.isOnline()) {
						Player p = player.getPlayer();
						try {
							List<PlayerDataBean> pdbList = sm.getTime(uuid);
							PermissionPackageBean.reloadPlayerPermissions(player, pdbList, pm);
						} catch (Exception e) {
							e.printStackTrace();
							p.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_FAIL_SET_PERMISSION));
						}
						p.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_TELL_DEL_ALL, sender.getName()));
					}
					sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_DEL_ALL, playerName));
				} else {
					pm.getServer().getScheduler().runTask(pm, new Runnable() {
						@Override
						public void run() {
							pm.writeFailLog("Command execution failed. Delete {0}({1})'s all packages. Executor: {2}", playerName, uuid, sender.getName());
						}
					});
					sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_DEL_ALL_FAIL, playerName));
				}
			} else {
				sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NO_FIND_PLAYER, playerName));
			}
		} else {
			sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PARAMETER_MISMATCH));
			sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_REMOVEALL, pm.PLUGIN_NAME_LOWER_CASE));
		}
	}
}
