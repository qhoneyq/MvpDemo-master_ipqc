package youdian.apk.ipqc.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


import youdian.apk.ipqc.R;
import youdian.apk.ipqc.base.BaseActivity;
import youdian.apk.ipqc.databinding.ActivityHomeBinding;
import youdian.apk.ipqc.obsever.FirstCheckResultObserver;
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
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this,getLayoutId());
//        if (!UserUtils.getInstance().isLogin()){
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//        }

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
        binding.homeHeaderview.setTitleText(getResources().getString(R.string.title));

    }

    public void firstCheckClick(){
        Bundle bundle =new Bundle();
        FirstCheckResultObserver observer = new FirstCheckResultObserver();
        observer.setCheck_person("admin");
        observer.setFirst_checklist_code("code");
        observer.setFirst_checklist_name("name");
        observer.setSe_code("secode");
        observer.setSe_name("sename");
        bundle.putSerializable(Constans.FirstCheck,observer);
            NewChujian_Activity.startActivity(this, bundle);
//            TableListActivity.startTableListActivity(this, Constans.FirstCheck);
            finish();
    }

    public void insChecksClick(){
            TableListActivity.startTableListActivity(this, Constans.FirstCheck);
            finish();
    }


}
