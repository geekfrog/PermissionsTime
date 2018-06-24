package gg.frog.mc.nametags.model;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import gg.frog.mc.base.PluginMain;
import gg.frog.mc.base.config.PluginCfg;
import gg.frog.mc.base.utils.StrUtil;
import gg.frog.mc.base.utils.config.IConfigBean;
import gg.frog.mc.base.utils.config.PluginConfig;
import gg.frog.mc.base.utils.nms.NMSUtil;
import gg.frog.mc.nametags.config.TagNameCfg;

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
	// 当前显示的名称
	private String displayName;
	// 当前显示的前缀
	private String displayPrefix;
	// 当前显示的后缀
	private String displaySuffix;
	// 玩家所在队伍
	private Team team;
	// 不可见的队伍名
	private String invisibleName;
	// hd
	private Hologram holograms;

	public PlayerTagBean(String fileName, PluginMain pm) {
		super(fileName, pm);
	}

	@Override
	protected void init() {

	}

	@Override
	protected void loadToDo(CommandSender sender) {
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

	public static void initPlayerTag(Player player, PluginMain pm) {
		String uuid = pm.getPlayerUUIDByName(player.getName());
		PlayerTagBean playerTag = null;
		if (TagNameCfg.PLAYER_TAG.containsKey(uuid)) {
			playerTag = TagNameCfg.PLAYER_TAG.get(uuid);
		} else {
			playerTag = new PlayerTagBean("playerTag/" + uuid + ".yml", pm);
			TagNameCfg.PLAYER_TAG.put(uuid, playerTag);
		}
		playerTag.setPlayerDisplayName(player, true);
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
					displayPrefix = StrUtil.messageFormat(player, prefix + "§r" + namecolor);
					displaySuffix = StrUtil.messageFormat(player, "§r" + suffix);
					displayName = "§r" + displayPrefix + player.getName() + displaySuffix + "§r";
					if (TagNameCfg.CHANGE_DISPLAYNAME) {
						player.setDisplayName(displayName);
					}
					if (PluginCfg.IS_DEBUG) {
						System.out.println("PlayerTagBean:" + playerTag);
					}
					try {
						if (TagNameCfg.scoreboard == null) {
							TagNameCfg.scoreboard = pm.getServer().getScoreboardManager().getNewScoreboard();
						}
						if (invisibleName == null) {
							invisibleName = String.valueOf(TagNameCfg.scoreboard.getTeams().size() + 1).replaceAll("", "§");
						}
						if (!TagNameCfg.USE_HD_PLUGIN) {
							team = TagNameCfg.scoreboard.getTeam(player.getName());
							if (team == null) {
								team = TagNameCfg.scoreboard.registerNewTeam(player.getName());
							}
							String teamPrefix = displayPrefix.length() > 16 ? (displayPrefix.substring(0, 7) + ".." + displayPrefix.substring(displayPrefix.length() - 7)) : displayPrefix;
							team.setPrefix(teamPrefix);
							String teamSuffix = displaySuffix.length() > 16 ? (displaySuffix.substring(0, 7) + ".." + displaySuffix.substring(displaySuffix.length() - 7)) : displaySuffix;
							team.setSuffix(teamSuffix);
							if (!(holograms == null || holograms.isDeleted())) {
								holograms.delete();
							}
						} else {
							team = TagNameCfg.scoreboard.getTeam(invisibleName);
							if (team == null) {
								team = TagNameCfg.scoreboard.registerNewTeam(invisibleName);
							}
							team.setPrefix("");
							team.setSuffix("");
							if (NMSUtil.getServerVersion().startsWith("v1_7") || NMSUtil.getServerVersion().startsWith("v1_8")) {

							} else {
								team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
							}
							initHologramsName(player);
						}
						team.addPlayer(player);
						player.setScoreboard(TagNameCfg.scoreboard);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void initHologramsName(Player player) {
		Location loc = player.getLocation();
		loc = new Location(loc.getWorld(), loc.getX(), loc.getY() + (TagNameCfg.ONE_LINE_DISPLAY ? 2.25 : 2.75), loc.getZ());
		if (holograms == null || holograms.isDeleted()) {
			holograms = HologramsAPI.createHologram(pm, loc);
		} else {
			holograms.teleport(loc);
		}
		holograms.clearLines();
		if (TagNameCfg.ONE_LINE_DISPLAY) {
			holograms.appendTextLine(getDisplayNameStr(player));
		} else {
			holograms.appendTextLine(StrUtil.messageFormat(player, prefix));
			holograms.appendTextLine(StrUtil.messageFormat(player, namecolor + player.getName()));
			holograms.appendTextLine(StrUtil.messageFormat(player, suffix));
		}
		holograms.getVisibilityManager().hideTo(player);
	}

	public void moveHologramsName(Player player) {
		if (!(holograms == null || holograms.isDeleted())) {
			Location loc = player.getLocation();
			loc = new Location(loc.getWorld(), loc.getX(), loc.getY() + (TagNameCfg.ONE_LINE_DISPLAY ? 2.25 : 2.75), loc.getZ());
			holograms.teleport(loc);
		}
	}

	/**
	 * 退出游戏调用
	 */
	public void delHologramsName() {
		if (!(holograms == null || holograms.isDeleted())) {
			holograms.delete();
		}
	}

	public String getDisplayNameStr(Player player) {
		return StrUtil.messageFormat(player, prefix + "§r" + namecolor + player.getName() + "§r" + suffix);
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

	public String getDisplayName() {
		return displayName;
	}

	public String getDisplayPrefix() {
		return displayPrefix;
	}

	public String getDisplaySuffix() {
		return displaySuffix;
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
		return "PlayerTagBean [namecolor=" + namecolor + ", prefix=" + prefix + ", suffix=" + suffix + ", displayName=" + displayName + ", displayPrefix=" + displayPrefix + ", displaySuffix=" + displaySuffix + ", invisibleName=" + invisibleName + "]";
	}

}
