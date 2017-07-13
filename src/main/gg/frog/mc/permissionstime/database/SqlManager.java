package gg.frog.mc.permissionstime.database;

import java.util.Collections;
import java.util.List;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.impl.SqlitePlayerDataService;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
import gg.frog.mc.permissionstime.utils.StrUtil;
import lib.PatPeter.SQLibrary.Database;
import lib.PatPeter.SQLibrary.MySQL;
import lib.PatPeter.SQLibrary.SQLite;

public class SqlManager {

    private PluginMain pm;
    private Database db = null;
    private IPlayerDataService pds = null;

    public SqlManager(PluginMain pm) {
        this.pm = pm;
    }

    public Database getDb() {
        return db;
    }

    public boolean updateDatabase() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        if (PluginCfg.USE_MYSQL) {
            db = new MySQL(PluginMain.LOG, "[" + pm.PLUGIN_NAME + "] ", PluginCfg.SQL_HOSTNAME, PluginCfg.SQL_PORT, PluginCfg.SQL_DATABASE, PluginCfg.SQL_USERNAME, PluginCfg.SQL_PASSWORD);
        } else {
            db = new SQLite(PluginMain.LOG, "[" + pm.PLUGIN_NAME + "] ", pm.getDataFolder().getAbsolutePath(), "playerData", ".db");
            pds = new SqlitePlayerDataService(pm, this);
        }
        db.open();
        try {
            if (!pds.tableExist()) {
                pds.creatTable();
            }
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX) + "连接数据库成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PlayerDataBean> getTime(String uuid) {
        for (int i = 0; i < 3; i++) {
            try {
                return pds.queryNotExpirePlayerData(uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    public boolean giveTime(String uuid, String packageName, int days) {
        for (int i = 0; i < 3; i++) {
            try {
                if (pds.addTime(uuid, packageName, days)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean setTime(String uuid, String packageName, int days) {
        for (int i = 0; i < 3; i++) {
            try {
                if (pds.setTime(uuid, packageName, days)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean removeTime(String uuid, String packageName) {
        for (int i = 0; i < 3; i++) {
            try {
                if (pds.delPlayData(uuid, packageName)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean removeAllTime(String uuid) {
        for (int i = 0; i < 3; i++) {
            try {
                if (pds.delPlayData(uuid)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
