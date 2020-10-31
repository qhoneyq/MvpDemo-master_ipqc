package youdian.apk.ipqc.presenter;


import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import youdian.apk.ipqc.base.BasePresenter;
import youdian.apk.ipqc.bean.Lines;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.contract.NewChujianContract;
import youdian.apk.ipqc.contract.NewXunjianContract;
import youdian.apk.ipqc.model.NewChujianModel;
import youdian.apk.ipqc.network.RxScheduler;
import youdian.apk.ipqc.utils.Constans;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/25 0025
 * Time: 上午 9:14
 * Function:
 */
public class NewXunjianPresenter extends BasePresenter<NewXunjianContract.View> implements NewXunjianContract.Presenter {

    NewChujianContract.IMainModel model;

    public NewXunjianPresenter() {
        model = new NewChujianModel();
    }

    List<Lines> linesList = new ArrayList<>();//line
    List<OptionData> checkTypeList = new ArrayList<>();//初见类型

    /**
     * 获取线别
     *
     * @param se_code
     */
    @Override
    public void getLines(String se_code) {
        if (!isViewAttached()) {
            return;
        }
        model.getLines(se_code)
                .compose(RxScheduler.Obs_io_main())
//                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<ListResponseData<Lines>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull Response<ListResponseData<Lines>> listResponseDataResponse) {
                        linesList = listResponseDataResponse.getData().getResults();
//                    mView.setLines(linesList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.showError(Constans.FLAG_LINE, e.getMessage());
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
//        linesList = new ArrayList<>();
//        linesList.add(new Lines(1,"se","se","org","line1","111"));
//        linesList.add(new Lines(2,"se","se","org","line2","222"));
//        linesList.add(new Lines(3,"se","se","org","line3","333"));
//        linesList.add(new Lines(4,"se","se","org","line4","444"));
    }

    //获取通用类型
    @Override
    public void getSelectInfo(String selectinfo) {
        model.getSelectInfo(selectinfo)
                .compose(RxScheduler.Obs_io_main())
//                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<List<OptionData>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(@NonNull Response<List<OptionData>> listResponse) {
                        checkTypeList = listResponse.getData();
                        if (checkTypeList.size() < 0)
                            mView.showError(Constans.FLAG_LINE, "数值为空");
                        else
                            mView.showCheckTypeBottomDialog(checkTypeList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.hideLoading();

                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
//        checkTypeList = new ArrayList<>();
//        checkTypeList.add(new OptionData("q", "Q"));
//        checkTypeList.add(new OptionData("w", "W"));
//        checkTypeList.add(new OptionData("e", "E"));
//        checkTypeList.add(new OptionData("a", "A"));
//        checkTypeList.add(new OptionData("s", "S"));
//        mView.showCheckTypeBottomDialog(checkTypeList);

    }

    /**
     * 显示下拉选项
     *
     * @param flag
     */
    @Override
    public void showBottomDialog(int flag) {
        switch (flag) {
            case Constans.FLAG_LINE:
                if (linesList.size() > 0)
                    mView.showLineBottomDialog(linesList);
                else
                    mView.showError(flag, "没有可选线别");
                break;

        }

    }

}
