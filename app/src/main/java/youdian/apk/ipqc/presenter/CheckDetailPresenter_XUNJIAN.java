package youdian.apk.ipqc.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import android.util.Log;

import com.foxconn.youdian.youdian.adapter.Ipqc_Chouyang_Adapter;
import com.foxconn.youdian.youdian.bean.Currency;
import com.foxconn.youdian.youdian.ipqc.contract.CheckDetailContract_XUNJIAN;
import com.foxconn.youdian.youdian.ipqc.model.CheckAction;
import com.foxconn.youdian.youdian.ipqc.model.CheckDetail;
import com.foxconn.youdian.youdian.ipqc.model.CheckResult;
import com.foxconn.youdian.youdian.ipqc.model.Chouyang;
import com.foxconn.youdian.youdian.ipqc.model.IpqcAction;
import com.foxconn.youdian.youdian.sqlite.DBServer;
import com.foxconn.youdian.youdian.utils.Constans;
import com.foxconn.youdian.youdian.utils.MyUtils_IPQC;
import com.foxconn.youdian.youdian.utils.MyUtils_P;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/10/15 0015
 * Time: 上午 11:53
 * Function:
 */
public class CheckDetailPresenter_XUNJIAN implements CheckDetailContract_XUNJIAN.Presenter, Ipqc_Chouyang_Adapter.onCountChangeListener {
    private CheckDetailContract_XUNJIAN.View view;
    //    List<String> list_gongxu_name = new ArrayList<>();
    Ipqc_Chouyang_Adapter chouyang_adapter;
    List<Currency> list_gongxu = new ArrayList<>();
    List<IpqcAction> list_action = new ArrayList<>();
    List<CheckAction> list_action_detail = new ArrayList<>();//点检动作结果保存
    List<Chouyang> list_chouyang_detail = new ArrayList<>();//点检动作结果保存
    List<CheckDetail> list_detail = new ArrayList<>();  //暂存点检动作列表
    private String resultstatus = "True";           //点检状态（True：正常；false：异常。默认：true）
    private int result_id = -1;
    private Context context;
    private Ipqc_Chouyang_Adapter.onCountChangeListener oncountchangeListener;
    String acation_count = "0";

    public CheckDetailPresenter_XUNJIAN(CheckDetailContract_XUNJIAN.View view, Context context) {
        this.view = view;
        this.context = context;
        this.oncountchangeListener = this;
        view.setPresenter(this);
        list_chouyang_detail = new ArrayList<>();
    }


    @Override
    public void start() {
        view.showLoading();
        list_chouyang_detail = new ArrayList<>();
    }

    /**
     * 确定查核时机后，查询是否存在缓存记录
     *
     * @param dbServer
     */
    @Override
    public void isExitTemp(DBServer dbServer, String time, String table_id, String rate) {
        String result_id_temp = dbServer.isExitTemp_Xunjian(time, table_id, rate);
        if (!result_id_temp.equals("")) {
            //存在暂存数据，弹窗，选择是否恢复暂存
            //是：恢复
            //否：需要时带出
            view.showDialogRecover(result_id_temp);
        } else {
            //带出
            view.getHistoryData();

        }

    }

