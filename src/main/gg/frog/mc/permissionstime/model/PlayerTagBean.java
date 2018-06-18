package gg.frog.mc.permissionstime.model;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
	// 当前显示的名称
	private String displayName;

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
					displayName = getDisplayNameStr(player);
					player.setDisplayName(displayName);
					if (PluginCfg.IS_DEBUG) {
						System.out.println("PlayerTagBean:" + playerTag);
					}
					if (!TagNameCfg.USE_HD_PLUGIN) {
						try {
							if (TagNameCfg.scoreboard == null) {
								TagNameCfg.scoreboard = pm.getServer().getScoreboardManager().getNewScoreboard();
							}
							Team team = TagNameCfg.scoreboard.getTeam(player.getName());
							if (team == null) {
								team = TagNameCfg.scoreboard.registerNewTeam(player.getName());
							}
							String teamPrefix = StrUtil.messageFormat(player, prefix + "&r" + namecolor);
							if (PluginCfg.IS_DEBUG)
								System.out.println(teamPrefix);
							teamPrefix = teamPrefix.length() > 16 ? (teamPrefix.substring(0, 7) + ".." + teamPrefix.substring(teamPrefix.length() - 7)) : teamPrefix;
							if (PluginCfg.IS_DEBUG)
								System.out.println(teamPrefix);
							team.setPrefix(teamPrefix);
							String teamSuffix = StrUtil.messageFormat(player, "&r" + suffix);
							if (PluginCfg.IS_DEBUG)
								System.out.println(teamSuffix);
							teamSuffix = teamSuffix.length() > 16 ? (teamSuffix.substring(0, 7) + ".." + teamSuffix.substring(teamSuffix.length() - 7)) : teamSuffix;
							if (PluginCfg.IS_DEBUG) {
								System.out.println(teamSuffix);
							}
							team.setSuffix(teamSuffix);
							team.addEntry(player.getName());
							player.setScoreboard(TagNameCfg.scoreboard);
							if (PluginCfg.IS_DEBUG)
								for (Team t : TagNameCfg.scoreboard.getTeams()) {
									System.out.println(t.getPrefix());
									System.out.println(t.getSuffix());
									for (String e : t.getEntries()) {
										System.out.println(e);
									}
								}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						//TODO
						//player.setCustomNameVisible(false);
					}
				}
			}
		});
	}

	public String getDisplayNameStr(Player player) {
		return StrUtil.messageFormat(player, "&r" + prefix + "&r" + namecolor + player.getName() + "&r" + suffix + "&r");
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
