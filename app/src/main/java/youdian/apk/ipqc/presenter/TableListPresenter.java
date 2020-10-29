package youdian.apk.ipqc.presenter;


import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;

import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import youdian.apk.ipqc.bean.HomeTableData;
import youdian.apk.ipqc.bean.SEData;

import java.util.List;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import youdian.apk.ipqc.base.BasePresenter;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.contract.TableListContract;
import youdian.apk.ipqc.model.TableListModel;
import youdian.apk.ipqc.network.RxScheduler;
import youdian.apk.ipqc.obsever.HomeTableObsever;
import youdian.apk.ipqc.obsever.SEObsever;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/17 0017
 * Time: 下午 3:30
 * Function:
 */
public class TableListPresenter extends BasePresenter<TableListContract.View> implements TableListContract.Presenter {

    private TableListContract.IMainModel model;
    private ObservableArrayList<SEObsever> seObservablelList;
    private ObservableArrayList<HomeTableObsever> firstTableObseversList;
    private ObservableArrayList<HomeTableObsever> insTableObseversList;


    public TableListPresenter() {
        model = new TableListModel();
    }


    /**
     * 获取制程数据
     */
    @Override
    public void getSeList(final String org_code) {
        if (!isViewAttached()) {
            return;
        }
        model.getSEData(org_code)
                .compose(RxScheduler.Obs_io_main())
//                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<ListResponseData<SEData>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }


                    @Override
                    public void onNext(Response<ListResponseData<SEData>> listResponseDataResponse) {
                        try {
                            List<SEData> seDataList = listResponseDataResponse.getData().getResults();
                            seObservablelList = new ObservableArrayList<>();
                            if (seDataList!=null&&seDataList.size()>0){
                                for (SEData seData:seDataList){
                                    seObservablelList.add(new SEObsever(seData));
                                }
                            }
                            mView.setSEList(seObservablelList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.onError(e.getMessage());
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

    /**
     * 获取初件检验表列表
     * @param se_code
     */
    @Override
    public void getFirstTableList(String se_code) {
        model.getFirstHomeTableData(se_code)
                .compose(RxScheduler.Obs_io_main())
//                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<ListResponseData<HomeTableData>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(Response<ListResponseData<HomeTableData>> listResponseDataResponse) {
                        try {
                            List<HomeTableData> tableDataList = listResponseDataResponse.getData().getResults();
                            firstTableObseversList = new ObservableArrayList<>();
                            if (tableDataList!=null&&tableDataList.size()>0){
                                for (HomeTableData tableData:tableDataList){
                                    firstTableObseversList.add(new HomeTableObsever(tableData));
                                }
                            }
                            mView.setTableList(firstTableObseversList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.onError(e.getMessage());
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }


    /**
     * 获巡检件检验表列表
     * @param se_code
     */
    @Override
    public void getInsTableList(String se_code) {
        model.getInstHomeTableData(se_code)
                .compose(RxScheduler.Obs_io_main())
//                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<ListResponseData<HomeTableData>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(Response<ListResponseData<HomeTableData>> listResponseDataResponse) {
                        try {
                            List<HomeTableData> tableDataList = listResponseDataResponse.getData().getResults();
                            insTableObseversList = new ObservableArrayList<>();
                            if (tableDataList!=null&&tableDataList.size()>0){
                                for (HomeTableData tableData:tableDataList){
                                    insTableObseversList.add(new HomeTableObsever(tableData));
                                }
                            }
                            mView.setTableList(insTableObseversList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.onError(e.getMessage());
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }


    }
