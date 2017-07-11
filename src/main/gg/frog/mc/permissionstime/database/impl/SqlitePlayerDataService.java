package gg.frog.mc.permissionstime.database.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.IPlayerDataService;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
import gg.frog.mc.permissionstime.utils.StrUtil;
import gg.frog.mc.permissionstime.utils.database.DatabaseUtil;

public class SqlitePlayerDataService extends DatabaseUtil implements IPlayerDataService {

    private PluginMain pm = PluginMain.getInstance();

    @Override
    public boolean tableExist() throws Exception {
        String sql = "SELECT count(*) AS num FROM \"main\".sqlite_master M where (tbl_name='playerData' AND type='table');";
        try {
            ResultSet rs = getDB().query(sql);
            int num = rs.getInt("num");
            if (num == 1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "无法检查有无数据表"));
            throw e;
        }
    }

    @Override
    public boolean creatTable() throws Exception {
        String sql = "CREATE TABLE \"playerData\" ( \"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \"uuid\" TEXT NOT NULL, \"packageName\" TEXT NOT NULL, \"expire\" INTEGER NOT NULL, CONSTRAINT \"UUID_PACKAGE\" UNIQUE (\"uuid\", \"packageName\"));";
        try {
            getDB().query(sql);
            return true;
        } catch (Exception e) {
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "无法创建数据表"));
            throw e;
        }
    }

    @Override
    public boolean setPlayerData(PlayerDataBean bean) throws Exception {
        PlayerDataBean pdb = bean;
        String sql;
        if (pdb.getId() != null) {
            sql = "UPDATE \"main\".\"playerData\" SET \"uuid\"='" + pdb.getUuid() + "', \"packageName\"='" + pdb.getPackageName() + "', \"expire\"=" + pdb.getExpire() + " WHERE (\"id\"=" + pdb.getId() + ");";
        } else {
            sql = "INSERT INTO \"main\".\"playerData\" (\"uuid\", \"packageName\", \"expire\") VALUES ('" + pdb.getUuid() + "', '" + pdb.getPackageName() + "', " + pdb.getExpire() + ");";
        }
        try {
            getDB().query(sql);
            return true;
        } catch (Exception e) {
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "无法创建数据: {0}", pdb));
            throw e;
        }
    }

    @Override
    public boolean addTime(String uuid, String packageName, int days) throws Exception {
        PlayerDataBean pdb = queryPlayerData(uuid, packageName);
        long now = new Date().getTime();
        long addTime = days * 24 * 60 * 60 * 1000;
        long expire = 0;
        if (pdb == null) {
            expire = now + addTime;
            pdb = new PlayerDataBean(null, uuid, packageName, expire);
            return setPlayerData(pdb);
        } else {
            if (pdb.getExpire() < now) {
                expire = now + addTime;
                pdb.setExpire(expire);
                return setPlayerData(pdb);
            } else {
                String sql = "UPDATE \"main\".\"playerData\" SET \"expire\"=\"expire\"+" + addTime + " WHERE (\"id\"=" + pdb.getId() + ");";
                try {
                    getDB().query(sql);
                    return true;
                } catch (Exception e) {
                    pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "无法更新数据: {0}", pdb));
                    throw e;
                }
            }
        }
    }

    @Override
    public List<PlayerDataBean> queryPlayerData(String uuid) throws Exception {
        String sql = "SELECT * FROM \"playerData\" where (\"uuid\"='" + uuid + "');";
        try {
            List<PlayerDataBean> pdbList = new ArrayList<>();
            ResultSet rs = getDB().query(sql);
            while (rs.next()) {
                long tid = rs.getLong("id");
                String tuuid = rs.getString("uuid");
                String tpackageName = rs.getString("packageName");
                long texpire = rs.getLong("expire");
                PlayerDataBean tpd = new PlayerDataBean(tid, tuuid, tpackageName, texpire);
                pdbList.add(tpd);
            }
            return pdbList;
        } catch (Exception e) {
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "无法查询UUID: {0} 的数据", uuid));
            throw e;
        }
    }

    @Override
    public PlayerDataBean queryPlayerData(String uuid, String packageName) throws Exception {
        String sql = "SELECT * FROM \"playerData\" where (\"uuid\"='" + uuid + "' AND \"packageName\"='" + packageName + "');";
        try {
            ResultSet rs = getDB().query(sql);
            while (rs.next()) {
                long tid = rs.getLong("id");
                String tuuid = rs.getString("uuid");
                String tpackageName = rs.getString("packageName");
                long texpire = rs.getLong("expire");
                PlayerDataBean tpd = new PlayerDataBean(tid, tuuid, tpackageName, texpire);
                return tpd;
            }
            return null;
        } catch (Exception e) {
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "无法查询UUID: {0}, packageName: {1} 的数据", uuid, packageName));
            throw e;
        }
    }

    @Override
    public boolean delPlayData(String uuid) throws Exception {
        String sql = "DELETE FROM \"main\".\"playerData\" WHERE (\"uuid\"='" + uuid + "');";
        try {
            getDB().query(sql);
            return true;
        } catch (Exception e) {
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "无法删除UUID为: {0} 的数据", uuid));
            throw e;
        }
    }

    @Override
    public boolean delPlayData(String uuid, String packageName) throws Exception {
        String sql = "DELETE FROM \"main\".\"playerData\" WHERE (\"uuid\"='" + uuid + "' AND \"packageName\"='" + packageName + "');";
        try {
            getDB().query(sql);
            return true;
        } catch (Exception e) {
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "无法删除UUID: {0}, packageName: {1} 的数据", uuid, packageName));
            throw e;
        }
    }

}
