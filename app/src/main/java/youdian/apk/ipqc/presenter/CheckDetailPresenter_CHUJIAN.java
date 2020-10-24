package youdian.apk.ipqc.presenter;

import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import android.util.Log;

import com.foxconn.youdian.youdian.bean.Currency;
import com.foxconn.youdian.youdian.ipqc.contract.CheckDetailContract_CHUJIAN;
import com.foxconn.youdian.youdian.ipqc.model.CheckAction;
import com.foxconn.youdian.youdian.ipqc.model.CheckDetail;
import com.foxconn.youdian.youdian.ipqc.model.CheckResult;
import com.foxconn.youdian.youdian.ipqc.model.IpqcAction;
import com.foxconn.youdian.youdian.sqlite.DBServer;
import com.foxconn.youdian.youdian.utils.Constans;
import com.foxconn.youdian.youdian.utils.MyUtils_IPQC;

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
 * Date: 2019/9/21 0021
 * Time: 下午 6:55
 * Function:
 */
public class CheckDetailPresenter_CHUJIAN implements CheckDetailContract_CHUJIAN.Presenter {

    private CheckDetailContract_CHUJIAN.IModel iModel;
    private CheckDetailContract_CHUJIAN.View view;
    List<Currency> list_gongxu = new ArrayList<>();
    List<IpqcAction> list_action = new ArrayList<>();
    List<CheckAction> list_action_detail = new ArrayList<>();//点检动作结果保存
    List<CheckDetail> list_detail = new ArrayList<>();  //暂存点检动作列表
    private String resultstatus = "True";           //点检状态（True：正常；false：异常。默认：true）
    private int result_id = -1;

    public CheckDetailPresenter_CHUJIAN(CheckDetailContract_CHUJIAN.View view) {
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void start() {
        view.showLoading();
    }


    /**
     * 获取暂存数据并恢复
     */
    @Override
    public void recoverData(SQLiteDatabase sqLiteDatabase, final DBServer dbServer, final String result_id) {
        list_action_detail.clear();
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
                        list_action_detail = dbServer.getDetailTemp(result_id);
                        Log.d("GETDATA", d.toString());
                    }

                    @Override
                    public void onNext(List<CheckAction> strings) {

                        view.setActionList(list_action, list_action_detail);
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
    public void getGongxuList(SQLiteDatabase sqLiteDatabase, String table_id) {
        list_gongxu.clear();
        list_gongxu = MyUtils_IPQC.getGongXuList(sqLiteDatabase, Constans.IPQC_CHUJIAN, table_id);

        Observable.create(new ObservableOnSubscribe<List<Currency>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Currency>> e) throws Exception {

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
    public void getAction(SQLiteDatabase sqLiteDatabase, String process_id) {
        list_action.clear();
        list_action = MyUtils_IPQC.getChujianAction(sqLiteDatabase, process_id);

        Observable.create(new ObservableOnSubscribe<List<Currency>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Currency>> e) throws Exception {

                e.onNext(list_gongxu);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Currency>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("getChujianList", d.toString());
                    }

                    @Override
                    public void onNext(List<Currency> strings) {
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


    @Override
    public void commit(SQLiteDatabase localData, DBServer dbServer, String table_id, String flag) {
        //存在点检动作记录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        if (list_action_detail.size() > 0) {
            //遍历点检动作状态，如果存在异常，更新设备status
            resultstatus = "True";
            for (int i = 0; i < list_action_detail.size(); i++) {
                while (list_action_detail.get(i).getAction_result().equals("False")) {
                    //存在异常记录
                    resultstatus = "False";
                    break;
                }
            }
            CheckResult checkResult = new CheckResult(null, time, resultstatus, "", "", flag, Integer.parseInt(table_id));

            //判断是否有暂存，存在的话走更新，否则插入
//            result_id = dbServer.getRecordId(table_id);
//            if (result_id != -1)
//                result_id = dbServer.addIpqcRecord(checkResult, result_id, true);
//            else
//                result_id = dbServer.addIpqcRecord(checkResult, result_id, false);
            result_id = dbServer.addResult(table_id, checkResult);
            list_detail = new ArrayList<>();
            for (CheckAction checkAction : list_action_detail) {
                dbServer.addIpqcDetail(new CheckDetail(null, checkAction.getProcess_id(), checkAction.getProcess_name(), checkAction.getAction_id().toString(), checkAction.getDesc(), checkAction.getAction_cycle(), checkAction.getAction_result(),
                        checkAction.getAction_value(), checkAction.getNote(), checkAction.getAction_time(), result_id));
            }
            if (flag.equals(Constans.IPQC_RESTORE_COMMIT))
                dbServer.updateTableType(table_id, Constans.IPQC_RESTORE_COMMIT);
            else
                dbServer.updateTableType(table_id, Constans.IPQC_RESTORE_TEMP);

        }
    }

}
