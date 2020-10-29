package youdian.apk.ipqc.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import youdian.apk.ipqc.utils.Utils;

/**
 * @author
 * @date 2020/5/28.
 * description：
 */
public class Baseapplicton extends Application implements Application.ActivityLifecycleCallbacks {
    @SuppressLint("StaticFieldLeak")
    private static Application mApplication = null;
    private static List<Activity> activities = Collections.synchronizedList(new LinkedList<>());
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Utils.getInstance().init(this);
        this.mContext = this;
        mApplication = this;
//        MultiDex.install(this);


//        //界面加载管理 初始化
//        LoadSir.beginBuilder()
//                .addCallback(new LoadingCallback())//加载
//                .addCallback(new ErrorCallback())//错误
//                .addCallback(new EmptyCallback())//空
//                .setDefaultCallback(SuccessCallback.class)//设置默认加载状态页
//                .commit();

    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}