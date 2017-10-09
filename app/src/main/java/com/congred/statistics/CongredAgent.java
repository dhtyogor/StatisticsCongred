package com.congred.statistics;

import android.content.Context;
import android.util.Log;

import com.congred.statistics.bean.ClientUsingLogData;
import com.congred.statistics.bean.EventData;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by Tao on 2017/9/19.
 */

public class CongredAgent {

    private static WeakReference<Context> contextWR;
    private static boolean INIT = false;// init sdk
    private static UsinglogManager usinglogManager;

    public enum LogLevel {
        Info, // equals Log.INFO, for less important info
        Debug, // equals Log.DEBUG, for some debug information
        Warn, // equals Log.WARN, for some warning info
        Error, // equals Log.ERROR, for the exceptions errors
        Verbose // equals Log.VERBOSE, for the verbose info
    }

    /**
     * 数据发送模式 <br>
     * POST_ONSTART 下次启动发送 <br>
     * POST_NOW 实时发送 <br>
     * POST_INTERVAL 定时发送
     */
    public enum SendPolicy {
        POST_ONSTART, POST_NOW, POST_INTERVAL
    }

    /**
     * 初始化sdk
     *
     * @param context
     */
    public static void init(Context context,String baseUrl, String appkey) {
        if (appkey.length() == 0 || baseUrl.length() ==0) {
            CobubLog.e(UmsConstants.LOG_TAG, CongredAgent.class, "appkey and baseUrl are required");
            return;
        }
        UmsConstants.BASE_URL = baseUrl;
        updateContent(context);
        INIT = true;
        SharedPrefUtil sp = new SharedPrefUtil(contextWR.get());
        sp.setValue("app_key", appkey);
        init(context);
    }


    private static void updateContent(Context context) {
        CongredAgent.contextWR = new WeakReference<Context>(context);
        context = null;
    }

    private static void init(Context context) {
        updateContent(context);
        CongredAgent.postClientData();
        CobubLog.i(UmsConstants.LOG_TAG, CongredAgent.class, "Call init();BaseURL = " + UmsConstants.BASE_URL);
        SharedPrefUtil spu = new SharedPrefUtil(contextWR.get());
        spu.setValue("system_start_time", System.currentTimeMillis());
        // registerActivityCallback(context);
    }

    /**
     * 启动信息
     * **/
    static void postClientData() {
        ClientdataManager cm = new ClientdataManager(contextWR.get());
        cm.postClientData();
    }

    public static void onResume(Context context) {
        if (!INIT) {
            CobubLog.e(UmsConstants.LOG_TAG, CongredAgent.class, "sdk is not init!");
            return;
        }
        updateContent(context);
        CobubLog.i(UmsConstants.LOG_TAG, CongredAgent.class, "Call onResume()");
        if (usinglogManager == null)
            usinglogManager = new UsinglogManager(contextWR.get());
        usinglogManager.onResume(contextWR.get());
    }

    public static void onFragmentResume(Context context,final String PageName) {
        if (!INIT) {
            CobubLog.e(UmsConstants.LOG_TAG, CongredAgent.class, "sdk is not init!");
            return;
        }
        updateContent(context);

        CobubLog.i(UmsConstants.LOG_TAG, CongredAgent.class, "Call onFragmentResume()");
        if (usinglogManager == null)
            usinglogManager = new UsinglogManager(contextWR.get());
        usinglogManager.onFragmentResume(contextWR.get(), PageName);
    }

    public static void onPause(Context context) {
        if (!INIT) {
            CobubLog.e(UmsConstants.LOG_TAG, CongredAgent.class, "sdk is not init!");
            return;
        }
        updateContent(context);
        CobubLog.i(UmsConstants.LOG_TAG, CongredAgent.class, "Call onPause()");
        if (usinglogManager == null)
            usinglogManager = new UsinglogManager(contextWR.get());
        usinglogManager.onPause(contextWR.get());
    }


    /**
     * 发送webPage的名称
     *
     * @param pageName
     */
    public static void postWebPage(final String pageName) {
        if (!INIT) {
            CobubLog.e(UmsConstants.LOG_TAG, CongredAgent.class, "sdk is not init!");
            return;
        }
        CobubLog.i(UmsConstants.LOG_TAG, CongredAgent.class, "Call postWebPage()");
        if (usinglogManager == null)
            usinglogManager = new UsinglogManager(contextWR.get());
        usinglogManager.onWebPage(pageName, contextWR.get());
    }

