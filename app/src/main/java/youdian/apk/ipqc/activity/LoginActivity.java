package youdian.apk.ipqc.activity;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import java.nio.charset.Charset;
import java.util.Locale;

import youdian.apk.ipqc.R;
import youdian.apk.ipqc.base.BaseMvpActivity;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.bean.UserData;
import youdian.apk.ipqc.contract.LoginContract;
import youdian.apk.ipqc.databinding.ActivityLoginBinding;
import youdian.apk.ipqc.presenter.LoginPresenter;
import youdian.apk.ipqc.utils.DeviceUtils;
import youdian.apk.ipqc.utils.MyUtils;
import youdian.apk.ipqc.utils.UserUtils;
import youdian.apk.ipqc.utils.Utils;

/**
 * 登录页
 */
public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View {
    ActivityLoginBinding binding;

    private MyUtils myUtils;
    String gid = "";

    NfcAdapter mAdapter;
    Dialog dialog_nfc;
    NdefMessage mNdefPushMessage;
    PendingIntent mPendingIntent;
    private boolean isOnShowing = false;//是否正在刷卡

    /**
     * 通过静态方法跳转到登录页面
     */
    public static void startActivityLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        myUtils = new MyUtils();
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        binding.btnLoginCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login("account");

            }
        });
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);

    }

    private void showSomeMsg(Object res) {
        if (res instanceof String) {
            Utils.getInstance().showMsg(this, (String) res);
        } else if (res instanceof Integer) {
            Utils.getInstance().showMsg(this, getText((Integer) res).toString());
        }
    }

/**
 * 联网登录
 * Login by pnum and password
 */
    /**
     * Login by pnum and password
     */
    private void Login(String method) {
        if (method.equals("account")) {
            String emp_no = binding.edtLoginPnum.getText().toString();
            String pass = binding.edtLoginPass.getText().toString();

            if (TextUtils.isEmpty(method) || TextUtils.isEmpty(emp_no) || TextUtils.isEmpty(pass)) {
                showSomeMsg(R.string.empty_login);
                return;
            }
        }
        if (!devId.equals("")) {
            mPresenter.login(devId, binding.edtLoginPnum.getText().toString(), binding.edtLoginPass.getText().toString(),
                    gid, method);
        } else {
            showSomeMsg(R.string.empty_dev);
        }
        gid = "";
        isOnShowing = false;

    }

    /**
     * Save personal imformation including some access privileges
     *
     * @param loginDataBaseResponseBean
     */
    private void dealLogin(Response<UserData> loginDataBaseResponseBean) {
        UserUtils.getInstance().login(loginDataBaseResponseBean);
        startActivity(new Intent(LoginActivity.this, ActivityHome.class));
        finish();
    }


    /***************************************  Recognition of NFC  ************************************************/

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter == null) {
            // "设备不支持NFC！"
            return;
        }
        if (mAdapter != null && !mAdapter.isEnabled()) {
            dialog_nfc = new Dialog(this);
            dialog_nfc.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_nfc.setContentView(R.layout.dialog_nfc);
            dialog_nfc.setCancelable(false);
            dialog_nfc.show();
            dialog_nfc.findViewById(R.id.btn_dialog_nfc).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);// 系统设置界面
                            startActivity(intent);
                            dialog_nfc.dismiss();
                        }
                    });
        } else {
            mPendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass())
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            NdefRecord[] arrayOfNdefRecord = new NdefRecord[1];
            arrayOfNdefRecord[0] = newTextRecord("Message from NFC Reader",
                    Locale.CHINA, true);
            this.mNdefPushMessage = new NdefMessage(arrayOfNdefRecord);
            this.mAdapter.enableForegroundDispatch(this, this.mPendingIntent,
                    null, null);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.mAdapter == null)
            return;
        this.mAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private NdefRecord newTextRecord(String payload, Locale locale,
                                     boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(
                Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset
                .forName("UTF-16");

        byte[] textBytes = payload.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);

        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];

        data[0] = (byte) status;

        System.arraycopy(langBytes, 0, data, 1, langBytes.length);

        System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
                textBytes.length);

        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);

        return record;
    }

    protected void resolveIntent(Intent intent) {

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            // 处理该intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] id = tag.getId();
            // [32, -111, 83, 24] 20.91.53.18
            for (int i = 0; i < id.length; i++) {

                String hexString = Integer.toHexString(id[i] & 0xFF).toUpperCase();
                if (hexString.length() == 1) {
                    hexString = "0" + hexString;
                }
                gid = gid + ":" + hexString;
            }
            gid = gid.substring(1);
            if (!isOnShowing) {
                isOnShowing = true;
                Login("gid");

            } else {
                //重复刷卡

                MyUtils.MyToast(this, getResources().getString(R.string.repeat));
            }
        } else {
            MyUtils.MyToast(this, getResources().getString(R.string.card_error));

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            //当isShouldHideInput(v, ev)为true时，表示的是点击输入框区域，则需要显示键盘，同时显示光标，反之，需要隐藏键盘、光标
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    //处理Editext的光标隐藏、显示逻辑
                    binding.edtLoginPass.clearFocus();
                    binding.edtLoginPnum.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            // 点击的是输入框区域，保留点击EditText的事件
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

    }


    @Override
    public void dealUserData(Response<UserData> currencyResponse) {
        if (currencyResponse.getCode() == 100) {
            ActivityHome.startMainActivity(this, null);
            UserUtils.getInstance().login(currencyResponse);
            finish();
        } else if (currencyResponse.getCode() == 400) {
            String msg = Utils.getInstance().getErrorMsg(currencyResponse.getMsg().toString());
        } else {
            showSomeMsg(currencyResponse.getMsg());
        }
    }

    @Override
    public void showLoading() {
        waitDialog.show();
    }

    @Override
    public void hideLoading() {
        waitDialog.dismiss();
    }

    @Override
    public void onError(String errMessage) {
        showSomeMsg(errMessage);
    }
}
