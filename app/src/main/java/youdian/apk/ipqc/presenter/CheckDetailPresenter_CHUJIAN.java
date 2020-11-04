package youdian.apk.ipqc.presenter;


import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import youdian.apk.ipqc.adapter.ActionDetailAdapter;
import youdian.apk.ipqc.base.BasePresenter;
import youdian.apk.ipqc.bean.FirstCheckList;
import youdian.apk.ipqc.bean.FirstCheckListItem;
import youdian.apk.ipqc.bean.FirstCheckProcess;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.contract.CheckDetailContract_CHUJIAN;
import youdian.apk.ipqc.obsever.CountModel;
import youdian.apk.ipqc.model.Model_Chujian_CheckDetail;
import youdian.apk.ipqc.network.RxScheduler;
import youdian.apk.ipqc.obsever.FirstCheckItemObserver;
import youdian.apk.ipqc.obsever.FirstCheckResultObserver;
import youdian.apk.ipqc.obsever.ProgressObserver;
import youdian.apk.ipqc.utils.Constans;
import youdian.apk.ipqc.utils.JsonFormatUtils;
import youdian.apk.ipqc.utils.MyUtils;
import youdian.apk.ipqc.utils.UserUtils;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/21 0021
 * Time: 下午 6:55
 * Function:
 */
public class CheckDetailPresenter_CHUJIAN extends BasePresenter<CheckDetailContract_CHUJIAN.View> implements CheckDetailContract_CHUJIAN.Presenter, ActionDetailAdapter.onCountChangeListener {

    private CheckDetailContract_CHUJIAN.IModel model;

    private List<FirstCheckProcess> firstCheckLists;
    private ObservableList<FirstCheckItemObserver> checkItemObservers;//全部检验项
    private ObservableList<FirstCheckItemObserver> onCheckItemList;//单个工序检验项
    private ObservableList<ProgressObserver> progressObserverList;
    private CountModel countModel = new CountModel();
    List<OptionData> suggestList = new ArrayList<>();//建议列表


    public CheckDetailPresenter_CHUJIAN() {
        this.model = new Model_Chujian_CheckDetail();
    }


    /**
     * 获取全部检验项
     *
     * @param first_checklist_id
     */
    @Override
    public void getCheckListData(String first_checklist_id) {
        if (!isViewAttached()) {
            return;
        }
        model.getCheckListData(first_checklist_id)
                .compose(RxScheduler.Obs_io_main())
//                .to(mView.bindAutoDispose())//解决内存泄漏
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

    /**
     * 获取检验建议
     *
     * @param option
     */
    @Override
    public void getCheckSuggestion(String option) {
        model.getSelectInfo(option)
                .compose(RxScheduler.Obs_io_main())
//                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<List<OptionData>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(@NonNull Response<List<OptionData>> listResponse) {
                        if (option.equals(Constans.FirstSug)) {
                            suggestList = listResponse.getData();
                            if (suggestList.size() <= 0)
                                mView.onError("建议内容为空");
                            else
                                mView.showBottomDialog(suggestList);

                        }
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

    /**
     * 底部弹窗
     */
    @Override
    public void showBottomDialog() {
    }

    /**
     * 提交记录
     *
     * @param firstCheckResultObserver
     */
    @Override
    public void postFirstResult(FirstCheckResultObserver firstCheckResultObserver) {
        String body = new Gson().toJson(firstCheckResultObserver);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);

        model.postFirstResult(requestBody)
                .compose(RxScheduler.Obs_io_main())
//                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<FirstCheckResultObserver>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(@NonNull Response<FirstCheckResultObserver> response) {
                        if (response.getCode() == 100)
                            mView.showPopWindow(true, "检验成功");
                        else
                            mView.onError(response.getMsg());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.hideLoading();
                        mView.showPopWindow(false, "数据提交失败");


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
//                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<List<ProgressObserver>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull Response<List<ProgressObserver>> listResponse) {
                        progressObserverList = new ObservableArrayList<>();
                        List<ProgressObserver> list = listResponse.getData();
                        for (int i = 0; i < list.size(); i++) {
                            progressObserverList.add(list.get(i));
                        }
                        mView.setprocess(progressObserverList);
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

    @Override
    public void getCheckedCount(int s) {

    }

    @Override
    public void getCheckDetail(FirstCheckItemObserver checkItemObserver) {

    }
    //FirstSug
}