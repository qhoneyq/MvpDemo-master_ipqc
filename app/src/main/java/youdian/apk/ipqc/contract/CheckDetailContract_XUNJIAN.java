package youdian.apk.ipqc.contract;

import android.database.sqlite.SQLiteDatabase;

import com.foxconn.youdian.youdian.adapter.Ipqc_Chouyang_Adapter;
import com.foxconn.youdian.youdian.bean.Currency;
import com.foxconn.youdian.youdian.ipqc.model.CheckAction;
import com.foxconn.youdian.youdian.ipqc.model.IpqcAction;
import com.foxconn.youdian.youdian.ipqc.presenter.BasePresenter;
import com.foxconn.youdian.youdian.ipqc.view.BaseView;
import com.foxconn.youdian.youdian.sqlite.DBServer;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/10/15 0015
 * Time: 上午 11:52
 * Function:
 */
public interface CheckDetailContract_XUNJIAN {

    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();

        void setGongxuList(List<Currency> data);
        void setChouyangList(Ipqc_Chouyang_Adapter chouyang_adapter);
//        void setChouyangList(List<Chouyang> data);
        void setActionNum(String data);
        void setCheckedNum(String data);

        void setActionList(List<IpqcAction> list, List<CheckAction> list_detail);

        void showDialogRecover(String result_id);
        void getHistoryData();
    }


    interface Presenter extends BasePresenter {
        void isExitTemp(DBServer dbServer, String time, String table_id, String rate);
        void recoverData(SQLiteDatabase sqLiteDatabase, DBServer dbServer, String result_id, String rate, String log_type, boolean isRecoverChouyang);
        void gethistoryData(SQLiteDatabase sqLiteDatabase, DBServer dbServer, String table_id, String[] cycle);
        void getGongxuList(SQLiteDatabase sqLiteDatabase, String table_id);
        void getAction(SQLiteDatabase sqLiteDatabase, String process_id, String rate);
        void getChouyangDetail(DBServer dbServer, String result_id);
       void saveChouyangDetail(String sn, String check_time, String result, String note, String rate);
        void commit(SQLiteDatabase sqLiteDatabase, DBServer dbServer, String table_id, String flag, String rate);
        void getCheckAction(Object action_id, String desc, String action_result, String note, String action_cycle,
                            String action_value, String time, String process_id, String process_name);
    }
}
