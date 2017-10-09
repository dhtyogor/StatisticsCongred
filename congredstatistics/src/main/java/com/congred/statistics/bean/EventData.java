package com.congred.statistics.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Tao on 2017/9/25.
 */
@Entity
public class EventData {
    @Id
    private Long id;
    private String deviceid;
    private String event;
    private String label;
    private String clientdate;
    private String productkey;
    private int  num;
    private String version;
    private String useridentifier;
    private String session_id;
    private String lib_version;
    private String insertdate;
    @Generated(hash = 1657906941)
    public EventData(Long id, String deviceid, String event, String label,
            String clientdate, String productkey, int num, String version,
            String useridentifier, String session_id, String lib_version,
            String insertdate) {
        this.id = id;
        this.deviceid = deviceid;
        this.event = event;
        this.label = label;
        this.clientdate = clientdate;
        this.productkey = productkey;
        this.num = num;
        this.version = version;
        this.useridentifier = useridentifier;
        this.session_id = session_id;
        this.lib_version = lib_version;
        this.insertdate = insertdate;
    }
    @Generated(hash = 2076277765)
    public EventData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDeviceid() {
        return this.deviceid;
    }
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
    public String getEvent() {
        return this.event;
    }
    public void setEvent(String event) {
        this.event = event;
    }
    public String getLabel() {
        return this.label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getClientdate() {
        return this.clientdate;
    }
    public void setClientdate(String clientdate) {
        this.clientdate = clientdate;
    }
    public String getProductkey() {
        return this.productkey;
    }
    public void setProductkey(String productkey) {
        this.productkey = productkey;
    }
    public int getNum() {
        return this.num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public String getVersion() {
        return this.version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getUseridentifier() {
        return this.useridentifier;
    }
    public void setUseridentifier(String useridentifier) {
        this.useridentifier = useridentifier;
    }
    public String getSession_id() {
        return this.session_id;
    }
    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
    public String getLib_version() {
        return this.lib_version;
    }
    public void setLib_version(String lib_version) {
        this.lib_version = lib_version;
    }
    public String getInsertdate() {
        return this.insertdate;
    }
    public void setInsertdate(String insertdate) {
        this.insertdate = insertdate;
    }


}
