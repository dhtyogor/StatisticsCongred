package com.congred.statistics;

import android.content.Context;

import com.congred.statistics.bean.ClientUsingLogData;
import com.congred.statistics.bean.EventData;

import java.util.List;

/**
 * Created by Tao on 2017/9/25.
 */

public class EventDaoUtils {

    private DaoManager mManager;

    public EventDaoUtils(Context context) {
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 插入多条数据，在子线程操作
     * @param clientUsingLogDataList
     * @return
     */
    public boolean insertEventData(final List<EventData> clientUsingLogDataList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (EventData clientUsingLogData : clientUsingLogDataList) {
                        mManager.getDaoSession().insertOrReplace(clientUsingLogData);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 查询所有记录
     * @return
     */
    public List<EventData> queryAllEventData(){
        return mManager.getDaoSession().loadAll(EventData.class);
    }


    public EventData queryEventDataById(long id){
        return mManager.getDaoSession().load(EventData.class, id);
    }

    /**
     * 删除单条记录
     * @param eventData
     * @return
     */
    public boolean deleteEventData(EventData eventData){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(eventData);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

}
