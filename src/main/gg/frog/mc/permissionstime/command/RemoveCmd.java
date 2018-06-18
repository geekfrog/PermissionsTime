package gg.frog.mc.permissionstime.command;

import java.util.List;

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
					sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PROCESSING));
					String uuid = player.getUniqueId().toString();
					if (PluginCfg.IS_DEBUG) {
						sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + uuid + "\n" + pack.toString()));
					}
					if (sm.removeTime((delGlobal ? "g:" : "") + uuid, packageName)) {
						if (player.isOnline()) {
							Player p = player.getPlayer();
							try {
								List<PlayerDataBean> pdbList = sm.getTime(uuid);
								PermissionPackageBean.reloadPlayerPermissions(player, pdbList, pm);
							} catch (Exception e) {
								e.printStackTrace();
								p.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_FAIL_SET_PERMISSION));
							}
							p.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_TELL_DEL_PACKAGE, sender.getName(), pack.getDisplayName()));
						}
						sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_DEL_PACKAGE, playerName, pack.getDisplayName()));
					} else {
						pm.getServer().getScheduler().runTask(pm, new Runnable() {
							@Override
							public void run() {
								pm.writeFailLog("Command execution failed. Delete {0}({1})'s packages which be named {2} Executor: {3}", playerName, uuid, pack.getDisplayName(), sender.getName());
							}
						});
						sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_DEL_PACKAGE_FAIL, playerName, pack.getDisplayName()));
					}
				} else {
					sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NO_FIND_PLAYER, playerName));
				}
			} else {
				sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NO_FIND_PACKAGE, packageName));
			}
		} else {
			sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PARAMETER_MISMATCH));
			sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_REMOVE, pm.PLUGIN_NAME_LOWER_CASE));
		}
	}
}
