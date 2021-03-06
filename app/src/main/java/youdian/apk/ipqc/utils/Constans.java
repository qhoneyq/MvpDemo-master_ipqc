package youdian.apk.ipqc.utils;


import java.io.File;

import youdian.apk.ipqc.base.Baseapplicton;

public class Constans {
    public static final String SIGN = "d883fd21fb995169";
    ////正式版本地址
//    public static final String BASEURL = "http://10.67.124.112:8082/v1/";
//    public static final String BASEURLLogin = "http://10.67.124.112:8082/";
    public static final String BASEURL = "http://10.134.171.201:9091/v1/";
    public static final String BASEURLLogin = "http://10.134.171.201:9091/";
//    public static final String BASEURL = "http://api.ucheck.efoxconn.com:9091/v1/";
//    public static final String BASEURLLogin = "http://api.ucheck.efoxconn.com:9091/";

    public static final int REQUEST_PERMISSION_CODE = 2;
    public static final int REQ_PERM_CAMERA = 100;

    public static final String PATH_DATA = Baseapplicton.getContext().getCacheDir().getAbsolutePath() + File.separator + "data";
    public static final String PATH_CACHE = PATH_DATA + File.separator + "net";
    public static final String UUID = "UUID";

    //登录Key和status code
    public static String AUTHORIZATION = "Authorization";
    public static String KEY_LOGIN_BEAN = "KEY_LOGIN_BEAN";
    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static String GIDLOGIN = "gid";
    public static String DEV = "dev_code";
    public static String METHOD = "method";
    public static String FAIL_CODE = "400";

    public static String FirstCheck = "FirstCheck";
    public static String Inspection = "Inspection";
    public static String Shift = "SHIFT";

    public static String INTENTFLAG = "INTENTFLAG";
    public static String NEW = "NEW";
    public static String REINPUT = "REINPUT";


    public static final String Day = "Day";
    public static final String Night = "Night";
    public static final String CHUJIAN_TYPE = "CHUJIAN_TYPE";
    public static final String Frequency = "Frequency";
    public static final String FirstSug = "FirstSug";
    public static final String ChouYang = "ChouYang";


    //表头页标识位
    public static final int FLAG_LINE = 1;
    public static final int FLAG_SN = 2;
    public static final int FLAG_CHECKTYPE = 3;
    public static final int FLAG_SHIFT = 4;



    //CHECK_STATUS
    public static final String Normal = "Normal";
    public static final String Abnormal = "Abnormal";
    public static final String Shutdown = "Shutdown";
    public static final String UnChecked = "UnChecked";
    public static final String NA = "NA";


    public static String Radio = "Radio";
    public static String Number = "Number";
    public static String Check = "Check";
    public static String Text = "Text";


    public static String MeiBan = "preClass";
    public static String HuangXian = "changeLine";

    // request参数
    public static final int REQ_QR_CODE = 11002; // // 打开扫描界面请求码
    public static final int REQ_PERM_CAMERAdemo = 11003; // 打开摄像头
    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";


    public static String NFCACTIVITY = "com.cesbg.youdian.NfcActivity";
    public static String QRACTIVITY = "com.cesbg.youdian.QRCodeScanActivity";

    public static int Auto= 1;
    public static int Input = 2;



}
