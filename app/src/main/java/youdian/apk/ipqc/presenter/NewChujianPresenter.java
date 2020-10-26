package youdian.apk.ipqc.presenter;


import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import youdian.apk.ipqc.contract.NewChujianContract;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/25 0025
 * Time: 上午 9:14
 * Function:
 */
public class NewChujianPresenter extends BasePresenter<NewChujianContract.View> implements NewChujianContract.Presenter {

    private NewChujianContract.View view;

    public NewChujianPresenter(NewChujianContract.View view) {
        this.view = view;
        view.setPresenter(this);
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


}
