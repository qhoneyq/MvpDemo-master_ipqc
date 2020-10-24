package youdian.apk.ipqc.presenter;


import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import com.foxconn.youdian.youdian.ipqc.contract.NewChujianContract;
import com.foxconn.youdian.youdian.ipqc.contract.NewChujianContract.Presenter;
import com.foxconn.youdian.youdian.ipqc.model.TableaTitle_Chujian;
import com.foxconn.youdian.youdian.sqlite.DBServer;
import com.foxconn.youdian.youdian.utils.Constans;


/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/25 0025
 * Time: 上午 9:14
 * Function:
 */
public class NewChujianPresenter implements Presenter {

    private NewChujianContract.View view;

    public NewChujianPresenter(NewChujianContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        view.showSfc();
    }

    @Override
    public void handlerSN() {
//
//        if (view != null) {
//            view.showLoading();
//        }
//
//        mModel.getSFC(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                view.hideLoading();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String content = response.body().string();
//                if (view != null) {
////                    view.succes(content);
////                    getChujianTableList();
//                    view.hideLoading();
////                    view.showSfc();
//                }
//            }
//        });

    }

    /**
     *
     */
    @Override
    public void showDialog(DBServer dbServer, String line_code, String table_no, String time, Drawable drawable) {
        String table_id = dbServer.getTableId_temp(line_code, table_no, time, Constans.IPQC_XUNJIAN);
        if (table_id != null) {
            //存在该line_code的表头记录
            view.showDialog(drawable, "已存在该线别的表头信息", "恢复表头信息查看或修改", table_id);
        } else {
            if (view.isShowingDialog()) {
                view.hideDialog();
            }
        }
    }


    @Override
    public void getTableTitle(DBServer dbServer, String table_id) {
        TableaTitle_Chujian tableaTitle = dbServer.get_tabletitle(table_id);
        view.recoverTable(tableaTitle);
    }

    @Override
    public void recoverData(SQLiteDatabase sqLiteDatabase, DBServer dbServer, String result_id) {

    }

}