    /**
     * 发送event事件数据
     *事件次数的统计，默认值为1次,调用该函数意味着 事件event_id 发生了一次
     * @param context
     */
    public static void onEvent(Context context, final String event_id) {
        if (!INIT) {
            CobubLog.e(UmsConstants.LOG_TAG, CongredAgent.class, "sdk is not init!");
            return;
        }
        updateContent(context);
        CobubLog.i(UmsConstants.LOG_TAG, CongredAgent.class, "Call onEvent(context,event_id)");
        onEvent(contextWR.get(), event_id, 1);


    }

    /**
     * 发送event事件数据
     * //事件统计，int参数为整形参数,调用该函数意味着事件event_id 发生了number次
     * @param context
     * @param event_id
     * @param acc
     */
    public static void onEvent(Context context, final String event_id, final int acc) {
        if (!INIT) {
            CobubLog.e(UmsConstants.LOG_TAG, CongredAgent.class, "sdk is not init!");
            return;
        }
        updateContent(context);
        CobubLog.i(UmsConstants.LOG_TAG, CongredAgent.class, "Call onEvent(event_id,acc)");
        onEvent(contextWR.get(), event_id, "", acc);
    }


    /**
     * 发送event事件数据
     *
     * @param event_id
     * @param label
     * @param acc
     */
    public static void onEvent(Context context, final String event_id,final String label, final int acc) {
        if (!INIT) {
            CobubLog.e(UmsConstants.LOG_TAG, CongredAgent.class, "sdk is not init!");
            return;
        }
        updateContent(context);
        if (event_id == null || event_id.length() == 0) {
            CobubLog.e(UmsConstants.LOG_TAG, CongredAgent.class, "Valid event_id is required");
        }
        if (acc < 1) {
            CobubLog.e(UmsConstants.LOG_TAG, CongredAgent.class, "Event acc should be greater than zero");
        }

        CobubLog.i(UmsConstants.LOG_TAG, CongredAgent.class, "Call onEvent(event_id,label,acc)");
        EventManager em = new EventManager(contextWR.get(), event_id, label,
                acc + "");
        em.postEventInfo();
    }


    /**
     * Call this function to send the catched exception stack information to
     * server 手动上传捕捉到的error信息
     *
     * @param context
     * @param errorType
     * @param errorinfo
     */
    public static void onError(Context context, final String errorType, final String errorinfo) {
        if (!INIT) {
            CobubLog.e(UmsConstants.LOG_TAG, CongredAgent.class, "sdk is not init!");
            return;
        }
        updateContent(context);
        final String error = errorType + "\n" + errorinfo;
        CobubLog.i(UmsConstants.LOG_TAG, CongredAgent.class, "Call onError(context,errorinfo)");
        ErrorManager em = new ErrorManager(contextWR.get());
        em.postErrorInfo(error,errorType);
    }

    /**
     * 获取配置信息
     * */
    public static void updateOnlineConfig(Context context) {
        if (!INIT) {
            CobubLog.e(UmsConstants.LOG_TAG, CongredAgent.class, "sdk is not init!");
            return;
        }
        updateContent(context);
        CobubLog.i(UmsConstants.LOG_TAG, CongredAgent.class, "Call updaeOnlineConfig");
        ConfigManager cm = new ConfigManager(contextWR.get());
        cm.updateOnlineConfig();
    }

    /**
     * Automatic Updates 检测是否更新应用
     *
     * @param context
     */
    public static void update(Context context) {
        if (!INIT) {
            CobubLog.e(UmsConstants.LOG_TAG, CongredAgent.class, "sdk is not init!");
            return;
        }
        updateContent(context);
        CobubLog.i(UmsConstants.LOG_TAG, CongredAgent.class, "Call update()");
        UpdateManager um = new UpdateManager(contextWR.get());
        um.postUpdate();
    }


    /**
     * Setting data transmission mode 设置数据发送模式
     *
     * @param context
     * @param sendPolicy {@link SendPolicy}
     */
    public static void setDefaultReportPolicy(Context context,SendPolicy sendPolicy) {
        updateContent(context);
        UmsConstants.mReportPolicy = sendPolicy;
        int type = 1;

        if (sendPolicy == SendPolicy.POST_ONSTART) {
            type = 0;
            getClienUsingLogData();
            getEventData();
        }
        if (sendPolicy == SendPolicy.POST_INTERVAL) {
            type = 2;
        }
        SharedPrefUtil spu = new SharedPrefUtil(contextWR.get());
        spu.setValue("DefaultReportPolicy", type);

        CobubLog.i(UmsConstants.LOG_TAG, CongredAgent.class, "setDefaultReportPolicy = " + String.valueOf(sendPolicy));

    }


