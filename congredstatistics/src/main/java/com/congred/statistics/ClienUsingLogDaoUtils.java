package com.congred.statistics;

import android.content.Context;
import com.congred.statistics.bean.ClientUsingLogData;
import java.util.List;

/**
 * Created by Tao on 2017/9/25.
 */

public class ClienUsingLogDaoUtils {

    private DaoManager mManager;

    public ClienUsingLogDaoUtils(Context context) {
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 插入多条数据，在子线程操作
     * @param clientUsingLogDataList
     * @return
     */
    public boolean insertClientUsingLogData(final List<ClientUsingLogData> clientUsingLogDataList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (ClientUsingLogData clientUsingLogData : clientUsingLogDataList) {
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
    public List<ClientUsingLogData> queryAllClientUsingLogDataData(){
        return mManager.getDaoSession().loadAll(ClientUsingLogData.class);
    }


    public ClientUsingLogData queryClientUsingLogDataById(long id){
        return mManager.getDaoSession().load(ClientUsingLogData.class, id);
    }


    /**
     * 删除单条记录
     * @param photo
     * @return
     */
    public boolean deleteClientUsingLogData(ClientUsingLogData photo){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(photo);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }





}
