package youdian.apk.ipqc.obsever;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import youdian.apk.ipqc.BR;


/**
 * 抽样SN
 */
public class InsCheckSnObserver extends BaseObservable implements Serializable {
    private String sn = "";
    private String check_time = "";
    private String note = "";
    private String detail_value = "";
    private String detail_status = "";


   @Bindable
   public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
        notifyPropertyChanged(BR.sn);
    }

    @Bindable
    public String getCheck_time() {
        return check_time;
    }

    public void setCheck_time(String check_time) {
        this.check_time = check_time;
   notifyPropertyChanged(BR.check_time); }

   @Bindable
   public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        notifyPropertyChanged(BR.note);
    }

   @Bindable
   public String getDetail_value() {
        return detail_value;
    }

    public void setDetail_value(String detail_value) {
        this.detail_value = detail_value;
   notifyPropertyChanged(BR.detail_value); }

    @Bindable
    public String getDetail_status() {
        return detail_status;
    }

    public void setDetail_status(String detail_status) {
        this.detail_status = detail_status;
        notifyPropertyChanged(BR.detail_status); }

}
