package com.congred.statistics;

import android.app.Application;

/**
 * Created by Tao on 2017/9/25.
 */

public class APP extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //1.初始化
        String appkey = "d3201a626c43cb898b53ac04c625422d";
        CongredAgent.init(this,"http://arcs.dev.congred.com/app/appstat",appkey);
        //2.参数配置
        //2.1 本地配置
        CongredAgent.setDefaultReportPolicy(this, CongredAgent.SendPolicy.POST_ONSTART);  //实时发送
        //2.2 在线配置
        CongredAgent.updateOnlineConfig(this);
    }

}
