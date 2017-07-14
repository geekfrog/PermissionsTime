package gg.frog.mc.permissionstime.database.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.IPlayerDataService;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.model.db.PlayerDataBean;
import gg.frog.mc.permissionstime.utils.StrUtil;
import gg.frog.mc.permissionstime.utils.database.DatabaseUtil;

public class MySQLPlayerDataService extends DatabaseUtil implements IPlayerDataService {

    private PluginMain pm;

    public MySQLPlayerDataService(PluginMain pm, SqlManager sm) {
        super(sm);
        this.pm = pm;
    }

    @Override
    public boolean tableExist() throws Exception {
        String sql = "SELECT count(*) AS num FROM information_schema.TABLES WHERE table_name ='" + PluginCfg.SQL_TABLE_PREFIX + "playerData';";
        try {
            ResultSet rs = getDB().query(sql);
            rs.next();
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
        String sql = "CREATE TABLE `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` ( `id` BIGINT NOT NULL AUTO_INCREMENT, `uuid` VARCHAR (255) NOT NULL, `packageName` VARCHAR (255) NOT NULL, `expire` BIGINT NOT NULL, PRIMARY KEY (`id`), UNIQUE INDEX `UUID_PACKAGE` (`uuid`, `packageName`));";
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
            sql = "UPDATE `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` SET `uuid`='" + pdb.getUuid() + "', `packageName`='" + pdb.getPackageName() + "', `expire`='" + pdb.getExpire() + "' WHERE (`id`='" + pdb.getId() + "');";
        } else {
            sql = "INSERT INTO `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` (`uuid`, `packageName`, `expire`) VALUES ('" + pdb.getUuid() + "', '" + pdb.getPackageName() + "', " + pdb.getExpire() + ");";
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
    public boolean setTime(String uuid, String packageName, int days) throws Exception {
        long now = new Date().getTime();
        long addTime = days * TIME_UNIT;
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
    public boolean addTime(String uuid, String packageName, int days) throws Exception {
        long now = new Date().getTime();
        long addTime = days * TIME_UNIT;
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
                String sql = "UPDATE `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` SET `expire`='" + addTime + "' WHERE (`id`='" + pdb.getId() + "');";
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
        String sql = "SELECT * FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` where (`uuid`='" + uuid + "');";
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
        String sql = "SELECT * FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` where (`uuid`='" + uuid + "' AND `packageName`='" + packageName + "');";
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
    public List<PlayerDataBean> queryNotExpirePlayerData(String uuid) throws Exception {
        long now = new Date().getTime();
        String sql = "SELECT * FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` where (`uuid`='" + uuid + "' AND `expire` > " + now + ");";
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
    public boolean delPlayData(String uuid) throws Exception {
        String sql = "DELETE FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` WHERE (`uuid`='" + uuid + "');";
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
        String sql = "DELETE FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` WHERE (`uuid`='" + uuid + "' AND `packageName`='" + packageName + "');";
        try {
            getDB().query(sql);
            return true;
        } catch (Exception e) {
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "无法删除UUID: {0}, packageName: {1} 的数据", uuid, packageName));
            throw e;
        }
    }

}