package youdian.apk.ipqc.network;


import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import youdian.apk.ipqc.BuildConfig;
import youdian.apk.ipqc.api.client.ClientFactory;
import youdian.apk.ipqc.api.converter.CheckGsonConverterFactory;
import youdian.apk.ipqc.api.remote.APIService;
import youdian.apk.ipqc.utils.Constans;
import youdian.apk.ipqc.utils.UserUtils;


public class RetrofitClient {

    private static volatile RetrofitClient instance;
    private APIService apiService;
    private APIService apiServiceLogin;
    private String baseUrl = Constans.BASEURL;
    private String baseUrl_login = Constans.BASEURLLogin;
    private Retrofit retrofit;
    private Retrofit retrofitLogin;
    private OkHttpClient okHttpClient;

    private RetrofitClient() {
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    /**
     * 设置Header
     *
     * @return
     */
    private Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                //添加Token
//                        .header("token", "");
                Request request;
                if (UserUtils.getInstance().isLogin())
                     request = requestBuilder.addHeader(Constans.AUTHORIZATION, UserUtils.getInstance().getToken())
                            .build();
                else
                request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

    }

    /**
     * 设置拦截器 打印日志
     *
     * @return
     */
    private Interceptor getInterceptor() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //显示日志
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return interceptor;
    }

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            //如果为DEBUG 就打印日志
            if (BuildConfig.DEBUG) {
                okHttpClient = new OkHttpClient().newBuilder()
                        //设置Header
                        .addInterceptor(getHeaderInterceptor())
                        //设置拦截器
                        .addInterceptor(getInterceptor())
                        .build();
            } else {
                okHttpClient = new OkHttpClient().newBuilder()
                        //设置Header
                        .addInterceptor(getHeaderInterceptor())
                        .build();
            }

        }

        return okHttpClient;
    }

    public APIService getApi() {
        //初始化一个client,不然retrofit会自己默认添加一个
        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    //设置网络请求的Url地址
//                    .baseUrl(baseUrl)
//                    //设置数据解析器
//                    .addConverterFactory(GsonConverterFactory.create())
//                    //设置网络请求适配器，使其支持RxJava与RxAndroid
//                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//                    .client(getOkHttpClient())
//                    .build();
            retrofit = new Retrofit.Builder()
                    //设置网络请求的Url地址
                    .baseUrl(baseUrl)
                    //设置数据解析器
                    .addConverterFactory(CheckGsonConverterFactory.create())
                    //设置网络请求适配器，使其支持RxJava与RxAndroid
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//                    .client(ClientFactory.getInstance().getOkHttpClient())
                    .client(getOkHttpClient())
                    .build();
        }
        //创建—— 网络请求接口—— 实例
        if (apiService == null) {
            apiService = retrofit.create(APIService.class);
        }

        return apiService;
    }

    public APIService getLoginApi() {
        //初始化一个client,不然retrofit会自己默认添加一个
        if (retrofitLogin == null) {
            retrofitLogin = new Retrofit.Builder()
                    //设置网络请求的Url地址
                    .baseUrl(baseUrl_login)
                    //设置数据解析器
                    .addConverterFactory(GsonConverterFactory.create())
                    //设置网络请求适配器，使其支持RxJava与RxAndroid
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        //创建—— 网络请求接口—— 实例
        if (apiServiceLogin == null) {
            apiServiceLogin = retrofitLogin.create(APIService.class);
        }

        return apiServiceLogin;
    }


}
