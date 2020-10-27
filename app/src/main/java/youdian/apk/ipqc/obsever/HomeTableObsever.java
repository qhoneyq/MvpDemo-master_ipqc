package youdian.apk.ipqc.obsever;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import youdian.apk.ipqc.BR;

import java.io.Serializable;

import youdian.apk.ipqc.bean.HomeTableData;

/**
 *
 */
public class HomeTableObsever extends BaseObservable implements Serializable {
    private int id;
    private String se_name;
    private String se;
    private String list_name;
    private String list_code;


    public HomeTableObsever(HomeTableData tableData) {
        this.id = tableData.getId();
        this.se_name = tableData.getSe_name();
        this.se = tableData.getSe();
        this.list_code = tableData.getList_code();
        this.list_name = tableData.getList_name();

    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable

    public String getSe_name() {
        return se_name;
    }

    public void setSe_name(String se_name) {
        this.se_name = se_name;
    }

    @Bindable
    public String getList_name() {
        return list_name;
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    notifyPropertyChanged(BR.list_name);}

    @Bindable
    public String getList_code() {
        return list_code;
    }

    public void setList_code(String list_code) {
        this.list_code = list_code;
        notifyPropertyChanged(BR.list_code);}

   @Bindable
   public String getSe() {
        return se;
    }

    public void setSe(String se) {
        this.se = se;
        notifyPropertyChanged(BR.se);
    }
}
