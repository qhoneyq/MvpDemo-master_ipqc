package youdian.apk.ipqc.contract;




import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.Callback;
import youdian.apk.ipqc.base.BaseView;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.obsever.ProgressObserver;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/21 0021
 * Time: 下午 6:50
 * Function:
 */
public interface CheckDetailContract_CHUJIAN {

    interface IModel {
        //获取初件工序
        Observable<Response<List<ProgressObserver>>> getProgress(String first_checklist_id);
    }

    interface View extends BaseView {
        void showLoading();
        void hideLoading();
        void setProgress(List<ProgressObserver> list);

    }


    interface Presenter extends BasePresenter {

      void getProgress(String first_checklist_id);
      void getProgress(String first_checklist_id);

    }
}
