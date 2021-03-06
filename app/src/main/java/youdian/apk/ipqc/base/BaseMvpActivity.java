package youdian.apk.ipqc.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import youdian.apk.ipqc.utils.AndroidBug54971Workaround;
import youdian.apk.ipqc.utils.DeviceUtil;
import youdian.apk.ipqc.utils.PermissionTool;

/**
 * @author azheng
 * @date 2018/4/24.
 * GitHub：https://github.com/RookieExaminer
 * Email：wei.azheng@foxmail.com
 * Description：
 */
public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity implements BaseView {

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionTool.Request(this, new PermissionTool.PermissionCallBack() {
            @Override
            public void Success() {
                // 在权限请求完成后，才可执行的逻辑
                DeviceUtil.getDeviceId(BaseMvpActivity.this);
            }
        });
    }
    /**
     * 处理权限请求结果，若未授权，则继续请求
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionTool.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        }
    }


    /**
     * Activity执行结果
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionTool.onActivityResult(this, requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        super.onDestroy();
    }

    /**
     * 绑定生命周期 防止MVP内存泄漏
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AndroidBug54971Workaround.checkDeviceHasNavigationBar(this)) {                                  //适配华为手机虚拟键遮挡tab的问题
            AndroidBug54971Workaround.assistActivity(findViewById(android.R.id.content));                   //需要在setContentView()方法后面执行
        }
    }
}
