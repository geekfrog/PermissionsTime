package gg.frog.mc.permissionstime.config;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.utils.config.PluginConfig;

/**
 * 插件默认配置
 * 
 * @author QiaoPengyu
 *
 */
public class PluginCfg extends PluginConfig {

    public static String PLUGIN_PREFIX;
    public static boolean IS_METRICS = true;
    public static boolean IS_DEBUG = false;
    public static boolean TAG_SYSTEM = false;
    public static String LANG;
    public static boolean USE_MYSQL = false;
    public static String SQL_SERVER_ID;
    public static String SQL_HOSTNAME;
    public static int SQL_PORT;
    public static String SQL_DATABASE;
    public static String SQL_USERNAME;
    public static String SQL_PASSWORD;
    public static String SQL_TABLE_PREFIX;

    public PluginCfg(PluginMain pm) {
        super(pm);
    }

    @Override
    protected void init() {}

    @Override
    protected void loadToDo() {
        PLUGIN_PREFIX = setGetDefault("pluginPrefix", "&b[" + pm.PLUGIN_NAME + "] ") + "&r";
        IS_DEBUG = setGetDefault("debug", false);
        IS_METRICS = setGetDefault("metrics", true);
        TAG_SYSTEM = setGetDefault("tagSystem", false);
        LANG = setGetDefault("lang", "en");
        USE_MYSQL = setGetDefault("mysql.enable", false);
        SQL_SERVER_ID = setGetDefault("mysql.serverId", "default");
        SQL_HOSTNAME = setGetDefault("mysql.hostname", "localhost");
        SQL_PORT = setGetDefault("mysql.port", 3306);
        SQL_DATABASE = setGetDefault("mysql.database", "minecraft");
        SQL_USERNAME = setGetDefault("mysql.username", "user");
        SQL_PASSWORD = setGetDefault("mysql.password", "123456");
        SQL_TABLE_PREFIX = setGetDefault("mysql.tablePrefix", "pt_");
    }

}
