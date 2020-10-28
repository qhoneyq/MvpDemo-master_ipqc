package youdian.apk.ipqc.utils;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.parse.SimpleParser;
import youdian.apk.dianjian.base.bean.CategoryTreeData;
import youdian.apk.dianjian.base.bean.ListDataResponse;
import youdian.apk.dianjian.base.bean.Response;
import youdian.apk.dianjian.http.Urls;


/**
 * 作者 create by H7111906 on 2020/4/17
 */
public class CommonUtils {
    private static CommonUtils instance = null;

    public static CommonUtils getInstance() {
        if (instance == null) {
            instance = new CommonUtils();
        }
        return instance;
    }

    private CommonUtils() {
    }

    /**
     * 设置窗体透明度
     *
     * @param alpha  透明度
     * @param window 当前窗体
     */
    public static void setBackgroundAlpha(float alpha, Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = alpha;
        window.setAttributes(params);
    }

    /**
     * 读取NFC数据
     *
     * @param intent 刷卡意图
     * @return NFC数据
     */
    public static String nfcRead(Intent intent) {
        String result = null;
        StringBuilder readResult = new StringBuilder();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] id = tag.getId();
        for (byte b : id) {
            String hexString = String.format("%02X", b).toUpperCase();
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            readResult.append(":").append(hexString);
        }
        readResult = new StringBuilder(readResult.substring(1));
        result = readResult.toString().toLowerCase();
        return result;

//        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//        if (rawArray != null) {
//            NdefMessage message = (NdefMessage) rawArray[0];
//            NdefRecord record = message.getRecords()[0];
//            if (record != null) {
//                readResult = new String(record.getPayload(), StandardCharsets.UTF_8);
//            }
//        }
//        readResult = readResult.substring(3);
//        return readResult;
    }

    /**
     * 读取NFC标签数据的操作
     *
     * @param intent
     * @return
     */
    public static String readFromTag(Intent intent) {
        String result = null;
        Parcelable[] rawArray = intent
                .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        //然后通过Tag对象读取id
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);// // 获取id数组

        byte[] bytesId = tag.getId(); // 字符串id
        String strId = ByteArrayToHexString(bytesId);
        String final_str = HexStringToTen(strId);
        if (rawArray != null) {
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            try {
                if (mNdefRecord != null) {
                    result = new String(mNdefRecord.getPayload(), "UTF-8");
                    result = result.substring(3);
                    return result;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }
        return result;
    }
    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void showMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public boolean shouldUpdate(int servicesVersionCode, int localVersionCode) {
        return servicesVersionCode > localVersionCode;
    }


    /**
     * 判断信息是否为空
     *
     * @param data 查询数据
     * @return
     */
    public boolean isInfoNonNull(Object data) {
        if (data.equals(null) || data.equals("")) return false;
        return true;
    }


    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringNullAdapter();
        }
    }

    @NonNull
    protected static String HexStringToTen(String s) {
        String str = s.substring(6, 8) + s.substring(4, 6) + s.substring(2, 4) + s.substring(0, 2);
        return Long.toString(Long.parseLong(str, 16));
    }

    protected static String ByteArrayToHexString(@NonNull byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }
}
