package gg.frog.mc.base.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;

import gg.frog.mc.base.PluginMain;
import gg.frog.mc.base.config.LangCfg;
import gg.frog.mc.base.config.PluginCfg;
import gg.frog.mc.base.utils.FileUtil;
import gg.frog.mc.base.utils.FileUtil.FindFilesDo;
import gg.frog.mc.base.utils.config.PluginConfig;
import gg.frog.mc.nametags.config.TagNameCfg;
import gg.frog.mc.permissionstime.config.PackagesCfg;

/**
 * 配置文件管理
 * 
 * @author QiaoPengyu
 *
 */
public class ConfigManager {

	private PluginMain pm;
	private Map<String, PluginConfig> cfgMap = new LinkedHashMap<>();

	public ConfigManager(PluginMain pm) {
		this.pm = pm;
		copyLangFilesFromJar();
		// 添加到配置列表
		cfgMap.put("plugin", new PluginCfg(pm));
	}

	public void initConfig() {
		cfgMap.put("lang", new LangCfg("lang/" + PluginCfg.LANG + ".yml", pm));
		cfgMap.put("packages", new PackagesCfg("packages.yml", pm));
		cfgMap.put("tagNames", new TagNameCfg("tagNames.yml", pm));
	}

	public void reloadConfig(CommandSender sender) {
		for (Entry<String, PluginConfig> entry : cfgMap.entrySet()) {
			if ("lang".equals(entry.getKey())) {
				entry.setValue(new LangCfg("lang/" + PluginCfg.LANG + ".yml", pm));
			}
			entry.getValue().reloadConfig(sender);
		}
	}

	public Map<String, PluginConfig> getCfgMap() {
		return cfgMap;
	}

	private void copyLangFilesFromJar() {
		FileUtil.findFilesFromJar(new FindFilesDo() {

			@Override
			public void process(String fileName, InputStream is) {
				File f = new File(pm.getDataFolder(), fileName);
				File parentFolder = f.getParentFile();
				if (!parentFolder.exists()) {
					parentFolder.mkdirs();
				}
				try {
					OutputStream os = new FileOutputStream(f);
					int bytesRead = 0;
					byte[] buffer = new byte[8192];
					while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
						os.write(buffer, 0, bytesRead);
					}
					os.close();
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public boolean isProcess(String fileName) {
				if (fileName.matches("lang/.+\\.yml") || "config.yml".equals(fileName) || "packages.yml".equals(fileName) || "tagNames.yml".equals(fileName)) {
					File f = new File(pm.getDataFolder(), fileName);
					if (!f.exists()) {
						return true;
					}
				}
				return false;
			}
		}, this.getClass());
	}
}
