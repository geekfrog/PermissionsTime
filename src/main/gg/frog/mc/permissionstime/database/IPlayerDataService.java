package gg.frog.mc.permissionstime.database;

import java.sql.SQLException;
import java.util.List;

import gg.frog.mc.permissionstime.model.db.PlayerDataBean;

public interface IPlayerDataService {

    /**
     * 检查表是否
     * 
     * @return
     * @throws SQLException
     */
    boolean tableExist() throws Exception;

    /**
     * 创建用户数据表
     * 
     * @return
     * @throws SQLException
     */
    boolean creatTable() throws Exception;

    /**
     * 保存更新用户数据
     * 
     * @throws Exception
     */
    boolean setPlayerData(PlayerDataBean bean) throws Exception;
    boolean setTime(String uuid, String packageName, int days) throws Exception;
    boolean addTime(String uuid, String packageName, int days) throws Exception;

    /**
     * 删除
     */
    boolean delPlayData(String uuid) throws Exception;

    boolean delPlayData(String uuid, String packageName) throws Exception;

    /**
     * 查询
     * 
     * @return
     */
    List<PlayerDataBean> queryPlayerData(String uuid) throws Exception;
    PlayerDataBean queryPlayerData(String uuid, String packageName) throws Exception;
    List<PlayerDataBean> queryNotExpirePlayerData(String uuid) throws Exception;

}
