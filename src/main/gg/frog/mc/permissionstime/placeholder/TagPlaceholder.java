package gg.frog.mc.permissionstime.placeholder;

import org.bukkit.entity.Player;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.config.TagNameCfg;
import gg.frog.mc.permissionstime.model.PlayerTagBean;
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
		return pm.PLUGIN_NAME_LOWER_CASE;
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
		if (identifier.equalsIgnoreCase("displayname") && PluginCfg.TAG_SYSTEM) {
			if (player != null) {
				String uuid = pm.getPlayerUUIDByName(player.getName());
				PlayerTagBean playerTag = TagNameCfg.PLAYER_TAG.get(uuid);
				if (playerTag != null) {
					return playerTag.getDisplayName();
				}
			}
		}
		return null;
	}
}
