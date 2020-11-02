package youdian.apk.ipqc.wedige;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import youdian.apk.ipqc.R;


public class WaitDialog extends ProgressDialog {
    TextView tipTextView;

    public WaitDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
    }

    public TextView getTipTextView() {
        return tipTextView;
    }

    public void setTipText(String tips) {
        if (!tips.equals("")) {
            tipTextView.setVisibility(View.VISIBLE);
            tipTextView.setText(tips);// 设置加载信息}
        } else {
            tipTextView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        tipTextView = (TextView) findViewById(R.id.dialog_loading_tv);// 提示文字
        //        tipTextView.setText(R.string.wait_dialog_title);// 设置加载信息
        WaitDialog.this.setCancelable(false);

    }


}
