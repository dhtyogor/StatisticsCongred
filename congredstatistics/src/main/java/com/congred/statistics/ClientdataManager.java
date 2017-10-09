package com.congred.statistics;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;


class ClientdataManager {
    private Context context;
    private final String PLATFORM = "android";

    public ClientdataManager(Context context) {
        this.context = context;
        DeviceInfo.init(context);
    }

    JSONObject prepareClientdataJSON() throws JSONException {
        JSONObject jsonClientdata = new JSONObject();
        jsonClientdata.put("version", AppInfo.getAppVersion(context));//版本
        jsonClientdata.put("platform", PLATFORM);//平台
        jsonClientdata.put("osversion", DeviceInfo.getOsVersion());//os_version 操作系统版本号
        jsonClientdata.put("language", DeviceInfo.getLanguage());//语言（默认:ZH）
        jsonClientdata.put("resolution", DeviceInfo.getResolution());//分辨率
        jsonClientdata.put("devicename", DeviceInfo.getDeviceName());//终端名称
        jsonClientdata.put("ismobiledevice", 1);//是否为移动设备(1是，0否)
        jsonClientdata.put("deviceid", DeviceInfo.getDeviceId());//终端id
        jsonClientdata.put("modulename", DeviceInfo.getDeviceProduct());//机型名称
        jsonClientdata.put("imei", DeviceInfo.getDeviceIMEI());//设备识别码
        jsonClientdata.put("imsi", DeviceInfo.getIMSI());//用户识别码 //
        jsonClientdata.put("havegps", 1);//是否支持导航(1是，0否) // DeviceInfo.getGPSAvailable()
        jsonClientdata.put("havebt", 1);//是否支持蓝牙(1是，0否) //DeviceInfo.getBluetoothAvailable()
        jsonClientdata.put("havewifi", 1);//是否支持WiFi(1是，0否) //DeviceInfo.getWiFiAvailable()
        jsonClientdata.put("havegravity", 1);//是否支持重力感应(1是，0否) //DeviceInfo.getGravityAvailable()
        jsonClientdata.put("wifimac", DeviceInfo.getWifiMac());//WiFi设备名
        jsonClientdata.put("latitude", DeviceInfo.getLatitude());//纬度 DeviceInfo.getLatitude()
        jsonClientdata.put("longitude", DeviceInfo.getLongitude());//经度
        jsonClientdata.put("date", DeviceInfo.getDeviceTime());//time //客户端时间（格式：2017-05-25 16:00:04）
        jsonClientdata.put("clientip", DeviceInfo.getIp());//客户端IP
        jsonClientdata.put("productkey", AppInfo.getAppKey(context));//产品密钥（同appkey）
        jsonClientdata.put("service_supplier", "中国移动");
        jsonClientdata.put("network", DeviceInfo.getNetworkTypeWIFI2G3G());//联网方式（2G|3G|4G|WIFI）
        jsonClientdata.put("isjailbroken", 0);//是否破解(1是，0否)
        jsonClientdata.put("useridentifier", CommonUtil.getUserIdentifier(context));//用户编号
        jsonClientdata.put("session_id", CommonUtil.getSessionid(context));//会话ID（客户端）
        jsonClientdata.put("lib_version", UmsConstants.LIB_VERSION);//SDK版本

        return jsonClientdata;
    }

    
    
    public void postClientData() {
        try {
            final JSONObject clientData = prepareClientdataJSON();
            Log.e("xxx", "postClientData: "+clientData );
            if (CommonUtil.isNetworkAvailable(context)) {
                OkGo.<String>post(UmsConstants.BASE_URL + UmsConstants.CLIENTDATA_URL).upJson(clientData).execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        CommonUtil.saveInfoToFile("clientData", clientData, context);
                        Log.e("xxx", "onSuccess:==启动信息== "+response.body() );
                    }
                });
            }
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
            return;
        }
    }
}
