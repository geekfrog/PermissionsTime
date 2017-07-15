package gg.frog.mc.permissionstime.model.db;

public class PlayerDataBean {

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

    @Override
    public String toString() {
        return "PlayerDataBean [id=" + id + ", uuid=" + uuid + ", packageName=" + packageName + ", global=" + global + ", expire=" + expire + "]";
    }

}
