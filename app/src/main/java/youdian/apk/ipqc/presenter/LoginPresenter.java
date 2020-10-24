package youdian.apk.ipqc.presenter;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import youdian.apk.ipqc.base.BasePresenter;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.bean.UserData;
import youdian.apk.ipqc.contract.LoginContract;
import youdian.apk.ipqc.model.LoginModel;
import youdian.apk.ipqc.network.RxScheduler;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {


    private LoginContract.IMainModel model;

    public LoginPresenter() {
        model = new LoginModel();
    }

    @Override
    public void login(String devid, String username, String passwd, String gid, String account) {
//View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.login(devid,username,passwd,gid,account)
                .compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<UserData>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Response<UserData> userDataResponse) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
          }
}