    /**
     * 获取暂存数据并恢复
     */
    @Override
    public void recoverData(SQLiteDatabase sqLiteDatabase, final DBServer dbServer, final String table_id, final String rate, final String log_type, final boolean isRecoverChouyang) {
        list_action_detail.clear();
        list_chouyang_detail.clear();
        view.showLoading();
        Observable.create(new ObservableOnSubscribe<List<CheckAction>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<CheckAction>> e) throws Exception {

                e.onNext(list_action_detail);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CheckAction>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        list_action_detail = dbServer.getDetailTemp_xunjian(table_id, rate, log_type);
                        if (isRecoverChouyang)
                            list_chouyang_detail = dbServer.getChouyangDetail_Temp(table_id, rate);

                        Log.d("recoverData", d.toString());
                    }

                    @Override
                    public void onNext(List<CheckAction> strings) {

                        view.setActionList(list_action, list_action_detail);
//                        view.setChouyangList(list_chouyang_detail);
                        chouyang_adapter = new Ipqc_Chouyang_Adapter(list_chouyang_detail, oncountchangeListener, context);
                        view.setChouyangList(chouyang_adapter);
                        view.setActionNum(list_action.size() + "");
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });
    }

    @Override
    public void gethistoryData(SQLiteDatabase sqLiteDatabase, DBServer dbServer, String table_id, String[] cycle) {

    }


    @Override
    public void getGongxuList(SQLiteDatabase sqLiteDatabase, String table_id) {
        list_gongxu.clear();
//        list_gongxu_name.clear();
        list_gongxu = MyUtils_IPQC.getGongXuList(sqLiteDatabase, Constans.IPQC_XUNJIAN, table_id);

        Observable.create(new ObservableOnSubscribe<List<Currency>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Currency>> e) throws Exception {
//                for (Currency c : list_gongxu) {
//                    list_gongxu_name.add(c.getName());
//                }
                e.onNext(list_gongxu);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Currency>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("GETDATA", d.toString());
                    }

                    @Override
                    public void onNext(List<Currency> strings) {
                        view.hideLoading();
                        view.setGongxuList(list_gongxu);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });
    }


    /**
     * 根据工序id 获取action列表
     *
     * @param sqLiteDatabase
     * @param process_id
     */
    @Override
    public void getAction(SQLiteDatabase sqLiteDatabase, String process_id, String rate) {
        list_action.clear();
        list_action = MyUtils_IPQC.getXunjianAction(sqLiteDatabase, process_id, rate);

        Observable.create(new ObservableOnSubscribe<List<IpqcAction>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<IpqcAction>> e) throws Exception {

                e.onNext(list_action);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<IpqcAction>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("getChujianList", d.toString());
                    }

                    @Override
                    public void onNext(List<IpqcAction> strings) {
                        view.hideLoading();
//                        view.setListItem(list_ChujainList_name);
                        view.setActionList(list_action, list_action_detail);
                        view.setActionNum(list_action.size() + "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });
    }

    /**
     * 获取暂存抽样
     * 抽样记录暂存或提交后才会存库，否则只存list
     *
     * @param dbServer
     * @param result_id
     */
    @Override
    public void getChouyangDetail(DBServer dbServer, String result_id) {
//        list_chouyang_detail = dbServer.getChouyangDetail(result_id);
//        if (list_chouyang_detail!=null && list_chouyang_detail.size()>0){
//        view.setChouyangList(list_chouyang_detail);
        chouyang_adapter = new Ipqc_Chouyang_Adapter(list_chouyang_detail, oncountchangeListener, context);
        view.setChouyangList(chouyang_adapter);        //        }
    }

    /**
     * 抽样列表缓存
     *
     * @param sn
     * @param result
     */
    @Override
    public void saveChouyangDetail(String sn, String check_time, String result, String note, String rate) {
        if (list_chouyang_detail != null && list_chouyang_detail.size() > 0) {
            boolean isExit = false;
            for (Chouyang chouyang : list_chouyang_detail) {
                if (sn.equals(chouyang.getSn())) {
                    //已存在，更新
                    chouyang.setCheckResult(result);
                    chouyang.setCheckValue(result);
                    chouyang.setCheck_time(check_time);
                    chouyang.setNote(note);
                    isExit = true;
                    break;
                }
            }
            if (!isExit)
                list_chouyang_detail.add(new Chouyang(null, sn, check_time, result, result, rate, note, ""));
        } else
            list_chouyang_detail.add(new Chouyang(null, sn, check_time, result, result, rate, note, ""));
        chouyang_adapter = new Ipqc_Chouyang_Adapter(list_chouyang_detail, oncountchangeListener, context);
        view.setChouyangList(chouyang_adapter);
    }


    /**
     * 获取点检动作结果，并保存
     * 如果已经有点检动作列表，先遍历查看，避免重复保存
     * 如果不存在，则新增
     *
     * @param action_id
     * @param desc
     * @param action_result
     * @param note
     */
    @Override
    public void getCheckAction(Object action_id, String desc, String action_result, String note,
                               String action_cycle, String action_value, String time, String process_id, String process_name) {
        if (list_action_detail.size() > 0) {
            boolean isChecked = false;
            //遍历动作记录列表，确认是否已经点检过。已经存在记录，则更新；第一次点检，则添加。
            for (CheckAction checkAction : list_action_detail) {
                if (checkAction.getAction_id().equals(action_id) && checkAction.getProcess_id().equals(process_id)) {
                    checkAction.setDesc(desc);
                    checkAction.setAction_result(action_result);
                    checkAction.setAction_cycle(action_cycle);
                    checkAction.setAction_value(action_value);
                    checkAction.setAction_time(time);
                    checkAction.setNote(note);
                    isChecked = true;
                }
            }
            if (!isChecked) {//不存在动作点检记录，新增
                list_action_detail.add(new CheckAction(action_id, desc, note, action_result, action_cycle, action_value, time, process_id, process_name));
            }
        } else {//点检动作记录表为空
            list_action_detail.add(new CheckAction(action_id, desc, note, action_result, action_cycle, action_value, time, process_id, process_name));
        }
    }


    /**
     * 数据暂存 or 提交保存
     *
     * @param localData
     * @param dbServer
     * @param table_id
     * @param flag      区分暂存或保存
     */
    @Override
    public void commit(SQLiteDatabase localData, DBServer dbServer, String table_id, String flag, String rate) {
        //存在点检动作记录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        if (list_action_detail.size() > 0 || list_chouyang_detail.size()>0) {
            //遍历点检动作状态，如果存在异常，更新设备status
            resultstatus = "True";
            for (int i = 0; i < list_action_detail.size(); i++) {
                while (list_action_detail.get(i).getAction_result().equals("False")) {
                    //存在异常记录
                    resultstatus = "False";
                    break;
                }
            }
            for (int i = 0; i < list_chouyang_detail.size(); i++) {
                while (list_chouyang_detail.get(i).getCheckResult().equals("False")) {
                    //存在异常记录
                    resultstatus = "False";
                    break;
                }
            }
            CheckResult checkResult = new CheckResult(null, time, resultstatus, "", rate, flag, Integer.parseInt(table_id));
            result_id = dbServer.addIpqcRecord(checkResult, result_id, false);
            list_detail = new ArrayList<>();
            for (CheckAction checkAction : list_action_detail) {
                dbServer.addIpqcDetail(new CheckDetail(null, checkAction.getProcess_id(), checkAction.getProcess_name(), checkAction.getAction_id().toString(), checkAction.getDesc(), checkAction.getAction_cycle(), checkAction.getAction_result(),
                        checkAction.getAction_value(), checkAction.getNote(), MyUtils_P.getTime(context), result_id));
            }
            if (list_chouyang_detail != null &&
                    list_chouyang_detail.size() > 0) {
                for (Chouyang chouyang : list_chouyang_detail) {
                    dbServer.addIpqcChouyangDetail(new Chouyang(null, chouyang.getSn(), MyUtils_P.getTime(context), chouyang.getCheckResult(), chouyang.getCheckValue(), chouyang.getRate(), chouyang.getNote(), result_id + ""));
                }
            }
            if (flag.equals(Constans.IPQC_RESTORE_COMMIT))
                //更新表头到提交
                dbServer.updateTableType(table_id, Constans.IPQC_RESTORE_COMMIT);
            else if (flag.equals(Constans.IPQC_RESTORE_TEMP)){
                dbServer.updateTableType(table_id, Constans.IPQC_RESTORE_TEMP);

            }

        }
    }

    @Override
    public void getChouyangCount(int s) {
        acation_count = s + "";
        view.setActionNum(acation_count);
        view.setCheckedNum(acation_count);
    }

    @Override
    public void setResult(int posiition, String result) {
        list_chouyang_detail.get(posiition).setCheckResult(result);
        list_chouyang_detail.get(posiition).setCheckValue(result);
    }
}
