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

public class MySQLPlayerDataDao extends DatabaseUtil implements IPlayerDataDao {

	private PluginMain pm;

	public MySQLPlayerDataDao(PluginMain pm, SqlManager sm) {
		super(sm);
		this.pm = pm;
	}

	@Override
	public boolean tableExist() throws Exception {
		String sql = "SELECT count(*) AS num FROM information_schema.TABLES WHERE table_name ='"
				+ PluginCfg.SQL_TABLE_PREFIX + "playerData';";
		try {
			ResultSet rs = getDB().query(sql);
			rs.next();
			int num = rs.getInt("num");
			if (num == 1) {
				return true;
			}
			return false;
		} catch (Exception e) {
			pm.getServer().getConsoleSender()
					.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't check table is exist."));
			throw e;
		}
	}

	@Override
	public boolean creatTable() throws Exception {
		String sql = "CREATE TABLE `" + PluginCfg.SQL_TABLE_PREFIX
				+ "playerData` ( `id` BIGINT NOT NULL AUTO_INCREMENT, `uuid` VARCHAR (36) NOT NULL, `packageName` VARCHAR (100) NOT NULL, `serverId` VARCHAR (100), `expire` BIGINT NOT NULL, PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8; ";
		try {
			getDB().query(sql);
			return true;
		} catch (Exception e) {
			pm.getServer().getConsoleSender()
					.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't creat table."));
			throw e;
		}
	}

	@Override
	public boolean setPlayerData(PlayerDataBean bean) throws Exception {
		PlayerDataBean pdb = bean;
		String sql;
		if (pdb.getId() != null) {
			sql = "UPDATE `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` SET `uuid`='" + pdb.getUuid()
					+ "', `packageName`='" + pdb.getPackageName() + "', `expire`='" + pdb.getExpire()
					+ "' WHERE (`id`='" + pdb.getId() + "');";
		} else {
			if (bean.getGlobal()) {
				sql = "INSERT INTO `" + PluginCfg.SQL_TABLE_PREFIX
						+ "playerData` (`uuid`, `packageName`, `serverId`, `expire`) VALUES ('" + pdb.getUuid() + "', '"
						+ pdb.getPackageName() + "', NULL, " + pdb.getExpire() + ");";
			} else {
				sql = "INSERT INTO `" + PluginCfg.SQL_TABLE_PREFIX
						+ "playerData` (`uuid`, `packageName`, `serverId`, `expire`) VALUES ('" + pdb.getUuid() + "', '"
						+ pdb.getPackageName() + "', '" + PluginCfg.SQL_SERVER_ID + "', " + pdb.getExpire() + ");";
			}
		}
		try {
			getDB().query(sql);
			return true;
		} catch (Exception e) {
			pm.getServer().getConsoleSender()
					.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't creat data: {0}", pdb));
			throw e;
		}
	}

	@Override
	public boolean setTime(String uuid, String packageName, int time) throws Exception {
		boolean global = uuid.startsWith("g:") ? true : false;
		if (global) {
			uuid = uuid.substring(2);
		}
		long now = new Date().getTime();
		long addTime = time * TIME_UNIT;
		long expire = now + addTime;
		PlayerDataBean pdb = queryPlayerData((global ? "g:" : "") + uuid, packageName);
		if (pdb == null) {
			pdb = new PlayerDataBean(null, uuid, packageName, expire);
			if (global) {
				pdb.setGlobal(true);
			}
			return setPlayerData(pdb);
		} else {
			pdb.setExpire(expire);
			if (global) {
				pdb.setGlobal(true);
			}
			return setPlayerData(pdb);
		}
	}