    private static void getClienUsingLogData(){
        final ClienUsingLogDaoUtils clienUsingLogDaoUtils = new ClienUsingLogDaoUtils(contextWR.get());
        for (ClientUsingLogData clientUsingLogData : clienUsingLogDaoUtils.queryAllClientUsingLogDataData()) {
            final ClientUsingLogData clientUsingLogDataID =  clienUsingLogDaoUtils.queryClientUsingLogDataById(clientUsingLogData.getId());
            JSONObject jsonUsinglog = new JSONObject();
            try {
                jsonUsinglog.put("session_id", clientUsingLogDataID.getSession_id());//会话ID（客户端）
                jsonUsinglog.put("start_millis", clientUsingLogDataID.getStart_millis());//开始使用时间（格式：2017-05-25 16:00:04）
                jsonUsinglog.put("end_millis", clientUsingLogDataID.getEnd_millis());//结束使用时间（格式：2017-05-25 16:00:04）
                jsonUsinglog.put("duration", clientUsingLogDataID.getDuration());//持续时长（单位：秒）
                jsonUsinglog.put("activities", clientUsingLogDataID.getActivities());//页面名称
                jsonUsinglog.put("appkey", clientUsingLogDataID.getAppkey());
                jsonUsinglog.put("version", clientUsingLogDataID.getVersion());//版本
                jsonUsinglog.put("deviceid", clientUsingLogDataID.getDeviceid());//终端ID
                jsonUsinglog.put("useridentifier", clientUsingLogDataID.getUseridentifier());//用户编号
                jsonUsinglog.put("lib_version", clientUsingLogDataID.getLib_version());//SDK版本
                jsonUsinglog.put("insertdate", clientUsingLogDataID.getInsertdate());//更新时间（格式：2017-05-25 16:00:04）
                OkGo.<String>post(UmsConstants.BASE_URL + UmsConstants.USINGLOG_URL).upJson(jsonUsinglog).execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("xxx", "onSuccess:==页面信息db== "+response.body() );
                        ClientUsingLogData culd = new ClientUsingLogData();
                        culd.setId(clientUsingLogDataID.getId());
                        clienUsingLogDaoUtils.deleteClientUsingLogData(culd);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void getEventData(){
        final EventDaoUtils eventDaoUtils = new EventDaoUtils(contextWR.get());
        for (EventData evenData : eventDaoUtils.queryAllEventData()) {
            final EventData clientUsingLogDataID =  eventDaoUtils.queryEventDataById(evenData.getId());
            JSONObject localJSONObject = new JSONObject();
            try {
                localJSONObject.put("deviceid", clientUsingLogDataID.getDeviceid());//终端ID
                localJSONObject.put("event", clientUsingLogDataID.getEvent());//事件名称（标示）
                localJSONObject.put("label", clientUsingLogDataID.getLabel());//标签（分组标示）
                localJSONObject.put("clientdate", clientUsingLogDataID.getClientdate());//客户端日期（格式：2017-05-25 16:00:04）
                localJSONObject.put("productkey", clientUsingLogDataID.getProductkey());//产品密钥（同appkey）
                localJSONObject.put("num", clientUsingLogDataID.getNum());//事件发生次数
                localJSONObject.put("version", clientUsingLogDataID.getVersion());//版本
                localJSONObject.put("useridentifier", clientUsingLogDataID.getUseridentifier());//用户编号
                localJSONObject.put("session_id", clientUsingLogDataID.getSession_id());//会话ID（客户端）
                localJSONObject.put("lib_version", clientUsingLogDataID.getLib_version());//sdk版本
                localJSONObject.put("insertdate", clientUsingLogDataID.getInsertdate());//更新日期（格式：2017-05-25 16:00:04）
                OkGo.<String>post(UmsConstants.BASE_URL + UmsConstants.EVENT_URL).upJson(localJSONObject).execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("xxx", "onSuccess:==自定义事件>>== "+response.body() );
                        EventData ed = new EventData();
                        ed.setId(clientUsingLogDataID.getId());
                        eventDaoUtils.deleteEventData(ed);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
