package youdian.apk.ipqc.contract;


import io.reactivex.rxjava3.core.Observable;
import youdian.apk.ipqc.base.BasePresenter;
import youdian.apk.ipqc.base.BaseView;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.bean.UserData;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/17 0017
 * Time: 下午 3:20
 * Function: ipqc主页契约接口，可以很直观的看到 M、V、P 层接口中提供的方法
 */
public interface LoginContract {

    interface IMainModel {
        Observable<Response<UserData>> login(String devid, String username, String passwd, String gid, String account);


    }

    interface View extends BaseView {
//
//        void showLoading();
//        void hideLoading();
//        void failed();
        void dealUserData(Response<UserData> data);
//
//        //主页显示表单
//        void onMainItemClick(com.foxconn.youdian.youdian.bean.Currency c);
//        void setChujianList(List list);
//        void setXunjian(List list);
//        void updateView();

    }


    interface Presenter  {
        void login(String devid,String username,String passwd,String gid,String account) ;


    }


}
