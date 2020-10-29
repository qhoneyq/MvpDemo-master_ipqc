package youdian.apk.ipqc.obsever;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


import java.io.Serializable;

import youdian.apk.ipqc.BR;
import youdian.apk.ipqc.bean.SEData;

/**
 *
 */
public class SEObsever extends BaseObservable implements Serializable {
    private int id;
    private String se_name;
    private String se_code;
    private String organization;
    private String org_name;
    private String se_category;
    private String se_category_name;

    public SEObsever(SEData seData) {
        this.id = seData.getId();
        this.se_name = seData.getSe_name();
        this.se_code = seData.getSe_code();
        this.organization = seData.getOrganization();
        this.org_name = seData.getOrg_name();
        this.se_category = seData.getSe_category();
        this.se_category_name = seData.getSe_category_name();

    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable

    public String getSe_name() {
        return se_name;
    }

    public void setSe_name(String se_name) {
        this.se_name = se_name;
        notifyPropertyChanged(BR.se_name);
    }

    @Bindable
    public String getSe_code() {
        return se_code;
    }

    public void setSe_code(String se_code) {
        this.se_code = se_code;
        notifyPropertyChanged(BR.se_code);
    }

    @Bindable
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
        notifyPropertyChanged(BR.organization);
    }

    @Bindable
    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
        notifyPropertyChanged(BR.org_name);
    }

    @Bindable
    public String getSe_category() {
        return se_category;
    }

    public void setSe_category(String se_category) {
        this.se_category = se_category;
        notifyPropertyChanged(BR.se_category);
    }

    @Bindable
    public String getSe_category_name() {
        return se_category_name;
    }

    public void setSe_category_name(String se_category_name) {
        this.se_category_name = se_category_name;
        notifyPropertyChanged(BR.se_category_name);
    }
}
