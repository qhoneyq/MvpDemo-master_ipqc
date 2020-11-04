package youdian.apk.ipqc.utils;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

import youdian.apk.ipqc.wedige.CustomPopupWindow;


public class MycountDownTimer extends CountDownTimer {

    CustomPopupWindow view;
    TextView textView;
    Activity activity;

    public MycountDownTimer(Activity activity, TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        // TODO Auto-generated constructor stub
        this.textView = textView;
        this.activity = activity;

    }

    public MycountDownTimer(CustomPopupWindow view, TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        // TODO Auto-generated constructor stub
        this.view = view;
        this.textView = textView;

    }

    @Override
    public void onFinish() {
        textView.setText("页面关闭");
        if (this.activity != null)
            activity.finish();
        if (this.view!=null){
            view.dismiss();
        }

    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setText(millisUntilFinished / 1000 + "秒后自动关闭");
    }

}
