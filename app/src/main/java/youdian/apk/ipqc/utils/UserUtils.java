package youdian.apk.ipqc.utils;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.bean.UserData;

import static youdian.apk.ipqc.utils.Constans.AUTHORIZATION;
import static youdian.apk.ipqc.utils.Constans.KEY_LOGIN_BEAN;

/**
 * 登录处理工具类
 * 作者 create by H7111906 on 2020/4/8
 */
public class UserUtils {

    private Response<UserData> mBaseResponseBean = null;

    private static class Holder{
        private static final UserUtils INSTANCE = new UserUtils();
    }

    public static UserUtils getInstance(){
        return Holder.INSTANCE;
    }

    private UserUtils(){
        getLoginBean();
    }

    public Response<UserData> getLoginBean(){
        if (mBaseResponseBean == null){
            String json = SPUtils.getInstance().get(KEY_LOGIN_BEAN, "");
            if (!TextUtils.isEmpty(json)){
                try {
                    Type type = new TypeToken<Response<UserData>>(){}.getType();
                    mBaseResponseBean = new Gson().fromJson(json, type);
                }catch (Exception ignore){
                }
            }
        }
        return mBaseResponseBean;
    }

    public void login(Response<UserData> baseResponseBean){
        mBaseResponseBean = baseResponseBean;
        String json = new Gson().toJson(baseResponseBean);
        SPUtils.getInstance().save(KEY_LOGIN_BEAN, json);
        SPUtils.getInstance().save(AUTHORIZATION,  baseResponseBean.getData().getToken());
        //添加Token公共请求头
//        if (null != baseResponseBean){
//            RxHttp.setOnParamAssembly(param -> param.addHeader("Authorization", baseResponseBean.getData().getToken()));
//            //Log.e("登录Token", baseResponseBean.getData().getToken());
//        }
    }

    public void logout(){
        mBaseResponseBean = null;
        SPUtils.getInstance().clear();
    }

    public boolean isLogin(){
        //LoginBean loginBean = getLoginBean();
        return getLoginBean() != null;
    }

    public String getUserName(){
        Response<UserData> baseResponseBean = getLoginBean();
        if (baseResponseBean == null){
            return null;
        }
        return baseResponseBean.getData().getUsername();
    }

    public String getToken(){
        Response<UserData> baseResponseBean = getLoginBean();
        if (baseResponseBean == null){
            return null;
        }
        return baseResponseBean.getData().getToken();
    }

    public String getRole(){
        Response<UserData> baseResponseBean = getLoginBean();
        if (baseResponseBean == null){
            return null;
        }
        return baseResponseBean.getData().getRole_code();
    }
    public String getBUCODE(){
        Response<UserData> baseResponseBean = getLoginBean();
        if (baseResponseBean == null){
            return null;
        }
        return baseResponseBean.getData().getBu_code();
    }
    public String getPnum(){
        Response<UserData> baseResponseBean = getLoginBean();
        if (baseResponseBean == null){
            return null;
        }
        return baseResponseBean.getData().getPnum();
    }
    public String getBUName(){
        Response<UserData> baseResponseBean = getLoginBean();
        if (baseResponseBean == null){
            return null;
        }
        return baseResponseBean.getData().getBu_name();
    }
}
