package youdian.apk.ipqc.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;


import androidx.annotation.Nullable;


import youdian.apk.ipqc.R;
import youdian.apk.ipqc.base.BaseActivity;
import youdian.apk.ipqc.databinding.ActivityHomeBinding;
import youdian.apk.ipqc.utils.Constans;
import youdian.apk.ipqc.utils.UserUtils;

public class ActivityHome extends BaseActivity {

    ActivityHomeBinding binding;

    /**
     * 静态方法跳转到当前页面
     */
    public static void startMainActivity(Context context, String data) {
        Intent intent = new Intent(context, ActivityHome.class);
        intent.putExtra("param", data);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if (!UserUtils.getInstance().isLogin()){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        binding.homeChujian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCheckClick();
            }
        });

        binding.homeXunjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insChecksClick();
            }
        });


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        binding.homeHeaderview.setTitleText(getResources().getString(R.string.title));
    }

    public void firstCheckClick(){
            TableListActivity.startTableListActivity(this, Constans.FirstCheck);
            finish();
    }

    public void insChecksClick(){
            TableListActivity.startTableListActivity(this, Constans.FirstCheck);
            finish();
    }


}
