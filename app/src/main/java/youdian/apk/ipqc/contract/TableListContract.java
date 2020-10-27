package youdian.apk.ipqc.contract;


import androidx.databinding.ObservableArrayList;

import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import io.reactivex.rxjava3.core.Observable;
import youdian.apk.ipqc.base.BaseView;
import youdian.apk.ipqc.bean.HomeTableData;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.bean.SEData;
import youdian.apk.ipqc.obsever.HomeTableObsever;
import youdian.apk.ipqc.obsever.SEObsever;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/17 0017
 * Time: 下午 3:20
 * Function: ipqc主页契约接口，可以很直观的看到 M、V、P 层接口中提供的方法
 */
public interface TableListContract {

    interface IMainModel {
        Observable<Response<ListResponseData<SEData>>> getSEData(String org_code);

        Observable<Response<ListResponseData<HomeTableData>>> getFirstHomeTableData(String se_code);

        Observable<Response<ListResponseData<HomeTableData>>> getInstHomeTableData(String se_code);

    }

    interface View extends BaseView {
        //显示SE
        void setSEList(ObservableArrayList<SEObsever> seObservablelList);

        //主页显示表单列表
        void setFirstTableList(ObservableArrayList<HomeTableObsever> tableObseversList);
        void setInsTableList(ObservableArrayList<HomeTableObsever> tableObseversList);

    }


    interface Presenter {
        void getSeList(String org_code);

        void getFirstTableList(String se_code);
        void getInsTableList(String se_code);

    }


}
