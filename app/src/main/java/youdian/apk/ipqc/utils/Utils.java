package youdian.apk.ipqc.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    private static Utils instance = null;

    private Utils() {
    }

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    @SuppressLint("StaticFieldLeak")
    private Context context = null;
//    private static List<OnInit> sOnInits = null;

    public void init(Context context) {
        this.context = context;
//        if (sOnInits != null) {
//            for (OnInit onInit : sOnInits) {
//                onInit.onInit(context);
//            }
//        }
    }

    /**
     * 全局context
     *
     * @return
     */
    public Context getAppContext() {
        if (context == null) {
            throw new RuntimeException("Utils未在Application中初始化");
        }
        return context;
    }

    public String getErrorMsg(String result) {
        String errMsg = "";
        LinkedHashMap<String, List<String>> jsonMap = JSON.parseObject(result, new TypeReference<LinkedHashMap<String, List<String>>>() {
        });
        for (Map.Entry<String, List<String>> entry : jsonMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
            errMsg = entry.getValue().get(0) + "\n";
        }
        return errMsg;
    }

    public void showMsg(Context cxt, String message) {
        Toast.makeText(cxt, message, Toast.LENGTH_SHORT).show();
    }

}
