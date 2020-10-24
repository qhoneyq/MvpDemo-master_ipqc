package youdian.apk.ipqc.network;


import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import youdian.apk.ipqc.bean.HomeTableData;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.bean.SEData;
import youdian.apk.ipqc.bean.UserData;


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

    @FormUrlEncoded
    @GET("firstchecks/")
    Observable<Response<ListResponseData<HomeTableData>>> getHomeTableList(@Field("se_code") String se_code);


}
