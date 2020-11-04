package youdian.apk.ipqc.model;


import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import youdian.apk.ipqc.bean.FirstCheckList;
import youdian.apk.ipqc.bean.InsCheckList;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.contract.CheckDetailContract_CHUJIAN;
import youdian.apk.ipqc.contract.CheckDetailContract_XUNJIAN;
import youdian.apk.ipqc.network.RetrofitClient;
import youdian.apk.ipqc.obsever.InsCheckResultObserver;
import youdian.apk.ipqc.obsever.ProgressObserver;

public class Model_Xunjian_CheckDetail implements CheckDetailContract_XUNJIAN.IModel {

    /**
     * 获取工序和检验项目数据
     *
     * @param ins_checklist_id
     * @return
     */
    @Override
    public Observable<Response<InsCheckList>> getCheckListData(String ins_checklist_id) {
        return RetrofitClient.getInstance().getApi().getInsCheckDataList(ins_checklist_id);
    }

    /**
     * 获取工序
     *
     * @param ins_checklist_id
     * @return
     */
    @Override
    public Observable<Response<List<ProgressObserver>>> getProcess(String ins_checklist_id) {
        return RetrofitClient.getInstance().getApi().getInsProcess(ins_checklist_id);
    }

    /**
     * 通用，获取建议
     * @param selectinfos
     * @return
     */
    @Override
    public Observable<Response<List<OptionData>>> getSelectInfo(String selectinfos) {
        return RetrofitClient.getInstance().getApi().getSelectInfo(selectinfos);
    }

    /**
     * 巡检记录提交
     * @param body
     * @return
     */
    @Override
    public Observable<Response<InsCheckResultObserver>> postInsResult(RequestBody body) {
        return RetrofitClient.getInstance().getApi().postInsResult(body);
    }

}
