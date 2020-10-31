package youdian.apk.ipqc.obsever;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;

import youdian.apk.ipqc.BR;
import youdian.apk.ipqc.bean.OptionData;

public class OptionObserver extends BaseObservable implements Serializable {
    private String option_name;
    private String option_value;
    private boolean isCheck = false;//是否选中


    public OptionObserver(OptionData data) {
        this.option_name = data.getOption_name();
        this.option_value = data.getOption_value();
    }

    @Bindable
    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
        notifyPropertyChanged(BR.option_name);
    }

    @Bindable
    public String getOption_value() {
        return option_value;
    }

    public void setOption_value(String option_value) {
        this.option_value = option_value;
        notifyPropertyChanged(BR.option_value);
    }

    @Bindable
    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    notifyPropertyChanged(BR.check);}
}
