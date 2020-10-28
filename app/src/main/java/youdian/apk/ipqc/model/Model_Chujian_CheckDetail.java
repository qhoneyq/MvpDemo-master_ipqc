package youdian.apk.ipqc.model;


import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import youdian.apk.ipqc.bean.FirstCheckList;
import youdian.apk.ipqc.bean.HomeTableData;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.bean.SEData;
import youdian.apk.ipqc.contract.CheckDetailContract_CHUJIAN;
import youdian.apk.ipqc.contract.TableListContract;
import youdian.apk.ipqc.network.RetrofitClient;
import youdian.apk.ipqc.obsever.ProgressObserver;

public class Model_Chujian_CheckDetail implements CheckDetailContract_CHUJIAN.IModel {

    /**
     * 获取工序和检验项目数据
     *
     * @param first_checklist_id
     * @return
     */
    @Override
    public Observable<Response<FirstCheckList>> getCheckListData(String first_checklist_id) {
        return RetrofitClient.getInstance().getApi().getFirstCheckDataList(first_checklist_id);
    }

    /**
     * 获取工序
     *
     * @param first_checklist_id
     * @return
     */
    @Override
    public Observable<Response<ListResponseData<ProgressObserver>>> getProcess(String first_checklist_id) {
        return RetrofitClient.getInstance().getApi().getProcess(first_checklist_id);
    }


}
