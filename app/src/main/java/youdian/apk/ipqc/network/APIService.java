package youdian.apk.ipqc.network;


import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import youdian.apk.ipqc.bean.FirstCheckList;
import youdian.apk.ipqc.bean.HomeTableData;
import youdian.apk.ipqc.bean.Lines;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.bean.SEData;
import youdian.apk.ipqc.bean.UserData;
import youdian.apk.ipqc.obsever.ProgressObserver;


public interface APIService {

    /**
     * 登陆
     */
    @FormUrlEncoded
    @POST("applogin/")
    Observable<Response<UserData>> login(@Field("devid") String devid, @Field("username") String username,
                                         @Field("passwd") String passwd, @Field("gid") String gid, @Field("method") String method);

    /**
     * 获取制程列表
     *
     * @param org_code
     * @return
     */
    @FormUrlEncoded
    @GET("ses/")
    Observable<Response<ListResponseData<SEData>>> getSeData(@Field("org_code") String org_code);

    /**
     * 获取初件表单
     * @param se_code
     * @return
     */
    @FormUrlEncoded
    @GET("firstchecks/")
    Observable<Response<ListResponseData<HomeTableData>>> getfirstHomeTableList(@Field("se_code") String se_code);

    /**
     * 获取巡检表单
     * @param se_code
     * @return
     */
    @FormUrlEncoded
    @GET("inschecks/")
    Observable<Response<ListResponseData<HomeTableData>>> getInsTableList(@Field("se_code") String se_code);

    /**
     * 获取线别
     * @param se_code
     * @return
     */
    @FormUrlEncoded
    @GET("lines/")
    Observable<Response<ListResponseData<Lines>>> getLines(@Field("se_code") String se_code);

    /**
     * 通用，获取下拉选择
     * @param select_type
     * @return
     */
    @FormUrlEncoded
    @GET("selectinfos/")
    Observable<Response<List<OptionData>>> getSelectInfo(@Field("select_type") String select_type);

    /**
     * 获取工序
     * @param first_checklist_id
     * @return
     */
    @FormUrlEncoded
    @GET("inschecks/")
    Observable<Response<ListResponseData<ProgressObserver>>> getProcess(@Field("first_checklist_id") String first_checklist_id);

 /**
     * 获取工序和检验项表单
     * @param first_checklist_id
     * @return
     */
    @FormUrlEncoded
    @GET("inschecks/")
    Observable<Response<FirstCheckList>> getFirstCheckDataList(@Field("first_checklist_id") String first_checklist_id);


}
