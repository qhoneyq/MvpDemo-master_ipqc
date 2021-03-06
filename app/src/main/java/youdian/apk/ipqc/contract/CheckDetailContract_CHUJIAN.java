package youdian.apk.ipqc.contract;


import android.drm.ProcessedData;

import androidx.databinding.ObservableList;

import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import youdian.apk.ipqc.base.BaseView;
import youdian.apk.ipqc.bean.FirstCheckList;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.obsever.FirstCheckItemObserver;
import youdian.apk.ipqc.obsever.FirstCheckResultObserver;
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
        //获取初件工序及检验项
        Observable<Response<FirstCheckList>> getCheckListData(String first_checklist_id);

        Observable<Response<List<ProgressObserver>>> getProcess(String first_checklist_id);

        Observable<Response<List<OptionData>>> getSelectInfo(String option);

        Observable<Response<FirstCheckResultObserver>> postFirstResult(RequestBody body);
    }

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void showMsg(String msg);

        void setCheckListData(List<FirstCheckItemObserver> list);//获取全部检验项

//        void showCheckItemByProcess(int process_id);//获取单个工序检验项

        void setprocess(ObservableList<ProgressObserver> list);

        void showBottomDialog(List<OptionData> list);

        void showPopWindow(boolean issucceed,String result);

    }


    interface Presenter {

        void getProcess(String first_checklist_id);

        void getCheckListData(String first_checklist_id);

        void getCheckSuggestion(String option);

        void showBottomDialog();

        void postFirstResult(FirstCheckResultObserver firstCheckResultObserver);

    }
}
