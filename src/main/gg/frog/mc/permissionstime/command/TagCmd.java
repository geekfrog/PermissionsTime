package gg.frog.mc.permissionstime.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.config.TagNameCfg;
import gg.frog.mc.permissionstime.config.TagNameCfg.TagType;
import gg.frog.mc.permissionstime.gui.PlayerTagShow;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class TagCmd implements Runnable {

	private PluginMain pm;
	private String[] args;
	private CommandSender sender;
	private boolean isPlayer;

	public TagCmd(PluginMain pm, CommandSender sender, boolean isPlayer, String[] args) {
		this.pm = pm;
		this.sender = sender;
		this.isPlayer = isPlayer;
		this.args = args;
	}

	@Override
	public void run() {
		if (isPlayer) {
			if (args.length == 2) {
				sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PROCESSING));
				List<ItemStack> itemList = new ArrayList<ItemStack>();
				TagType type = null;
				Map<String, List<ItemStack>> p_i_map;
				if ("c".equals(args[1])) {
					type = TagNameCfg.TagType.NAMECOLOR_TYPE;
					p_i_map = TagNameCfg.NAMECOLOR_ITEMS;
				} else if ("p".equals(args[1])) {
					type = TagNameCfg.TagType.PREFIX_TYPE;
					p_i_map = TagNameCfg.PREFIX_ITEMS;
				} else if ("s".equals(args[1])) {
					type = TagNameCfg.TagType.SUFFIX_TYPE;
					p_i_map = TagNameCfg.SUFFIX_ITEMS;
				} else {
					sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PARAMETER_MISMATCH));
					sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_TAG, pm.PLUGIN_NAME_LOWER_CASE));
					return;
				}
				for (String p : p_i_map.keySet()) {
					if ("".equals(p) || sender.hasPermission(p)) {
						itemList.addAll(p_i_map.get(p));
					}
				}
				OfflinePlayer player = pm.getOfflinePlayer(sender.getName());
				Player p = player.getPlayer();
				if (p != null) {
					PlayerTagShow.show(p, type, itemList);
				}
			} else {
				sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PARAMETER_MISMATCH));
				sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_TAG, pm.PLUGIN_NAME_LOWER_CASE));
			}
		} else {
			sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "&4Only player can use this command."));
		}
	}
}
