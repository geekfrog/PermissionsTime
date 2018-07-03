package gg.frog.mc.permissionstime.model.db;

import java.util.Date;

import gg.frog.mc.base.utils.StrUtil;
import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.database.IPlayerDataDao;

public class PlayerDataBean {

	private static final long dt = 24 * 60 * IPlayerDataDao.TIME_UNIT;
	private static final long ht = 60 * IPlayerDataDao.TIME_UNIT;
	private static final long mt = IPlayerDataDao.TIME_UNIT;

	private Long id;
	private String uuid;
	private String packageName;
	private Boolean global = false;
	private Long expire;

	public PlayerDataBean() {
		super();
	}

	public PlayerDataBean(Long id, String uuid, String packageName, Long expire) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.packageName = packageName;
		this.expire = expire;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Boolean getGlobal() {
		return global;
	}

	public void setGlobal(Boolean global) {
		this.global = global;
	}

	public Long getExpire() {
		return expire;
	}

	public void setExpire(Long expire) {
		this.expire = expire;
	}

	public static String getLeftTime(long time) {
		long leftTime = time - new Date().getTime();
		long d = leftTime / dt;
		long h = (leftTime % dt) / ht;
		long m = (leftTime % ht) / mt;
		return StrUtil.messageFormat(LangCfg.LEFT_TIME, d, LangCfg.TIME_UNIT_D, h, LangCfg.TIME_UNIT_H, m, LangCfg.TIME_UNIT_M);
	}

	@Override
	public String toString() {
		return "PlayerDataBean [id=" + id + ", uuid=" + uuid + ", packageName=" + packageName + ", global=" + global + ", expire=" + expire + "]";
	}

}
