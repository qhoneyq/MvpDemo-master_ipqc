package youdian.apk.ipqc.model;


import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import youdian.apk.ipqc.bean.HomeTableData;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.bean.SEData;
import youdian.apk.ipqc.contract.CheckDetailContract_CHUJIAN;
import youdian.apk.ipqc.contract.TableListContract;
import youdian.apk.ipqc.network.RetrofitClient;
import youdian.apk.ipqc.obsever.ProgressObserver;

public class Model_Chujian_CheckDetail implements CheckDetailContract_CHUJIAN.IModel {
    /**
     * 获取工序数据
     * @param first_checklist_id
     * @return
     */
    @Override
    public Observable<Response<List<ProgressObserver>>> getProgress(String first_checklist_id) {
        return RetrofitClient.getInstance().getApi().getFirstProgressList(first_checklist_id);
    }



}
