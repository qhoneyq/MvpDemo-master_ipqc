package youdian.apk.ipqc.obsever;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import youdian.apk.ipqc.BR;


public class CountModel extends BaseObservable {
    private String count_ed  = "0" ;
    private String count_all = "0";

    @Bindable
    public String getCount_ed() {
        return count_ed;
    }

    public void setCount_ed(String count_ed) {
        this.count_ed = count_ed;
   notifyPropertyChanged(BR.count_ed);
    }

    @Bindable
    public String getCount_all() {
        return count_all;
    }

    public void setCount_all(String count_all) {
        this.count_all = count_all;
        notifyPropertyChanged(BR.count_all);
    }
}
