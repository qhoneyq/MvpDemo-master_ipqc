package youdian.apk.ipqc.network;


import youdian.apk.ipqc.utils.Constans;

/**
 * @author: qndroid
 * @function: 所有请求相关地址
 * @date: 16/8/12
 */
public class HttpConstants {

    private static final String ROOT_URL = Constans.BASEURL;

    /**
     * LOGIN API
     */
    public static String LOGIN = ROOT_URL + "v1/user/login/app/";
    public static String LOGINBYGID = ROOT_URL + "v1/user/gidlogin/app/";
    public static String UPDATELOG = ROOT_URL + "v1/user/updatelog/app/";




    /**
     * GETDATA FROM SE
     */
    public static String SEURL = ROOT_URL + "ses/";



    /**
     * GETDATA FROM DIANJIAN
     */
    public static String GETDATA_DIANJIAN = ROOT_URL + "v1/user/downloaddata/app/";

    /**
     * GETDATA FROM PANDIAN
     */
    public static String GETDATA_PANDIAN = ROOT_URL + "v1/user/pandiandown/app/";

    /**
     * GETDATA FROM IPQC
     */
    public static String GETDATA_IPQC = ROOT_URL + "v1/user/ipqcdown/app/";

    /**
     * UPLOAD RECORD OF DIANJAIN
     */
    public static String UPLOAD_DIANJIAN = ROOT_URL + "v1/user/uploaddata/app/";

    /**
     * UPLOAD RECORD OF PANDIAN
     */
    public static String UPLOAD_PANDIAN = ROOT_URL + "v1/user/pandianupload/app/";

    /**
     * UPLOAD RECORD OF IPQC
     */
    public static String UPLOAD_IPQC = ROOT_URL + "v1/user/ipqcupload/app/";

    /**
     * GET APP_VERSION_NAME ON SERVER
     */
    public static String GETVERSIONNAME = ROOT_URL + "v1/user/getversion/app/";

}


