package gg.frog.mc.permissionstime.model.db;

public class PlayerDataBean {

    private Integer id;
    private String uuid;
    private String packageName;
    private Integer expire;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
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
    public Integer getExpire() {
        return expire;
    }
    public void setExpire(Integer expire) {
        this.expire = expire;
    }
    
    
}
