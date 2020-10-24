package youdian.apk.ipqc.model;


import io.reactivex.rxjava3.core.Observable;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.bean.UserData;
import youdian.apk.ipqc.contract.LoginContract;
import youdian.apk.ipqc.contract.MainContract;
import youdian.apk.ipqc.network.RetrofitClient;

public class LoginModel implements LoginContract.IMainModel {
    @Override
    public Observable<Response<UserData>> login(String devid, String username, String passwd, String gid, String account) {
        return RetrofitClient.getInstance().getApi().login(devid,username,passwd,gid,account);
    }


}
