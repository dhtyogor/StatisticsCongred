/**
 * Cobub Razor
 * <p>
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

import com.congred.statistics.CongredAgent.LogLevel;
import com.congred.statistics.CongredAgent.SendPolicy;

public class UmsConstants {
    public  static String BASE_URL = "";
    
    public final static String CLIENTDATA_URL = "/clientdata";
    public final static String ERROR_URL = "/errorlog";
    public final static String EVENT_URL = "/eventdata";
    public final static String USINGLOG_URL = "/clientusinglog";
    public final static String UPDATE_URL = "/appupdate";
    public final static String CONFIG_URL = "/pushpolicyquery";

    public final static String LOG_TAG = "UMSAgent";

    public static boolean DebugEnabled = false;
    public static LogLevel DebugLevel = LogLevel.Debug;
    public static boolean mProvideGPSData = false; // Default is false, not use GPS data.
    public static long defaultFileSize = 1024 * 1024;//1M
    public static String fileSep = "@_@";

    public static String LIB_VERSION = "1.0";

    public static SendPolicy mReportPolicy = SendPolicy.POST_NOW;

}


