package com.congred.statistics.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Tao on 2017/9/25.
 */
@Entity
public class ClientUsingLogData {

    @Id
    private Long id;
    private String session_id;
    private String start_millis;
    private String end_millis;
    private int duration;
    private String activities;
    private String appkey;
    private String version;
    private String deviceid;
    private String useridentifier;
    private String lib_version;
    private String insertdate;
    @Generated(hash = 653102890)
    public ClientUsingLogData(Long id, String session_id, String start_millis,
            String end_millis, int duration, String activities, String appkey,
            String version, String deviceid, String useridentifier,
            String lib_version, String insertdate) {
        this.id = id;
        this.session_id = session_id;
        this.start_millis = start_millis;
        this.end_millis = end_millis;
        this.duration = duration;
        this.activities = activities;
        this.appkey = appkey;
        this.version = version;
        this.deviceid = deviceid;
        this.useridentifier = useridentifier;
        this.lib_version = lib_version;
        this.insertdate = insertdate;
    }
    @Generated(hash = 570798808)
    public ClientUsingLogData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSession_id() {
        return this.session_id;
    }
    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
    public String getStart_millis() {
        return this.start_millis;
    }
    public void setStart_millis(String start_millis) {
        this.start_millis = start_millis;
    }
    public String getEnd_millis() {
        return this.end_millis;
    }
    public void setEnd_millis(String end_millis) {
        this.end_millis = end_millis;
    }
    public int getDuration() {
        return this.duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public String getActivities() {
        return this.activities;
    }
    public void setActivities(String activities) {
        this.activities = activities;
    }
    public String getAppkey() {
        return this.appkey;
    }
    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }
    public String getVersion() {
        return this.version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getDeviceid() {
        return this.deviceid;
    }
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
    public String getUseridentifier() {
        return this.useridentifier;
    }
    public void setUseridentifier(String useridentifier) {
        this.useridentifier = useridentifier;
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