	@Override
	public boolean addTime(String uuid, String packageName, int time) throws Exception {
		boolean global = uuid.startsWith("g:") ? true : false;
		if (global) {
			uuid = uuid.substring(2);
		}
		long now = new Date().getTime();
		long addTime = time * TIME_UNIT;
		long expire = now + addTime;
		PlayerDataBean pdb = queryPlayerData((global ? "g:" : "") + uuid, packageName);
		if (pdb == null) {
			pdb = new PlayerDataBean(null, uuid, packageName, expire);
			if (global) {
				pdb.setGlobal(true);
			}
			return setPlayerData(pdb);
		} else {
			if (pdb.getExpire() < now) {
				pdb.setExpire(expire);
				if (global) {
					pdb.setGlobal(true);
				}
				return setPlayerData(pdb);
			} else {
				String sql = "UPDATE `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` SET `expire`=`expire`+" + addTime
						+ " WHERE (`id`='" + pdb.getId() + "');";
				try {
					getDB().query(sql);
					return true;
				} catch (Exception e) {
					pm.getServer().getConsoleSender().sendMessage(
							StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't update data: {0}", pdb));
					throw e;
				}
			}
		}
	}

	@Override
	public List<PlayerDataBean> queryPlayerData(String uuid) throws Exception {
		String sql = "SELECT * FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` where `uuid`='" + uuid
				+ "' AND (`serverId`='" + PluginCfg.SQL_SERVER_ID + "' OR `serverId` IS NULL);";
		try {
			List<PlayerDataBean> pdbList = new ArrayList<>();
			ResultSet rs = getDB().query(sql);
			while (rs.next()) {
				long tid = rs.getLong("id");
				String tuuid = rs.getString("uuid");
				String tpackageName = rs.getString("packageName");
				String tserverId = rs.getString("serverId");
				long texpire = rs.getLong("expire");
				PlayerDataBean tpd = new PlayerDataBean(tid, tuuid, tpackageName, texpire);
				if (tserverId == null) {
					tpd.setGlobal(true);
				}
				pdbList.add(tpd);
			}
			return pdbList;
		} catch (Exception e) {
			pm.getServer().getConsoleSender().sendMessage(
					StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't find data by UUID: {0}", uuid));
			throw e;
		}
	}

	@Override
	public PlayerDataBean queryPlayerData(String uuid, String packageName) throws Exception {
		String sql = "SELECT * FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` where `uuid`='" + uuid
				+ "' AND `packageName`='" + packageName + "' AND `serverId`='" + PluginCfg.SQL_SERVER_ID + "';";
		if (uuid.startsWith("g:")) {
			uuid = uuid.substring(2);
			sql = "SELECT * FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` where `uuid`='" + uuid
					+ "' AND `packageName`='" + packageName + "' AND `serverId` IS NULL;";
		}
		try {
			ResultSet rs = getDB().query(sql);
			while (rs.next()) {
				long tid = rs.getLong("id");
				String tuuid = rs.getString("uuid");
				String tpackageName = rs.getString("packageName");
				String tserverId = rs.getString("serverId");
				long texpire = rs.getLong("expire");
				PlayerDataBean tpd = new PlayerDataBean(tid, tuuid, tpackageName, texpire);
				if (tserverId == null) {
					tpd.setGlobal(true);
				}
				return tpd;
			}
			return null;
		} catch (Exception e) {
			pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(
					PluginCfg.PLUGIN_PREFIX + "§4Can't find data by UUID: {0}, packageName: {1}", uuid, packageName));
			throw e;
		}
	}

	@Override
	public List<PlayerDataBean> queryNotExpirePlayerData(String uuid) throws Exception {
		long now = new Date().getTime();
		String sql = "SELECT * FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` where `uuid`='" + uuid
				+ "' AND (`serverId`='" + PluginCfg.SQL_SERVER_ID + "' OR `serverId` IS NULL) AND `expire` > " + now
				+ ";";
		try {
			List<PlayerDataBean> pdbList = new ArrayList<>();
			ResultSet rs = getDB().query(sql);
			while (rs.next()) {
				String tpackageName = rs.getString("packageName");
				if (PackagesCfg.PACKAGES.containsKey(tpackageName)) {
					long tid = rs.getLong("id");
					String tuuid = rs.getString("uuid");
					String tserverId = rs.getString("serverId");
					long texpire = rs.getLong("expire");
					PlayerDataBean tpd = new PlayerDataBean(tid, tuuid, tpackageName, texpire);
					if (tserverId == null) {
						tpd.setGlobal(true);
					}
					pdbList.add(tpd);
				}
			}
			return pdbList;
		} catch (Exception e) {
			pm.getServer().getConsoleSender().sendMessage(
					StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't find data by UUID: {0}", uuid));
			throw e;
		}
	}

	@Override
	public boolean delPlayData(Long id) throws Exception {
		String sql = "DELETE FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` WHERE `id`='" + id
				+ "' AND (`serverId`='" + PluginCfg.SQL_SERVER_ID + "' OR `serverId` IS NULL);";
		try {
			getDB().query(sql);
			return true;
		} catch (Exception e) {
			pm.getServer().getConsoleSender()
					.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't delete data by ID: {0}", id));
			throw e;
		}
	}

	@Override
	public boolean delPlayData(String uuid) throws Exception {
		String sql = "DELETE FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` WHERE `uuid`='" + uuid
				+ "' AND `serverId`='" + PluginCfg.SQL_SERVER_ID + "';";
		if (uuid.startsWith("g:")) {
			uuid = uuid.substring(2);
			sql = "DELETE FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` WHERE `uuid`='" + uuid
					+ "' AND `serverId` IS NULL;";
		}
		try {
			getDB().query(sql);
			return true;
		} catch (Exception e) {
			pm.getServer().getConsoleSender().sendMessage(
					StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "§4Can't delete data by UUID: {0}", uuid));
			throw e;
		}
	}

	@Override
	public boolean delPlayData(String uuid, String packageName) throws Exception {
		String sql = "DELETE FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` WHERE `uuid`='" + uuid
				+ "' AND `packageName`='" + packageName + "'AND `serverId`='" + PluginCfg.SQL_SERVER_ID + "';";
		if (uuid.startsWith("g:")) {
			uuid = uuid.substring(2);
			sql = "DELETE FROM `" + PluginCfg.SQL_TABLE_PREFIX + "playerData` WHERE `uuid`='" + uuid
					+ "' AND `packageName`='" + packageName + "'AND `serverId` IS NULL;";
		}
		try {
			getDB().query(sql);
			return true;
		} catch (Exception e) {
			pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(
					PluginCfg.PLUGIN_PREFIX + "§4Can't delete data by UUID: {0}, packageName: {1}", uuid, packageName));
			throw e;
		}
	}

}
