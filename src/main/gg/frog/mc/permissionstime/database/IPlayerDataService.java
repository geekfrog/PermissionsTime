package gg.frog.mc.permissionstime.database;

import java.sql.SQLException;

import gg.frog.mc.permissionstime.model.db.PlayerDataBean;

public interface IPlayerDataService {
    
    /**
     * 检查表是否 
     * @return
     * @throws SQLException 
     */
    boolean tableExist() throws Exception;

    /**
     * 创建用户数据表
     * @return 
     * @throws SQLException 
     */
    boolean creatTable() throws Exception;
    
    /**
     * 保存更新用户数据
     * @throws Exception 
     */
    boolean saveOrUpdatePlayerData(PlayerDataBean bean) throws Exception;
    
    /**
     * 删除
     */
    boolean delPlayData(String uuid);
    
    /**
     * 查询
     * @return 
     */
    PlayerDataBean queryPlayerData(String uuid) throws Exception;
}
