package gg.frog.mc.permissionstime.command;

import java.util.List;

import org.bukkit.command.CommandSender;

import gg.frog.mc.base.PluginMain;
import gg.frog.mc.base.config.LangCfg;
import gg.frog.mc.base.config.PluginCfg;
import gg.frog.mc.base.utils.StrUtil;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.model.cfg.PermissionPackageBean;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;

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
			String uuid = pm.getPlayerUUIDByName(args[1]);
			if (uuid != null) {
				List<PlayerDataBean> ps = sm.getTime(uuid);
				if (ps.size() > 0) {
					sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NUM_OF_PACKAGES,
							args[1], ps.size()));
					for (PlayerDataBean pdb : ps) {
						String expireString = StrUtil.timestampToString(pdb.getExpire());
						PermissionPackageBean pc = PackagesCfg.PACKAGES.get(pdb.getPackageName());
						if (pc != null && pdb.getGlobal() == pc.getGlobal()) {
							sender.sendMessage(
									StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_EXPIRATION_DATE,
											pdb.getGlobal() ? "*" : "", pc.getDisplayName(), pdb.getPackageName(),
											expireString, StrUtil.getLeftTime(pdb.getExpire())));
						} else {
							sender.sendMessage(
									StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_EXPIRATION_DATE,
											pdb.getGlobal() ? "*" : "", LangCfg.MSG_UNKNOWN_PACKAGE,
											pdb.getPackageName(), expireString, StrUtil.getLeftTime(pdb.getExpire())));
						}
					}
				} else {
					sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NO_DATA));
				}
			} else {
				sender.sendMessage(
						StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NO_FIND_PLAYER, args[1]));
			}
		} else {
			sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PARAMETER_MISMATCH));
			sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_GET, pm.PLUGIN_NAME_LOWER_CASE));
		}
	}
}
