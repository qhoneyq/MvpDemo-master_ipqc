package youdian.apk.ipqc.api.remote;


import androidx.databinding.ObservableList;

import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import youdian.apk.ipqc.bean.FirstCheckList;
import youdian.apk.ipqc.bean.HomeTableData;
import youdian.apk.ipqc.bean.InsCheckList;
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
    Observable<Response<UserData>> login(@Field("dev_code") String devid, @Field("username") String username,
                                         @Field("password") String passwd, @Field("gid") String gid, @Field("method") String method);

    /**
     * 获取制程列表
     *
     * @param org_code
     * @return
     */
    @GET("ses/")
    Observable<Response<ListResponseData<SEData>>> getSeData(@Query("org_code") String org_code);

    /**
     * 获取初件表单
     *
     * @param se_code
     * @return
     */
    @GET("firstchecks/")
    Observable<Response<ListResponseData<HomeTableData>>> getfirstHomeTableList(@Query("se_code") String se_code);

    /**
     * 获取巡检表单
     *
     * @param se_code
     * @return
     */
    @GET("inschecks/")
    Observable<Response<ListResponseData<HomeTableData>>> getInsTableList(@Query("se_code") String se_code);

    /**
     * 获取线别
     *
     * @param se_code
     * @return
     */
    @GET("lines/")
    Observable<Response<ListResponseData<Lines>>> getLines(@Query("se_code") String se_code);

    /**
     * 通用，获取下拉选择
     *
     * @param select_type
     * @return
     */
    @GET("selectinfos/")
    Observable<Response<List<OptionData>>> getSelectInfo(@Query("select_type") String select_type);

    /*****************************************  初件 *******************************************/


    /**
     * 获取工序
     *
     * @param first_checklist_id
     * @return
     */
    @GET("firstcheckprocesses/")
    Observable<Response<List<ProgressObserver>>> getProcess(@Query("first_checklist_id") String first_checklist_id);

    /**
     * 获取工序和检验项表单
     *
     * @param first_checklist_id
     * @return
     */
    @GET("firstchecks/{first_checklist_id}/")
    Observable<Response<FirstCheckList>> getFirstCheckDataList(@Path("first_checklist_id") String first_checklist_id);

    /**
     * 初件提交记录
     */
    @POST("firstcheckresults/")
    Observable<Response<String>> postFirstResult(@Body RequestBody body);


    /*****************************************  巡检 *******************************************/


    /**
     * 获取工序
     *
     * @param ins_checklist_id
     * @return
     */
    @GET("inscheckprocesses/")
    Observable<Response<List<ProgressObserver>>> getInsProcess(@Query("inspection_checklist_id") String ins_checklist_id);

    /**
     * 获取工序和检验项表单
     *
     * @param inspection_checklist_id
     * @return
     */
    @GET("inschecks/{inspection_checklist_id}/")
    Observable<Response<InsCheckList>> getInsCheckDataList(@Path("inspection_checklist_id") String inspection_checklist_id);

    /**
     * 初件提交记录
     */
    @POST("inscheckresults/")
    Observable<Response<String>> postInsResult(@Body RequestBody body);


}
