package youdian.apk.ipqc.presenter;


import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import youdian.apk.ipqc.adapter.ActionDetailAdapter;
import youdian.apk.ipqc.adapter.InsCheckDetailAdapter;
import youdian.apk.ipqc.base.BasePresenter;
import youdian.apk.ipqc.bean.InsCheckList;
import youdian.apk.ipqc.bean.InsCheckListItem;
import youdian.apk.ipqc.bean.InsCheckProcess;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.contract.CheckDetailContract_XUNJIAN;
import youdian.apk.ipqc.model.Model_Xunjian_CheckDetail;
import youdian.apk.ipqc.network.RxScheduler;
import youdian.apk.ipqc.obsever.CountModel;
import youdian.apk.ipqc.obsever.FirstCheckItemObserver;
import youdian.apk.ipqc.obsever.InsCheckItemObserver;
import youdian.apk.ipqc.obsever.InsCheckResultObserver;
import youdian.apk.ipqc.obsever.ProgressObserver;
import youdian.apk.ipqc.utils.Constans;


/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/21 0021
 * Time: 下午 6:55
 * Function:
 */
public class CheckDetailPresenter_XUNJIAN extends BasePresenter<CheckDetailContract_XUNJIAN.View> implements CheckDetailContract_XUNJIAN.Presenter, InsCheckDetailAdapter.onCountChangeListener {

    private CheckDetailContract_XUNJIAN.IModel model;

    private List<InsCheckProcess> insCheckLists;
    private List<InsCheckItemObserver> checkItemObservers;//全部检验项
    private List<InsCheckItemObserver> onCheckItemList;//单个工序检验项
    private List<ProgressObserver> progressObserverList;
    private CountModel countModel = new CountModel();
    List<OptionData> suggestList = new ArrayList<>();//建议列表



    public CheckDetailPresenter_XUNJIAN() {
        this.model = new Model_Xunjian_CheckDetail();
    }


    /**
     * 获取全部检验项
     * @param ins_checklist_id
     */
    @Override
    public void getCheckListData(String ins_checklist_id) {
        if (!isViewAttached()) {
            return;
        }
        model.getCheckListData(ins_checklist_id)
                .compose(RxScheduler.Obs_io_main())
//                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<InsCheckList>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(@NonNull Response<InsCheckList> listResponse) {
                        insCheckLists = listResponse.getData().getInspection_checklist_processes();
                        //pipeishuju
                        dealCheckData(insCheckLists);
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
     * @param insCheckResultObserver
     */
    @Override
    public void postInsResult(InsCheckResultObserver insCheckResultObserver) {
       String body = new Gson().toJson(insCheckResultObserver);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);

        model.postInsResult(requestBody)
                .compose(RxScheduler.Obs_io_main())
//                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<InsCheckResultObserver>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(@NonNull Response<InsCheckResultObserver> response) {
                        if (response.getCode() == 100)
                            mView.showPopWindow(true, "检验成功");
                        else
                            mView.onError(response.getMsg());


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.hideLoading();
                        mView.showPopWindow(false,"数据提交失败");}


                        @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });

    }


    private void dealCheckData(List<InsCheckProcess> list) {
        checkItemObservers = new ArrayList<>();
        for (InsCheckProcess insCheckProcess : list) {
            for (InsCheckListItem insCheckListItem : insCheckProcess.getIns_checklist_items()) {
                checkItemObservers.add(new InsCheckItemObserver(insCheckProcess, insCheckListItem));
            }
        }
        mView.setCheckListData(checkItemObservers);
    }


    @Override
    public void getProcess(String ins_checklist_id) {
        if (!isViewAttached()) {
            return;
        }
        model.getProcess(ins_checklist_id)
                .compose(RxScheduler.Obs_io_main())
//                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<List<ProgressObserver>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull Response<List<ProgressObserver>> listResponse) {
                        progressObserverList = new ArrayList<>();
//                        List<ProgressObserver> list =  listResponse.getData();
//                        for (int i = 0; i < list.size() ; i++) {
//                            progressObserverList.add(list.get(i));
//                        }
                        progressObserverList = listResponse.getData();
                        ProgressObserver progressObserver = new ProgressObserver(100000,"抽样",Constans.ChouYang,"","抽样检验");
                        progressObserverList.add(progressObserver);
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
    public void getCheckDetail(InsCheckItemObserver checkItemObserver) {

    }


}