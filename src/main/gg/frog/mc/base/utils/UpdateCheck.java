package gg.frog.mc.base.utils;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.configuration.file.YamlConfiguration;
import com.google.common.base.Charsets;

import gg.frog.mc.base.PluginMain;
import gg.frog.mc.base.config.PluginCfg;

public class UpdateCheck implements Runnable {

    private PluginMain pm;

    public UpdateCheck(PluginMain pm) {
        this.pm = pm;
    }

    @Override
    public void run() {
        try {
            String pluginInfoUrl = "https://raw.githubusercontent.com/geekfrog/PermissionsTime/master/src/resources/plugin.yml";
            URL url = new URL(pluginInfoUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            connection.setRequestMethod("GET");
            YamlConfiguration tempConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(connection.getInputStream(), Charsets.UTF_8));
            String version = tempConfig.getString("version", pm.PLUGIN_VERSION);
            if (!pm.PLUGIN_VERSION.equals(version)) {
                pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4There is a new version ''{0}'' of the plugin.", version));
            } else {
                pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§2No new version available."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
