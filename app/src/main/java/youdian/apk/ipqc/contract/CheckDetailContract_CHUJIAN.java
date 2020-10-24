package youdian.apk.ipqc.contract;

import android.database.sqlite.SQLiteDatabase;


import com.foxconn.youdian.apk.ipqc.activity.BaseView;
import com.foxconn.youdian.apk.ipqc.presenter.BasePresenter;
import com.foxconn.youdian.youdian.bean.CheckAction;
import com.foxconn.youdian.youdian.bean.Currency;
import com.foxconn.youdian.youdian.ipqc.model.IpqcAction;
import com.foxconn.youdian.youdian.sqlite.DBServer;

import java.util.List;

import okhttp3.Callback;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/21 0021
 * Time: 下午 6:50
 * Function:
 */
public interface CheckDetailContract_CHUJIAN {

    interface IModel {
        void getdata(Callback callback);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();

        void setGongxuList(List<Currency> data);
        void setActionNum(String data);

        void setActionList(List<IpqcAction> list, List<CheckAction> list_detail);

    }


    interface Presenter extends BasePresenter {

        void recoverData(SQLiteDatabase sqLiteDatabase, DBServer dbServer, String result_id);
        void getGongxuList(SQLiteDatabase sqLiteDatabase, String table_id);
        void getAction(SQLiteDatabase sqLiteDatabase, String process_id);
//        void tempstore(SQLiteDatabase sqLiteDatabase, DBServer dbServer,String table_id);
        void commit(SQLiteDatabase sqLiteDatabase, DBServer dbServer, String table_id, String flag);
        void getCheckAction(Object action_id, String desc, String action_result, String note, String action_cycle,
                            String action_value, String time, String process_id, String process_name);
    }
}
