package gg.frog.mc.permissionstime.database.impl;

import java.sql.ResultSet;

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
        String sql = "SELECT count(*) AS num FROM \"main\".sqlite_master M where tbl_name='playerData';";
        try {
            ResultSet rs = getDB().query(sql);
            int num = rs.getInt("num");
            if (num == 1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX) + "无法检查有无数据表");
            throw e;
        }
    }

    @Override
    public boolean creatTable() throws Exception {
        String sql = "CREATE TABLE \"main\".\"playerData\" ( \"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \"uuid\" TEXT NOT NULL, \"packageName\" TEXT NOT NULL, \"expire\" INTEGER NOT NULL )";
        try {
            getDB().query(sql);
            return true;
        } catch (Exception e) {
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX) + "无法创建数据表");
            throw e;
        }
    }

    @Override
    public boolean saveOrUpdatePlayerData(PlayerDataBean bean) throws Exception {
        if(bean.getId()!=null){
            
        }else{
            
        }
        String sql = "CREATE TABLE \"main\".\"playerData\" ( \"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \"uuid\" TEXT NOT NULL, \"packageName\" TEXT NOT NULL, \"expire\" INTEGER NOT NULL )";
        try {
            getDB().query(sql);
            return true;
        } catch (Exception e) {
            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX) + "无法创建数据表");
            throw e;
        }
    }

    @Override
    public boolean delPlayData(String uuid) {
        return false;
        // TODO Auto-generated method stub

    }

    @Override
    public PlayerDataBean queryPlayerData(String uuid) {
        return null;
    }

}
