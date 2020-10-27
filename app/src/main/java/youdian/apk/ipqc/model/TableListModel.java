package youdian.apk.ipqc.model;


import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import youdian.apk.ipqc.bean.HomeTableData;
import youdian.apk.ipqc.bean.SEData;

import io.reactivex.rxjava3.core.Observable;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.contract.TableListContract;
import youdian.apk.ipqc.network.RetrofitClient;

public class TableListModel implements TableListContract.IMainModel {
    /**
     * 获取制程数据
     * @param org_code
     * @return
     */
    @Override
    public Observable<Response<ListResponseData<SEData>>> getSEData(String org_code) {
        return RetrofitClient.getInstance().getApi().getSeData(org_code);
    }

    /**
     * 获取制程下的初件检验表单数据
     * @param se_code
     * @return
     */
    @Override
    public Observable<Response<ListResponseData<HomeTableData>>> getFirstHomeTableData(String se_code) {
        return RetrofitClient.getInstance().getApi().getfirstHomeTableList(se_code);
    }

    /**
     * 获取制程下的巡检检验表单数据
     * @param se_code
     * @return
     */
    @Override
    public Observable<Response<ListResponseData<HomeTableData>>> getInstHomeTableData(String se_code) {
        return RetrofitClient.getInstance().getApi().getInsTableList(se_code);
    }


}
