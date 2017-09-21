package com.congred.statistics;

import android.content.Context;

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
        // 定时发送模式
        CobubLog.i(UmsConstants.LOG_TAG, CongredAgent.class, "Call onResume()");
        if (usinglogManager == null)
            usinglogManager = new UsinglogManager(contextWR.get());
        usinglogManager.onResume(contextWR.get());
    }

    public static void onFragmentResume(Context context,
                                        final String PageName) {
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


}
