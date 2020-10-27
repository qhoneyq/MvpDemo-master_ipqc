package youdian.apk.ipqc.model;

import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import youdian.apk.ipqc.bean.Lines;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.contract.NewChujianContract;
import youdian.apk.ipqc.network.RetrofitClient;

public class NewChujianModel implements NewChujianContract.IMainModel {

    /**
     * 获取线别
     * @param secode
     * @return
     */
    @Override
    public Observable<Response<ListResponseData<Lines>>> getLines(String secode) {
        return RetrofitClient.getInstance().getApi().getLines(secode);
    }

    @Override
    public Observable<Response<List<OptionData>>> getSelectInfo(String selectinfos) {
        return RetrofitClient.getInstance().getApi().getSelectInfo(selectinfos);
    }
}
