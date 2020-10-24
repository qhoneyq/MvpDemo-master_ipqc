package youdian.apk.ipqc.contract;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import com.foxconn.youdian.youdian.ipqc.model.TableaTitle_Chujian;
import com.foxconn.youdian.youdian.ipqc.presenter.BasePresenter;
import com.foxconn.youdian.youdian.ipqc.view.BaseView;
import com.foxconn.youdian.youdian.sqlite.DBServer;

import okhttp3.Callback;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/25 0025
 * Time: 上午 9:15
 * Function:
 */
public interface NewChujianContract {
    interface IMainModel {

        void getSFC(Callback callback);

    }

    interface View extends BaseView<Presenter> {

        void showLoading();
        void hideLoading();

        void  showSfc();
        void hideSfc();


        void showDialog(Drawable drawable, String contentmsg, String confirm_text, String table_id);
        void hideDialog();
        boolean isShowingDialog();
        void failed();
        void recoverTable(TableaTitle_Chujian tabletitle);


    }


    interface Presenter extends BasePresenter {

        void handlerSN();
        void showDialog(DBServer dbServer, String line_code, String table_no, String time, Drawable drawable);
        void getTableTitle(DBServer dbServer, String table_id);
        void recoverData(SQLiteDatabase sqLiteDatabase, DBServer dbServer, String result_id);
        }
}
