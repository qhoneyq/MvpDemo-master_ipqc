package youdian.apk.ipqc.presenter;


import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;

import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import youdian.apk.ipqc.bean.HomeTableData;
import youdian.apk.ipqc.bean.SEData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import youdian.apk.ipqc.base.BasePresenter;
import youdian.apk.ipqc.bean.KVEntity;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.contract.ChujianContract;
import youdian.apk.ipqc.model.ChujianModel;
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
public class ChujianPresenter extends BasePresenter<ChujianContract.View> implements ChujianContract.Presenter {

    private ChujianContract.IMainModel model;
    private ObservableArrayList<SEObsever> seObservablelList;
    private ObservableArrayList<HomeTableObsever> tableObseversList;

    //初件表单
    List<KVEntity> list_se = new ArrayList<>();
    List<KVEntity> list_ChujainList = new ArrayList<>();
    List<String> list_ChujainList_name = new ArrayList<>();
    //巡检表单
    List<KVEntity> list_XunjianList = new ArrayList<>();
    List<String> list_XunjianList_name = new ArrayList<>();

//    //初件检验记录表
//    List<CheckLog> list_chujianrecord = neww ArrayList<>();
//
//    //巡检检验记录表
//    list_chujianrecordjianrecordst<CheckLog> list_xunjianrecord = new ArrayList<>();

    public ChujianPresenter() {
        model = new ChujianModel();
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
                .to(mView.bindAutoDispose())//解决内存泄漏
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

    @Override
    public void getTableList(String se_code,  String flag) {
        model.getHomeTableData(se_code)
                .compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<Response<ListResponseData<HomeTableData>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(Response<ListResponseData<HomeTableData>> listResponseDataResponse) {
                        try {
                            List<HomeTableData> tableDataList = listResponseDataResponse.getData().getResults();
                            tableObseversList = new ObservableArrayList<>();
                            if (tableDataList!=null&&tableDataList.size()>0){
                                for (HomeTableData tableData:tableDataList){
                                    tableObseversList.add(new HomeTableObsever(tableData));
                                }
                            }
                            mView.setTableList(tableObseversList);
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

//    /*
//     * 获取制程数据后，请求该制程表单数据
//     */
//    @Override
//    public void getChujianTableList(SQLiteDatabase sqLiteDatabase, String se_id) {
//        list_ChujainList.clear();
//        list_ChujainList = MyUtils_IPQC.getChujianTable(sqLiteDatabase, se_id);
//        list_ChujainList_name.clear();
//        for (Currency c : list_ChujainList) {
//            list_ChujainList_name.add(c.getName());
//        }
//        Observable.create(new ObservableOnSubscribe<List<String>>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
////                view.showLoading();
//                e.onNext(list_ChujainList_name);
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<String>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.d("getChujianList", d.toString());
//                    }
//
//                    @Override
//                    public void onNext(List<String> strings) {
////                        view.hideLoading();
////                        view.setListItem(list_ChujainList_name);
//                        view.setChujianList(list_ChujainList_name);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        view.hideLoading();
//                        view.failed();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        view.hideLoading();
//                    }
//                });
//    }
//
//    /**
//     * 获取巡检表单
//     *
//     * @param sqLiteDatabase
//     * @param se_id
//     */
//    @Override
//    public void getXunjianTableList(SQLiteDatabase sqLiteDatabase, String se_id) {
//        list_XunjianList.clear();
//        list_XunjianList = MyUtils_IPQC.getXunjianTable(sqLiteDatabase, se_id);
//        list_XunjianList_name.clear();
//        for (Currency c : list_XunjianList) {
//            list_XunjianList_name.add(c.getName());
//        }
//        Observable.create(new ObservableOnSubscribe<List<String>>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
////                view.showLoading();
//                e.onNext(list_XunjianList_name);
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<String>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.d("getChujianList", d.toString());
//                    }
//
//                    @Override
//                    public void onNext(List<String> strings) {
////                        view.hideLoading();
////                        view.setListItem(list_XunjianList_name);
//                        view.setChujianList(list_XunjianList_name);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
////                        view.hideLoading();
//
//                    }
//
//                    @Override
//                    public void onComplete() {
////                        view.hideLoading();
//                    }
//                });
//    }
//
//    /**
//     * 初件检验表点击单项，跳转新建页面
//     *
//     * @param position
//     */
//    @Override
//    public void itemclick(int position, int category,String flag) {
//        if (category == Constans.IPQC_XUNJIAN)
//            view.onMainItemClick(list_XunjianList.get(position));
//        else if (category == Constans.IPQC_CHUJIAN && flag.equals(Constans.ACTIVITY_MAIN))
//            view.onMainItemClick(list_ChujainList.get(position));
//
//    }
//
//    /**
//     * 获取初件检验记录列表
//     *
//     * @param dbServer
//     * @param se_id
//     */
//    @Override
//    public void getRecordlist_chujian(DBServer dbServer, String se_id, int category) {
//        list_xunjianrecord = dbServer.getChujianRecordList(se_id, category);
//        Observable.create(new ObservableOnSubscribe<List<CheckLog>>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<List<CheckLog>> e) throws Exception {
////                view.showLoading();
//                e.onNext(list_xunjianrecord);
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<CheckLog>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.d("getXunjianList", d.toString());
//                    }
//
//                    @Override
//                    public void onNext(List<CheckLog> strings) {
//                        view.setChujianList(list_xunjianrecord);
////                        view.hideLoading();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
////                        view.hideLoading();
//                    }
//
//                    @Override
//                    public void onComplete() {
////                        view.hideLoading();
//                    }
//                });
//    }
//
//    @Override
//    public void getRecordlist_xunjian(DBServer dbServer, String se_id, int category) {
//        list_chujianrecord = dbServer.getChujianRecordList(se_id, category);
//        Observable.create(new ObservableOnSubscribe<List<CheckLog>>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<List<CheckLog>> e) throws Exception {
////                view.showLoading();
//                e.onNext(list_chujianrecord);
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<CheckLog>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.d("getChujianList", d.toString());
//                    }
//
//                    @Override
//                    public void onNext(List<CheckLog> strings) {
//                        view.setXunjian(list_chujianrecord);
////                        view.hideLoading();
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ;
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        ;
//                    }
//                });
//    }


    }
