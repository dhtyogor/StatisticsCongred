package com.congred.statistics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

class UpdateManager {
   
    private static String force;
    private static ProgressDialog progressDialog; 
    private static Dialog noticeDialog;    
    private String saveFile = null;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static int progress;
    private static Thread downLoadThread;
    private static boolean interceptFlag = false;
    private String msg = "发现新版本,是否更新?";
    private String updateMsg = null;
    private String updateurl = null;
    private Context context;
    private static final String SAVEPATH = Environment.getExternalStorageDirectory().getPath();
   

    public UpdateManager(Context context) {
        this.context = context;
    }

    JSONObject prepareUpdateJSON() throws JSONException {
        JSONObject jsonUpdate = new JSONObject();

        jsonUpdate.put("app_key", AppInfo.getAppKey(context));
        jsonUpdate.put("version", CommonUtil.getCurVersionCode(context));
        return jsonUpdate;
    }

    public void postUpdate() {
        JSONObject updateData;
        try {
            updateData = prepareUpdateJSON();
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, UpdateManager.class, e.toString());
            return;
        }

//        if (CommonUtil.isNetworkAvailable(context) && CommonUtil.isNetworkTypeWifi(context)&&CommonUtil.isUpdateOnlyWIFI(context)) {
//            MyMessage message = NetworkUtil.Post(UmsConstants.BASE_URL + UmsConstants.UPDATE_URL,
//                    updateData.toString());
        Log.e("xxx", "postUpdate: " + UmsConstants.BASE_URL + UmsConstants.UPDATE_URL);
        OkGo.<String>post(UmsConstants.BASE_URL + UmsConstants.UPDATE_URL).upJson(updateData).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("xxx", "onSuccess:==版本更新== " + response.body());
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    int status = jsonObject.optInt("status");
                    String data = jsonObject.optString("data");
                    if (status == 0) {
                        JSONObject jsonObject1 = new JSONObject(data);
                        updateurl = jsonObject1.optString("updateurl");
                        String description = jsonObject1.optString("description");
                        String version = jsonObject1.optString("version");
                        updateMsg = msg + "\n" + version + ":" + description;
                        saveFile = SAVEPATH + nametimeString;
                        if(Double.parseDouble(AppInfo.getAppVersion(context)) <  Double.parseDouble(version)){
                            showNoticeDialog(context);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.e("xxx", "onError: ===" + response.message());

            }
        });
//    }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case DOWN_UPDATE:
                progressDialog.setProgress(progress);
                break;
            case DOWN_OVER:
                installApk();
                break;
            default:
                break;
            }
        };
    };

    public String now() {
        Time localTime = new Time("Asia/Beijing");
        localTime.setToNow();
        return localTime.format("%Y-%m-%d");
    }

    public String nametimeString = now();

    public void showNoticeDialog(final Context context) {

        Builder builder = new Builder(context);
        builder.setTitle("应用更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog(context);
            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (UpdateManager.force.equals("true")) {
//                    System.exit(0);
//                } else {
                    dialog.dismiss();
//                }
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showSdDialog(final Context context) {
        Builder builder = new Builder(context);
        builder.setTitle("提示");
        builder.setMessage("SD卡不存在");
        builder.setNegativeButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showDownloadDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("应用更新");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;

            }
        });
        progressDialog.show();
        downloadApk();

    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            FileOutputStream fos = null;
            InputStream is = null;
            try {
                URL url = new URL(updateurl);

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                is = conn.getInputStream();
              
                boolean sdCardExist = Environment.getExternalStorageState()
                        .equals(Environment.MEDIA_MOUNTED);
                if (!sdCardExist) {
                    showSdDialog(context);
                }
                String apkFile = saveFile;
                File ApkFile = new File(apkFile);
                fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        progressDialog.dismiss();

                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);

            } catch (MalformedURLException e) {
                CobubLog.e(UmsConstants.LOG_TAG, e);
            } catch (IOException e) {
                CobubLog.e(UmsConstants.LOG_TAG, e);
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    
                } catch (IOException e) {
                    CobubLog.e(UmsConstants.LOG_TAG, e);
                }
                
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception e) {
                     CobubLog.e(UmsConstants.LOG_TAG, e);
                }

            }

        }
    };

    /**
     * download apk
     * 
     */

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * install apk
     * 
     */
    private void installApk() {
        File apkfile = new File(saveFile);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
