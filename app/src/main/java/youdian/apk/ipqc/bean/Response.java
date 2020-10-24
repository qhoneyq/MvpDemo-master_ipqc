package youdian.apk.ipqc.bean;

import androidx.annotation.Nullable;


/**
 * 服务器响应实体类
 * 作者 create by H7111906 on 2020/4/7
 */
public class Response<T> extends BaseBean {
    private int code;
    private String msg;
    @Nullable
    private T data ;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
