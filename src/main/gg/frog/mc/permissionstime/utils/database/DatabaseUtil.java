package gg.frog.mc.permissionstime.utils.database;

import gg.frog.mc.permissionstime.database.SqlManager;
import lib.PatPeter.SQLibrary.Database;

public abstract class DatabaseUtil {

    protected Database getDB() {
        Database db = SqlManager.getDb();
        for (int i = 0; i < 3; i++) {
            if (!db.isOpen()) {
                if (db.open()) {
                    return db;
                }
            } else {
                return db;
            }
        }
        return db;
    }
}
