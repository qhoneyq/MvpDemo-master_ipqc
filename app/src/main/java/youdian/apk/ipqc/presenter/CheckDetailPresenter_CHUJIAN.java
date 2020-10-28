package youdian.apk.ipqc.presenter;


import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import youdian.apk.ipqc.base.BasePresenter;
import youdian.apk.ipqc.bean.FirstCheckList;
import youdian.apk.ipqc.bean.FirstCheckListItem;
import youdian.apk.ipqc.bean.FirstCheckProcess;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.contract.CheckDetailContract_CHUJIAN;
import youdian.apk.ipqc.model.Model_Chujian_CheckDetail;
import youdian.apk.ipqc.network.RxScheduler;
import youdian.apk.ipqc.obsever.FirstCheckItemObserver;
import youdian.apk.ipqc.obsever.FirstCheckResultObserver;
import youdian.apk.ipqc.obsever.ProgressObserver;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/21 0021
 * Time: 下午 6:55
 * Function:
 */
public class CheckDetailPresenter_CHUJIAN extends BasePresenter<CheckDetailContract_CHUJIAN.View> implements CheckDetailContract_CHUJIAN.Presenter {

    private CheckDetailContract_CHUJIAN.IModel model;

    private List<FirstCheckProcess> firstCheckLists;
    private ObservableList<FirstCheckItemObserver> checkItemObservers;
    private ObservableList<ProgressObserver> progressObserverList;

    public CheckDetailPresenter_CHUJIAN() {
        this.model = new Model_Chujian_CheckDetail();
    }


    @Override
    public void getCheckListData(String first_checklist_id) {
        if (!isViewAttached()) {
            return;
        }
        model.getCheckListData(first_checklist_id)
                .compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<FirstCheckList>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(@NonNull Response<FirstCheckList> listResponse) {
                        firstCheckLists = listResponse.getData().getFirst_checklist_processes();
                        //pipeishuju
                        dealCheckData(firstCheckLists);

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
    }


    private void dealCheckData(List<FirstCheckProcess> list) {
        checkItemObservers = new ObservableArrayList<>();
        for (FirstCheckProcess firstCheckProcess : list) {
            for (FirstCheckListItem firstCheckListItem : firstCheckProcess.getFirst_checklist_items()) {
                checkItemObservers.add(new FirstCheckItemObserver(firstCheckProcess, firstCheckListItem));
            }
        }
        mView.setCheckListData(checkItemObservers);
    }


    @Override
    public void getProcess(String first_checklist_id) {
        if (!isViewAttached()) {
            return;
        }
        model.getProcess(first_checklist_id)
                .compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<ListResponseData<ProgressObserver>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(@NonNull Response<ListResponseData<ProgressObserver>> listResponse) {
                        progressObserverList = listResponse.getData().getFirst_checklist_processes();

                        mView.setProgress(progressObserverList);
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
    }
}