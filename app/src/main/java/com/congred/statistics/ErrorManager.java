/**
 * Cobub Razor
 * <p/>
 * An open source analytics android sdk for mobile applications
 *
 * @package Cobub Razor
 * @author WBTECH Dev Team
 * @copyright Copyright (c) 2011 - 2015, NanJing Western Bridge Co.,Ltd.
 * @license http://www.cobub.com/products/cobub-razor/license
 * @link http://www.cobub.com/products/cobub-razor/
 * @filesource
 * @since Version 0.1
 */
package com.congred.statistics;

import android.content.Context;
import android.util.Log;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import org.json.JSONException;
import org.json.JSONObject;

class ErrorManager {
    private Context context;

    public ErrorManager(Context context) {
        this.context = context;
    }

    private JSONObject prepareErrorJSON(String errorinfo,String errorType) {
        String activities = CommonUtil.getActivityName(context);

        String appkey = AppInfo.getAppKey(context);
        String os_version = DeviceInfo.getOsVersion();
        JSONObject errorInfo = new JSONObject();
        try {
            errorInfo.put("appkey", appkey);
            errorInfo.put("device", DeviceInfo.getDeviceName());//设备名称
            errorInfo.put("os_version", os_version);//操作系统版本
            errorInfo.put("activity", activities);//页面名称
            errorInfo.put("time", DeviceInfo.getDeviceTime());//客户端时间（格式：2017-05-25 16:00:04）
            errorInfo.put("title", errorType);//错误摘要
            errorInfo.put("stacktrace", errorinfo);//堆栈追踪
            errorInfo.put("version", AppInfo.getAppVersion(context));//版本
            errorInfo.put("isfix", 1);//是否修复(1是，0否)
            errorInfo.put("session_id", CommonUtil.getSessionid(context));//会话ID（客户端）
            errorInfo.put("useridentifier", CommonUtil.getUserIdentifier(context));//用户编号
            errorInfo.put("lib_version", UmsConstants.LIB_VERSION);//sdk版本
            errorInfo.put("deviceid", DeviceInfo.getDeviceId());//终端ID
            errorInfo.put("insertdate", DeviceInfo.getDeviceTime());//更新日期（格式：2017-05-25 16:00:04）

        } catch (JSONException e) {
            CobubLog.e(UmsConstants.LOG_TAG, ErrorManager.class, e.toString());
            return null;
        }
        return errorInfo;
    }

    public void postErrorInfo(String error,String errorType) {
        try {
            JSONObject errorJSON = prepareErrorJSON(error,errorType);
//            JSONObject postdata = new JSONObject();
//            postdata.put("data", new JSONArray().put(errorJSON));
            Log.e("xxx", "onSuccess:==错误>== "+errorJSON );
            OkGo.<String>post(UmsConstants.BASE_URL + UmsConstants.ERROR_URL).upJson(errorJSON).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    Log.e("xxx", "onSuccess:==错误== "+response.body() );
                }
            });
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
            return;
        }
    }
}
