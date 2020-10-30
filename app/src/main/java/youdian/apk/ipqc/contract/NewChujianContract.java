package youdian.apk.ipqc.contract;


import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import youdian.apk.ipqc.base.BaseView;
import youdian.apk.ipqc.bean.Lines;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.bean.Response;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/25 0025
 * Time: 上午 9:15
 * Function:
 */
public interface NewChujianContract {
    interface IMainModel {

        Observable<Response<ListResponseData<Lines>>> getLines(String secode);
        Observable<Response<List<OptionData>>> getSelectInfo(String selectinfos);

    }

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void showError(int flag, String msg);

        void setLines(List<Lines> list);

        void showLineBottomDialog(List<Lines> list);

        void showCheckTypeBottomDialog(List<OptionData> list);
        void showShiftBottomDialog(List<OptionData> list);


        void hideDialog();

        boolean isShowingDialog();

        void failed();
//        void recoverTable(TableaTitle_Chujian tabletitle);


    }


    interface Presenter {

        void getLines(String secode);
        //获取下拉值
        void getSelectInfo(String selectinfo);

        void showBottomDialog(int flag);
    }
}
