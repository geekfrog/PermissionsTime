package gg.frog.mc.permissionstime.utils.database;

import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import lib.PatPeter.SQLibrary.Database;

public abstract class DatabaseUtil {

    private SqlManager sm;

    public DatabaseUtil(SqlManager sm) {
        this.sm = sm;
    }

    protected Database getDB() {
        Database db = sm.getDb();
        if (PluginCfg.USE_MYSQL) {
            for (int i = 0; i < 3; i++) {
                if (!db.isOpen()) {
                    if (db.open()) {
                        return db;
                    }
                } else {
                    return db;
                }
            }
        }
        return db;
    }
}
