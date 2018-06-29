package gg.frog.mc.nametags.placeholder;

import org.bukkit.entity.Player;

import gg.frog.mc.base.PluginMain;
import gg.frog.mc.base.config.PluginCfg;
import gg.frog.mc.nametags.config.TagNameCfg;
import gg.frog.mc.nametags.model.PlayerTagBean;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class TagPlaceholder extends PlaceholderExpansion {

	private PluginMain pm;

	public TagPlaceholder(PluginMain pm) {
		this.pm = pm;
	}

	public boolean persist() {
		return true;
	}

	public String getIdentifier() {
		return "pttag";
	}

	public String getPlugin() {
		return null;
	}

	public String getAuthor() {
		return "GeekFrog";
	}

	public String getVersion() {
		return pm.PLUGIN_VERSION;
	}

	public String onPlaceholderRequest(Player player, String identifier) {
		if (PluginCfg.TAG_SYSTEM && player != null) {
			String uuid = pm.getPlayerUUIDByName(player);
			PlayerTagBean playerTag = TagNameCfg.PLAYER_TAG.get(uuid);
			if (playerTag != null) {
				if (identifier.equalsIgnoreCase("fullname")) {
					return playerTag.getDisplayName();
				} else if (identifier.equalsIgnoreCase("prefix")) {
					return playerTag.getDisplayPrefix();
				} else if (identifier.equalsIgnoreCase("suffix")) {
					return playerTag.getDisplaySuffix();
				}
			}
		}
		return null;
	}
}
