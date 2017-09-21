/**
 * Cobub Razor
 *
 * An open source analytics android sdk for mobile applications
 *
 * @package     Cobub Razor
 * @author      WBTECH Dev Team
 * @copyright   Copyright (c) 2011 - 2015, NanJing Western Bridge Co.,Ltd.
 * @license     http://www.cobub.com/products/cobub-razor/license
 * @link        http://www.cobub.com/products/cobub-razor/
 * @since       Version 0.1
 * @filesource
 */
package com.congred.statistics;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * @author apple
 */
class DeviceInfo {
    private static Context context;
    private static Location location;
    private static TelephonyManager telephonyManager;
    private static LocationManager locationManager;
    private static BluetoothAdapter bluetoothAdapter;
    private static SensorManager sensorManager;
    private static String DEVICE_ID = "";
    private static String DEVICE_NAME = "";

    public static void init(Context context) {
        DeviceInfo.context = context;

        try {
            telephonyManager = (TelephonyManager) (context
                    .getSystemService(Context.TELEPHONY_SERVICE));
            locationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, DeviceInfo.class, e.toString());
        }
        getLocation();
    }

    public static String getDeviceId() {
        if (DEVICE_ID.equals("")) {
            try {
                SharedPrefUtil sp = new SharedPrefUtil(context);
                String uniqueid = sp.getValue("uniqueuid", "");

                if (!uniqueid.equals("")) {
                    DEVICE_ID = uniqueid;
                } else {
                    String imei = getDeviceIMEI();
                    String imsi = getIMSI();
                    String salt = CommonUtil.getSALT(context);
                    DEVICE_ID = CommonUtil.md5(imei + imsi + salt);
                    sp.setValue("uniqueuid", DEVICE_ID);
                }
            } catch (Exception e) {
                CobubLog.e(UmsConstants.LOG_TAG,e);
            }


        }
        return DEVICE_ID;
    }

    public static String getOsVersion() {
        String result = Build.VERSION.RELEASE;
        CobubLog.i(UmsConstants.LOG_TAG,  DeviceInfo.class,"getOsVersion()=" + result);
        if (result == null)
            return "";

        return result;
    }
    public static String getLanguage() {
        String language = Locale.getDefault().getLanguage();
        CobubLog.i(UmsConstants.LOG_TAG,    DeviceInfo.class, "getLanguage()=" + language);
        if (language == null)
            return "";
        return language;
    }

    public static String getResolution() {
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displaysMetrics);
        CobubLog.i(UmsConstants.LOG_TAG,    DeviceInfo.class, "getResolution()=" + displaysMetrics.widthPixels + "x"
                + displaysMetrics.heightPixels);
        return displaysMetrics.widthPixels + "x" + displaysMetrics.heightPixels;
    }

    public static String getNetworkTypeWIFI2G3G() {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            String type = "";
            if (ni != null && ni.getTypeName() != null) {
                type = ni.getTypeName().toLowerCase(Locale.US);
                if (!type.equals("wifi")) {
                    type = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                            .getExtraInfo();
                }
            }

            return type;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDeviceTime() {
        try {
            Date date = new Date();
            SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.US);
            return localSimpleDateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDeviceProduct() {
        String result = Build.PRODUCT;
        CobubLog.i(UmsConstants.LOG_TAG,  DeviceInfo.class,   "getDeviceProduct()=" + result);
        if (result == null)
            return "";
        return result;
    }

    public static String getDeviceName() {
        if (DEVICE_NAME.equals("")) {
            try {
                String manufacturer = Build.MANUFACTURER;
                if (manufacturer == null)
                    manufacturer = "";
                String model = Build.MODEL;
                if (model == null)
                    model = "";

                if (model.startsWith(manufacturer)) {
                    DEVICE_NAME = capitalize(model).trim();
                } else {
                    DEVICE_NAME = (capitalize(manufacturer) + " " + model)
                            .trim();
                }
            } catch (Exception e) {
                CobubLog.e(UmsConstants.LOG_TAG,   e);
                return "";
            }
        }
        return DEVICE_NAME;
    }

    public static String getWifiMac() {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wi = wifiManager.getConnectionInfo();
            String result = wi.getMacAddress();
            if (result == null)
                result = "";
            CobubLog.i(UmsConstants.LOG_TAG,  DeviceInfo.class, "getWifiMac()=" + result);
            return result;
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG,  e);
            return "";
        }
    }

    public static boolean getBluetoothAvailable() {
        return bluetoothAdapter != null;
    }

    public static boolean getWiFiAvailable() {
        try {
            if (!CommonUtil.checkPermissions(context,
                    Manifest.permission.ACCESS_WIFI_STATE)) {
                CobubLog.e(UmsConstants.LOG_TAG,    DeviceInfo.class,
                        "ACCESS_WIFI_STATE permission should be added into AndroidManifest.xml.");
                return false;
            }
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getType() == ConnectivityManager.TYPE_WIFI
                                && anInfo.isConnected()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getGPSAvailable() {
        if (location == null)
            return "false";
        else
            return "true";
    }

    public static boolean getGravityAvailable() {
        try {
            // This code getSystemService(Context.SENSOR_SERVICE);
            // often hangs out the application when it runs in Android
            // Simulator.
            // so in simulator, this line will not be run.
            if (isSimulator())
                sensorManager = null;
            else
                sensorManager = (SensorManager) context
                        .getSystemService(Context.SENSOR_SERVICE);
            CobubLog.i(UmsConstants.LOG_TAG,   DeviceInfo.class, "getGravityAvailable()");
            return sensorManager != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getLatitude() {
        if (location == null)
            return "";
        return String.valueOf(location.getLatitude());
    }

    public static String getLongitude() {
        if (location == null)
            return "";
        return String.valueOf(location.getLongitude());

    }


    private static void getLocation() {
        CobubLog.i(UmsConstants.LOG_TAG,  DeviceInfo.class, "getLocation");
        try {
            List<String> matchingProviders = locationManager.getAllProviders();
            for (String prociderString : matchingProviders) {
                location = locationManager.getLastKnownLocation(prociderString);
                if (location != null)
                    break;
            }
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG,  DeviceInfo.class, e.toString());
        }
    }



    public static String getDeviceIMEI() {
        String result = "";
        try {
            if (!CommonUtil.checkPermissions(context,
                    Manifest.permission.READ_PHONE_STATE)) {
                CobubLog.e(UmsConstants.LOG_TAG,  DeviceInfo.class,
                        "READ_PHONE_STATE permission should be added into AndroidManifest.xml.");
                return "";
            }
            result = telephonyManager.getDeviceId();
            if (result == null)
                result = "";
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG,  e);
        }
        return result;
    }

    /**
     * get IMSI for GSM phone, return "" if it is unavailable.
     *
     * @return IMSI string
     */
    public static String getIMSI() {
        String result = "";
        try {
            if (!CommonUtil.checkPermissions(context,
                    Manifest.permission.READ_PHONE_STATE)) {
                CobubLog.e(UmsConstants.LOG_TAG,  DeviceInfo.class,
                        "READ_PHONE_STATE permission should be added into AndroidManifest.xml.");
                return "";
            }
            result = telephonyManager.getSubscriberId();
            Log.e("xxx", "getIMSI:===== "+result );
            CobubLog.i(UmsConstants.LOG_TAG,  DeviceInfo.class, "getIMSI()=" + result);

            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            Log.e("xxx", "getIMSI: "+tm.getSubscriberId());
            if (result == null)
                return "";
            return result;

        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG,   e);
        }

        return result;
    }
    public static List getImsis() {
        String imsi = null;
        List imsiList = new ArrayList<>();
        try {
            //普通方法获取imsi
             TelephonyManager tm = (TelephonyManager) context. getSystemService(Context.TELEPHONY_SERVICE);
             if (tm.getSubscriberId() != null) imsiList.add(tm.getSubscriberId());
             imsi = null;
             Class[] resources = new Class[]{int.class};
             try {
             Method addMethod = tm.getClass().getDeclaredMethod("getSubscriberIdGemini", resources);
             addMethod.setAccessible(true);
             imsiList = getImsis(addMethod, tm, imsiList);
             return imsiList;
             }
             catch (Exception e) {
             imsi = null;
             }
             try {
             Method addMethod2 = tm.getClass().getDeclaredMethod("getSubscriberId", resources);
             addMethod2.setAccessible(true);
             imsiList = getImsis(addMethod2, tm, imsiList);
             return imsiList;
             } catch (Exception e) {

              }
             return imsiList;
             } catch (Exception e) {
            return imsiList; }

    }

    private static List getImsis(Method method, TelephonyManager tm, List list) throws Exception {
        List stringList = new ArrayList<>();
        stringList.addAll(list);
        for (int i = 1; i < 4; i++) {
            /*检查1到3卡位imsi，不重复就添加，可以获取不止双卡手机信息*/
            String ii = (String) method.invoke(tm, new Integer(i));
            if (!TextUtils.isEmpty(ii) && !stringList.contains(ii)) {
                stringList.add(ii);
            }
        }
        return stringList;
    }




    /**
     * Capitalize the first letter
     *
     * @param s
     *            model,manufacturer
     * @return Capitalize the first letter
     */
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    private static boolean isSimulator() {
        return getDeviceIMEI().equals("000000000000000");
    }

    public static String getIp(){
        WifiManager wm = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        //检查Wifi状态
        if(!wm.isWifiEnabled())
            return "wifi未开启";
        WifiInfo wi=wm.getConnectionInfo();
        //获取32位整型IP地址
        int ipAdd=wi.getIpAddress();
        //把整型地址转换成“*.*.*.*”地址
        String ip=intToIp(ipAdd);
        return ip;
    }
    private static String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }












    /**
     * 
     * @throws Exception
     */
    public static String getCellInfoofLAC(){
            CellLocation cl = telephonyManager.getCellLocation();
            if (cl instanceof GsmCellLocation) {
                GsmCellLocation location = (GsmCellLocation) cl;
                int lac = location.getLac();
                
                return lac+"";
            } else if (cl instanceof CdmaCellLocation) {
                CdmaCellLocation cdma = (CdmaCellLocation) cl;
                int lac = cdma.getNetworkId();   
                
                return lac+"";
            }
       
        return "";        
    }
    
    public static String getCellInfoofCID(){
            CellLocation cl = telephonyManager.getCellLocation();
            if (cl instanceof GsmCellLocation) {
                GsmCellLocation location = (GsmCellLocation) cl;
                int cid = location.getCid();
                return cid+"";
            } else if (cl instanceof CdmaCellLocation) {
                CdmaCellLocation cdma = (CdmaCellLocation) cl;
                int cid = cdma.getBaseStationId(); 
                return cid+"";
            }
        return "";        
    }


    /**
     * Returns a constant indicating the device phone type. This indicates the
     * type of radio used to transmit voice calls.
     * 
     * @return PHONE_TYPE_NONE //0 PHONE_TYPE_GSM //1 PHONE_TYPE_CDMA //2
     *         PHONE_TYPE_SIP //3
     */
    public static int getPhoneType() {
        if (telephonyManager == null)
            return -1;
        int result = telephonyManager.getPhoneType();
        CobubLog.i(UmsConstants.LOG_TAG,   DeviceInfo.class, "getPhoneType()=" + result);
        return result;
    }

    public static String getPhoneNum() {
        String result = "";
        try {
            if (!CommonUtil.checkPermissions(context,
                    Manifest.permission.READ_PHONE_STATE)) {
                CobubLog.e(UmsConstants.LOG_TAG,  DeviceInfo.class,
                        "READ_PHONE_STATE permission should be added into AndroidManifest.xml.");
                return "";
            }
            result = telephonyManager.getLine1Number();
            if (result == null)
                result = "";
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG,  e);
        }
        return result;
    }

    private static String getSSN() {
        String result = "";
        try {
            if (!CommonUtil.checkPermissions(context,
                    Manifest.permission.READ_PHONE_STATE)) {
                CobubLog.e(UmsConstants.LOG_TAG,  DeviceInfo.class,
                        "READ_PHONE_STATE permission should be added into AndroidManifest.xml.");
                return "";
            }
            result = telephonyManager.getSimSerialNumber();
            if (result == null)
                result = "";
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
        }
        return result;
    }

    public static void setDeviceId(String did) {
        DEVICE_ID = did;
    }
    
    public static String getMCCMNC() {
        String result;
        try {

            String operator = telephonyManager.getNetworkOperator();
            if (operator == null)
                result = "";
            else
                result = operator;
        } catch (Exception e) {
            result = "";
            CobubLog.e(UmsConstants.LOG_TAG,  DeviceInfo.class, e.toString());
        }
        return result;
    }


}
