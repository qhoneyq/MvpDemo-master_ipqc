package youdian.apk.ipqc.base;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.qihoo360.replugin.loader.a.PluginFragmentActivity;

import java.io.UnsupportedEncodingException;

import youdian.apk.ipqc.utils.AndroidBug54971Workaround;
import youdian.apk.ipqc.utils.MyUtils;
import youdian.apk.ipqc.wedige.WaitDialog;

import static youdian.apk.ipqc.utils.Constans.REQUEST_PERMISSION_CODE;


/**
 */
public abstract class BaseActivity extends PluginFragmentActivity {

    //读写权限
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};    //请求状态码

    public WaitDialog waitDialog;

    protected NfcAdapter nfcAdapter;
    protected String readResult = "";
    protected PendingIntent pendingIntent;
    protected IntentFilter[] mFilters;
    protected String[][] mTechLists;
    protected IntentFilter ndef;

    protected String devId="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(this.getLayoutId());
//        MyUtils.setStatus(this);
        //华为全屏
//        if (AndroidBug54971Workaround.checkDeviceHasNavigationBar(this)) {                                  //适配华为手机虚拟键遮挡tab的问题
//            AndroidBug54971Workaround.assistActivity(findViewById(android.R.id.content));                   //需要在setContentView()方法后面执行
//        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        waitDialog = new WaitDialog(this);
        devId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        initView();
    }
    protected void initNfc() {
        //NFC适配器，所有的关于NFC的操作从该适配器进行
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (!ifNFCUse()) {
            return;
        }
        //将被调用的Intent，用于重复被Intent触发后将要执行的跳转
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        //设定要过滤的标签动作，这里只接收ACTION_NDEF_DISCOVERED类型
        ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        ndef.addCategory("*/*");
        mFilters = new IntentFilter[]{ndef};// 过滤器
        mTechLists = new String[][]{new String[]{NfcA.class.getName()},
                new String[]{NfcF.class.getName()},
                new String[]{NfcB.class.getName()},
                new String[]{NfcV.class.getName()}};// 允许扫描的标签类型
    }

    /**
     * 检测工作,判断设备的NFC支持情况
     *
     * @return
     */
    protected Boolean ifNFCUse() {
        // TODO Auto-generated method stub
        if (nfcAdapter == null) {
            MyUtils.MyToast(this, "设备不支持NFC！");
            return false;
        }
        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "请先开启NFC功能", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
        return true;
    }

    /**
     * 读取NFC标签数据的操作
     *
     * @param intent
     * @return
     */
    protected boolean readFromTag(Intent intent) {
        Parcelable[] rawArray = intent
                .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        //然后通过Tag对象读取id
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);// // 获取id数组

        byte[] bytesId = tag.getId(); // 字符串id
        String strId = null;
        strId = ByteArrayToHexString(bytesId);
        strId = HexStringToTen(strId);
        if (rawArray != null) {
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            try {
                if (mNdefRecord != null) {
                    readResult = new String(mNdefRecord.getPayload(), "UTF-8");
                    readResult = readResult.substring(3);
                    return true;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }

    @NonNull
    protected String HexStringToTen(String s) {
        String str = s.substring(6, 8) + s.substring(4, 6) + s.substring(2, 4) + s.substring(0, 2);
        return Long.toString(Long.parseLong(str, 16));
    }

    protected String ByteArrayToHexString(@NonNull byte[] inarray) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化视图
     */
    public abstract void initView();

    @Override
    protected void onResume() {
        super.onResume();
        if (AndroidBug54971Workaround.checkDeviceHasNavigationBar(this)) {                                  //适配华为手机虚拟键遮挡tab的问题
            AndroidBug54971Workaround.assistActivity(findViewById(android.R.id.content));                   //需要在setContentView()方法后面执行
        }
    }
}
