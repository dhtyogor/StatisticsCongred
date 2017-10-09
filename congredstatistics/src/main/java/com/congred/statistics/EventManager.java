package com.congred.statistics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.congred.statistics.bean.ClientUsingLogData;
import com.congred.statistics.bean.EventData;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import org.json.JSONException;
import org.json.JSONObject;

class EventManager {
    private Context context;
    private String eventid;
    private String label;
    private String acc;
    private String json = "";

    public EventManager(Context context, String eventid, String label,
                        String acc) {
        this.context = context;
        this.eventid = eventid;
        this.label = label;
        this.acc = acc;
    }

    public EventManager(Context context, String eventid, String label,
                        String acc, String json) {
        this.context = context;
        this.eventid = eventid;
        this.label = label;
        this.acc = acc;
        this.json = json;
    }

    private JSONObject prepareEventJSON() {
        JSONObject localJSONObject = new JSONObject();
        try {
            localJSONObject.put("deviceid", DeviceInfo.getDeviceId());//终端ID
            localJSONObject.put("event", eventid);//事件名称（标示）
            localJSONObject.put("label", label);//标签（分组标示）
            localJSONObject.put("clientdate", DeviceInfo.getDeviceTime());//客户端日期（格式：2017-05-25 16:00:04）
            localJSONObject.put("productkey", AppInfo.getAppKey(context));//产品密钥（同appkey）
            localJSONObject.put("num", Integer.parseInt(acc));//事件发生次数
            localJSONObject.put("version", AppInfo.getAppVersion(context));//版本
            localJSONObject.put("useridentifier", CommonUtil.getUserIdentifier(context));//用户编号
            localJSONObject.put("session_id", CommonUtil.getSessionid(context));//会话ID（客户端）
            localJSONObject.put("lib_version", UmsConstants.LIB_VERSION);//sdk版本
            localJSONObject.put("insertdate", DeviceInfo.getDeviceTime());//更新日期（格式：2017-05-25 16:00:04）

        } catch (JSONException e) {
            CobubLog.e(UmsConstants.LOG_TAG, EventManager.class, e.toString());
        }
        return localJSONObject;
    }

    public void postEventInfo() {
        try {
            if(CommonUtil.getlocalDefaultReportPolicy(context) == 0){
                //db
                saveEventDB();
            }else{
                JSONObject localJSONObject = prepareEventJSON();
                OkGo.<String>post(UmsConstants.BASE_URL + UmsConstants.EVENT_URL).upJson(localJSONObject).execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("xxx", "onSuccess:==自定义事件== "+response.body() );
                    }
                });
            }
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, EventManager.class, e.toString());
            return;
        }
    }

    private void saveEventDB(){
        EventDaoUtils eventDaoUtils = new EventDaoUtils(context);
        List<EventData> list = new ArrayList<>();
        EventData clientUsingLogData = new EventData();
        clientUsingLogData.setDeviceid(DeviceInfo.getDeviceId());//终端ID
        clientUsingLogData.setEvent(eventid);//事件名称（标示）
        clientUsingLogData.setLabel(label);//标签（分组标示）
        clientUsingLogData.setClientdate(DeviceInfo.getDeviceTime());//客户端日期（格式：2017-05-25 16:00:04）
        clientUsingLogData.setProductkey(AppInfo.getAppKey(context));//产品密钥（同appkey）
        clientUsingLogData.setNum(Integer.parseInt(acc));//事件发生次数
        clientUsingLogData.setVersion(AppInfo.getAppVersion(context));//版本
        clientUsingLogData.setUseridentifier(CommonUtil.getUserIdentifier(context));//用户编号
        clientUsingLogData.setSession_id(CommonUtil.getSessionid(context));//会话ID（客户端）
        clientUsingLogData.setLib_version(UmsConstants.LIB_VERSION);//sdk版本
        clientUsingLogData.setInsertdate(DeviceInfo.getDeviceTime());//更新日期（格式：2017-05-25 16:00:04）
        list.add(clientUsingLogData);
        eventDaoUtils.insertEventData(list);
    }


}
