package gg.frog.mc.permissionstime.model.cfg;

import java.util.List;

import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.config.TagNameCfg;
import gg.frog.mc.permissionstime.utils.StrUtil;
import gg.frog.mc.permissionstime.utils.config.IConfigBean;
import gg.frog.mc.permissionstime.utils.config.PluginConfig;

/**
 * 玩家标签包实体类
 * 
 * @author QiaoPengyu
 *
 */
public class PlayerTagBean extends PluginConfig implements IConfigBean, Cloneable {

	// 名称颜色
	private String namecolor;
	// 前缀
	private String prefix;
	// 后缀
	private String suffix;

	public PlayerTagBean(String fileName, PluginMain pm) {
		super(fileName, pm);
		if (PluginCfg.IS_DEBUG) {
			System.out.println("Player Tag File:" + fileName);
		}
	}

	@Override
	protected void init() {

	}

	@Override
	protected void loadToDo() {
		namecolor = getConfig().getString("namecolor", TagNameCfg.DEFAULT_NAMECOLOR);
		prefix = getConfig().getString("prefix", TagNameCfg.DEFAULT_PREFIX);
		suffix = getConfig().getString("suffix", TagNameCfg.DEFAULT_SUFFIX);
	}

	public void saveConfig() {
		getConfig().set("namecolor", namecolor);
		getConfig().set("prefix", prefix);
		getConfig().set("suffix", suffix);
		super.saveConfig();
	}

	public void setPlayerDisplayName(Player player) {
		setPlayerDisplayName(player, false);
	}

	public void setPlayerDisplayName(Player player, boolean forceSet) {
		PlayerTagBean playerTag = this;
		pm.getServer().getScheduler().runTask(pm, new Runnable() {
			@Override
			public void run() {
				boolean namecolor_flag = true;
				boolean prefix_flag = true;
				boolean suffix_flag = true;
				if (!(namecolor.equals(TagNameCfg.DEFAULT_NAMECOLOR) && prefix.equals(TagNameCfg.DEFAULT_PREFIX) && suffix.equals(TagNameCfg.DEFAULT_SUFFIX))) {
					if (TagNameCfg.NAMECOLOR_PERMISSIONS.containsKey(namecolor)) {
						for (String p : TagNameCfg.NAMECOLOR_PERMISSIONS.get(namecolor)) {
							if (p == null || p.length() == 0 || player.hasPermission(p)) {
								namecolor_flag = false;
								break;
							}
						}
					}
					if (namecolor_flag) {
						namecolor = TagNameCfg.DEFAULT_NAMECOLOR;
					}
					if (TagNameCfg.PREFIX_PERMISSIONS.containsKey(prefix)) {
						for (String p : TagNameCfg.PREFIX_PERMISSIONS.get(prefix)) {
							if (p == null || p.length() == 0 || player.hasPermission(p)) {
								prefix_flag = false;
								break;
							}
						}
					}
					if (prefix_flag) {
						prefix = TagNameCfg.DEFAULT_PREFIX;
					}
					if (TagNameCfg.SUFFIX_PERMISSIONS.containsKey(suffix)) {
						for (String p : TagNameCfg.SUFFIX_PERMISSIONS.get(suffix)) {
							if (p == null || p.length() == 0 || player.hasPermission(p)) {
								suffix_flag = false;
								break;
							}
						}
					}
					if (suffix_flag) {
						suffix = TagNameCfg.DEFAULT_SUFFIX;
					}
				}
				if (forceSet || namecolor_flag || prefix_flag || suffix_flag) {
					Scoreboard scoreboard = pm.getServer().getScoreboardManager().getNewScoreboard();
					Team team = scoreboard.getTeam(player.getName());
					if (team == null) {
						team = scoreboard.registerNewTeam(player.getName());
					}
					team.setPrefix(StrUtil.messageFormat(player, "&r" + prefix + "&r" + namecolor + "&r"));
					team.setSuffix(StrUtil.messageFormat(player, "&r" + suffix + "&r"));
					team.addEntry(player.getName());
					player.setScoreboard(scoreboard);
					List<World> worlds = pm.getServer().getWorlds();
					for (World world : worlds) {
						pm.getChat().setPlayerInfoString(world.getName(), player, "prefix", StrUtil.messageFormat(player, "&r" + prefix + "&r" + namecolor + "&r"));
						pm.getChat().setPlayerInfoString(world.getName(), player, "suffix", StrUtil.messageFormat(player, "&r" + suffix + "&r"));
					}
					player.setDisplayName(getDisplayNameStr(player));
					if (PluginCfg.IS_DEBUG) {
						System.out.println("PlayerTagBean:" + playerTag);
					}
				}
			}
		});
	}

	public String getDisplayNameStr(Player player) {
		return StrUtil.messageFormat(player, prefix + namecolor + player.getName() + suffix + "&r");
	}

	@Override
	public PlayerTagBean clone() {
		PlayerTagBean t = null;
		try {
			t = (PlayerTagBean) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return t;
	}

	public String getNamecolor() {
		return namecolor;
	}

	public void setNamecolor(String namecolor) {
		this.namecolor = namecolor;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public YamlConfiguration toConfig() {
		YamlConfiguration config = new YamlConfiguration();
		config.set("namecolor", namecolor);
		config.set("prefix", prefix);
		config.set("suffix", suffix);
		return config;
	}

	@Override
	public void toConfigBean(MemorySection config) {
		namecolor = config.getString("namecolor");
		prefix = config.getString("prefix");
		suffix = config.getString("suffix");
	}

	@Override
	public String toString() {
		return "PlayerTagBean [namecolor=" + namecolor + ", prefix=" + prefix + ", suffix=" + suffix + "]";
	}

}
