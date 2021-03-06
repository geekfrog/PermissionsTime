package gg.frog.mc.permissionstime.database.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.base.utils.StrUtil;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.DatabaseUtil;
import gg.frog.mc.permissionstime.database.IPlayerDataDao;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;

public class SqlitePlayerDataDao extends DatabaseUtil implements IPlayerDataDao {

    private PluginMain pm;

    public SqlitePlayerDataDao(PluginMain pm, SqlManager sm) {
        super(sm);
        this.pm = pm;
    }

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
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't check table is exist."));
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
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't creat table."));
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
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't creat data: {0}", pdb));
            throw e;
        }
    }

    @Override
    public boolean setTime(String uuid, String packageName, int time) throws Exception {
        long now = new Date().getTime();
        long addTime = time * TIME_UNIT;
        long expire = now + addTime;
        PlayerDataBean pdb = queryPlayerData(uuid, packageName);
        if (pdb == null) {
            pdb = new PlayerDataBean(null, uuid, packageName, expire);
            return setPlayerData(pdb);
        } else {
            pdb.setExpire(expire);
            return setPlayerData(pdb);
        }
    }

    @Override
    public boolean addTime(String uuid, String packageName, int time) throws Exception {
        long now = new Date().getTime();
        long addTime = time * TIME_UNIT;
        long expire = now + addTime;
        PlayerDataBean pdb = queryPlayerData(uuid, packageName);
        if (pdb == null) {
            pdb = new PlayerDataBean(null, uuid, packageName, expire);
            return setPlayerData(pdb);
        } else {
            if (pdb.getExpire() < now) {
                pdb.setExpire(expire);
                return setPlayerData(pdb);
            } else {
                String sql = "UPDATE \"main\".\"playerData\" SET \"expire\"=\"expire\"+" + addTime + " WHERE (\"id\"=" + pdb.getId() + ");";
                try {
                    getDB().query(sql);
                    return true;
                } catch (Exception e) {
                    pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't update data: {0}", pdb));
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
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't find data by UUID: {0}", uuid));
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
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't find data by UUID: {0}, packageName: {1}", uuid, packageName));
            throw e;
        }
    }

    @Override
    public List<PlayerDataBean> queryNotExpirePlayerData(String uuid) throws Exception {
        long now = new Date().getTime();
        String sql = "SELECT * FROM \"playerData\" where (\"uuid\"='" + uuid + "' AND \"expire\" > " + now + ");";
        try {
            List<PlayerDataBean> pdbList = new ArrayList<>();
            ResultSet rs = getDB().query(sql);
            while (rs.next()) {
                String tpackageName = rs.getString("packageName");
                if (PackagesCfg.PACKAGES.containsKey(tpackageName)) {
                    long tid = rs.getLong("id");
                    String tuuid = rs.getString("uuid");
                    long texpire = rs.getLong("expire");
                    PlayerDataBean tpd = new PlayerDataBean(tid, tuuid, tpackageName, texpire);
                    pdbList.add(tpd);
                }
            }
            return pdbList;
        } catch (Exception e) {
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't find data by UUID: {0}", uuid));
            throw e;
        }
    }

    @Override
    public boolean delPlayData(Long id) throws Exception {
        String sql = "DELETE FROM \"main\".\"playerData\" WHERE (\"id\"='" + id + "');";
        try {
            getDB().query(sql);
            return true;
        } catch (Exception e) {
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't delete data by ID: {0}", id));
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
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't delete data by UUID: {0}", uuid));
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
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't delete data by UUID: {0}, packageName: {1}", uuid, packageName));
            throw e;
        }
    }

}
