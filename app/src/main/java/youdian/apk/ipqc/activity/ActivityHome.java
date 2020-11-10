package youdian.apk.ipqc.activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


import youdian.apk.ipqc.R;
import youdian.apk.ipqc.base.BaseActivity;
import youdian.apk.ipqc.databinding.ActivityHomeBinding;
import youdian.apk.ipqc.obsever.FirstCheckResultObserver;
import youdian.apk.ipqc.obsever.InsCheckResultObserver;
import youdian.apk.ipqc.utils.Constans;
import youdian.apk.ipqc.utils.DeviceUtils;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(ActivityHome.this)
                    .setTitle("退出")
                    .setMessage("确定退出应用吗")
                    .setNegativeButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
//                                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                                    intent.addCategory(Intent.CATEGORY_HOME);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
                                    finish();

                                }
                            }).setPositiveButton("取消", null).show();
            return true;

        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            System.out.println("HOME has been pressed yet ...");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this,getLayoutId());
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
        binding.homeHeaderview.setTitleText(getResources().getString(R.string.title));
        binding.homeHeaderview.showIcon(true);
        binding.homeHeaderview.showRightIcon(true);
        binding.homeHeaderview.setLeftIcon(R.mipmap.home_icon_return);
        binding.homeHeaderview.setRightIcon(R.mipmap.home_icon_mine);
        binding.homeHeaderview.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.homeHeaderview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ActivityHome.this)
                        .setTitle("切换用户")
                        .setMessage("确定切换其他用户登录吗\n"+ DeviceUtils.getDeviceId(ActivityHome.this))
                        .setNegativeButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        UserUtils.getInstance().logout();
                                        LoginActivity.startActivityLogin(ActivityHome.this);

                                    }
                                }).setPositiveButton("取消", null).show();

            }
        });

    }

    public void firstCheckClick(){
//        Bundle bundle =new Bundle();
//        FirstCheckResultObserver observer = new FirstCheckResultObserver();
//        observer.setCheck_person("admin");
//        observer.setFirst_checklist_code("code");
//        observer.setFirst_checklist_name("name");
//        observer.setSe_code("secode");
//        observer.setSe_name("sename");
//        bundle.putSerializable(Constans.FirstCheck,observer);
//            NewChujian_Activity.startActivity(this, bundle);
            TableListActivity.startTableListActivity(this, Constans.FirstCheck);
    }

    public void insChecksClick(){
//                Bundle bundle =new Bundle();
//        InsCheckResultObserver observer = new InsCheckResultObserver();
//        observer.setCheck_person("admin");
//        observer.setIns_checklist_code("code");
//        observer.setIns_checklist_name("name");
//        observer.setSe_code("secode");
//        observer.setSe_name("sename");
//        bundle.putSerializable(Constans.Inspection,observer);
//            NewXunjian_Activity.startActivity(this, bundle);
            TableListActivity.startTableListActivity(this, Constans.Inspection);
    }


}
