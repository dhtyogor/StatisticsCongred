package com.congred.statistics;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

import com.congred.statistics.bean.ClientUsingLogData;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

class UsinglogManager {
	private static WeakReference<Context> contextWR;
    private String session_id = "";

    public UsinglogManager(Context context) {
    	 contextWR = new WeakReference<Context>(context);
    }

    JSONObject prepareUsinglogJSON(String start_millis, String end_millis, String duration, String activities) throws JSONException {
        JSONObject jsonUsinglog = new JSONObject();
        if (session_id.equals("")) {
            session_id = CommonUtil.getSessionid(contextWR.get());
        }
        jsonUsinglog.put("session_id", session_id);//会话ID（客户端）
        jsonUsinglog.put("start_millis", start_millis);//开始使用时间（格式：2017-05-25 16:00:04）
        jsonUsinglog.put("end_millis", end_millis);//结束使用时间（格式：2017-05-25 16:00:04）
        jsonUsinglog.put("duration", Integer.parseInt(duration));//持续时长（单位：秒）
        jsonUsinglog.put("activities", activities);//页面名称
        jsonUsinglog.put("appkey", AppInfo.getAppKey(contextWR.get()));
        jsonUsinglog.put("version", AppInfo.getAppVersion(contextWR.get()));//版本
        jsonUsinglog.put("deviceid", DeviceInfo.getDeviceId());//终端ID
        jsonUsinglog.put("useridentifier", CommonUtil.getUserIdentifier(contextWR.get()));//用户编号
        jsonUsinglog.put("lib_version", UmsConstants.LIB_VERSION);//SDK版本
        jsonUsinglog.put("insertdate", end_millis);//更新时间（格式：2017-05-25 16:00:04）

        return jsonUsinglog;
    }

    /**
     * activity onResume
     * @param context
     */
    public void onResume(final Context context) {
        CommonUtil.savePageName(context, CommonUtil.getActivityName(context));
    }
    /**
     * fragment onResume
     * @param context
     * @param pageName
     */
    public void onFragmentResume(final Context context,String pageName) {
        CommonUtil.savePageName(context, pageName);
    }

    public void onPause(final Context context) {
        CobubLog.i(UmsConstants.LOG_TAG,UsinglogManager.class, "Call onPause()");

        SharedPrefUtil sp = new SharedPrefUtil(context);
        String pageName = sp.getValue("CurrentPage", CommonUtil.getActivityName(context));

        long start = sp.getValue("session_save_time",
                System.currentTimeMillis());
        String start_millis = CommonUtil.getFormatTime(start);

        long end = System.currentTimeMillis();
        String end_millis = CommonUtil.getFormatTime(end);

        String duration = end - start + "";
        Log.e("xxx", "onPause:///// "+duration );
        CommonUtil.saveSessionTime(context);

        JSONObject info;
        try {
            info = prepareUsinglogJSON(start_millis, end_millis, duration, pageName);
//            CommonUtil.saveInfoToFile("activityInfo", info, context);
            if(CommonUtil.getlocalDefaultReportPolicy(context) == 0){
                //db
                savaClienUsingLogDaoUtilsDB(start_millis, end_millis, duration, pageName);
            }else{
                OkGo.<String>post(UmsConstants.BASE_URL + UmsConstants.USINGLOG_URL).upJson(info).execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("xxx", "onSuccess:==页面信息== "+response.body() );
                    }
                });
            }
        } catch (JSONException e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
        }
    }

    public void onWebPage(String pageName,final Context context) {
        SharedPrefUtil sp = new SharedPrefUtil(context);
        String lastView = sp.getValue("CurrentWenPage", "");
        if (lastView.equals("")) {
            sp.setValue("CurrentWenPage", pageName);
            sp.setValue("session_save_time", System.currentTimeMillis());
        } else {
            long start = sp.getValue("session_save_time",
                    System.currentTimeMillis());
            String start_millis = CommonUtil.getFormatTime(start);

            long end = System.currentTimeMillis();
            String end_millis = CommonUtil.getFormatTime(end);

            String duration = end - start + "";

            sp.setValue("CurrentWenPage", pageName);
            sp.setValue("session_save_time", end);

            JSONObject obj;
            try {
                obj = prepareUsinglogJSON(start_millis, end_millis, duration,
                        lastView);
//                CommonUtil.saveInfoToFile("activityInfo", obj, context);
                OkGo.<String>post(UmsConstants.BASE_URL + UmsConstants.USINGLOG_URL).upJson(obj).execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("xxx", "onSuccess:==页面信息webView== "+response.body() );
                    }
                });
            } catch (JSONException e) {
                CobubLog.e(UmsConstants.LOG_TAG, e);
            }
        }
    }


    private  void savaClienUsingLogDaoUtilsDB(String start_millis, String end_millis, String duration, String activities) {
        ClienUsingLogDaoUtils clienUsingLogDaoUtils = new ClienUsingLogDaoUtils(contextWR.get());
        List<ClientUsingLogData> list = new ArrayList<>();
        ClientUsingLogData clientUsingLogData = new ClientUsingLogData();
        clientUsingLogData.setSession_id(session_id);//会话ID（客户端）
        clientUsingLogData.setStart_millis(start_millis);//开始使用时间（格式：2017-05-25 16:00:04）
        clientUsingLogData.setEnd_millis(end_millis);//结束使用时间（格式：2017-05-25 16:00:04）
        clientUsingLogData.setDuration(Integer.parseInt(duration));//持续时长（单位：秒）
        clientUsingLogData.setActivities(activities);//页面名称
        clientUsingLogData.setAppkey(AppInfo.getAppKey(contextWR.get()));
        clientUsingLogData.setVersion(AppInfo.getAppVersion(contextWR.get()));//版本
        clientUsingLogData.setDeviceid(DeviceInfo.getDeviceId());//终端ID
        clientUsingLogData.setUseridentifier(CommonUtil.getUserIdentifier(contextWR.get()));//用户编号
        clientUsingLogData.setLib_version(UmsConstants.LIB_VERSION);//SDK版本
        clientUsingLogData.setInsertdate(end_millis);//更新时间（格式：2017-05-25 16:00:04）
        list.add(clientUsingLogData);
        clienUsingLogDaoUtils.insertClientUsingLogData(list);
    }
}
