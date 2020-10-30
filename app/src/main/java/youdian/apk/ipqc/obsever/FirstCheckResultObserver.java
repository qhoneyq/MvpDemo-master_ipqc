package youdian.apk.ipqc.obsever;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import youdian.apk.ipqc.BR;
import youdian.apk.ipqc.utils.Constans;

public class FirstCheckResultObserver extends BaseObservable implements Serializable {
    private String sn = "";
    private String suggestion ="";
    private String shift ="";
    private String shift_name ="";
    private String result_status ="";
    private String se_name ="";
    private String se_code ="";
    private String line_name ="";
    private String line_code ="";
    private String work_no ="";
    private String part_no ="";
    private String edition ="";
    private String production_batch ="";
    private String check_quantity ="";
    private String chujian_type ="";
    private String note ="";
    private String machine_type ="";
    private String first_checklist_name ="";
    private String first_checklist_code ="";
    private String check_person ="";
    private String check_time ="";
    private String sign_status ="";
    private List<FirstCheckItemObserver> first_result_details = new ArrayList<>();

    @Bindable
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    notifyPropertyChanged(BR.sn);
    }

    @Bindable
    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
        notifyPropertyChanged(BR.suggestion);
    }

    @Bindable
    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
        notifyPropertyChanged(BR.shift);
    }
@Bindable
    public String getShift_name() {
        return shift_name;
    }

    public void setShift_name(String shift_name) {
        this.shift_name = shift_name;
          notifyPropertyChanged(BR.shift_name);
    }

    @Bindable
    public String getResult_status() {
        return result_status;
    }

    public void setResult_status(String result_status) {
        this.result_status = result_status;
        notifyPropertyChanged(BR.result_status);
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
    public String getLine_name() {
        return line_name;
    }

    public void setLine_name(String line_name) {
        this.line_name = line_name;
        notifyPropertyChanged(BR.line_name);
    }

    @Bindable
    public String getLine_code() {
        return line_code;
    }

    public void setLine_code(String line_code) {
        this.line_code = line_code;
        notifyPropertyChanged(BR.line_code);
    }

    @Bindable
    public String getWork_no() {
        return work_no;
    }

    public void setWork_no(String work_no) {
        this.work_no = work_no;
        notifyPropertyChanged(BR.work_no);
    }

    @Bindable
    public String getPart_no() {
        return part_no;
    }

    public void setPart_no(String part_no) {
        this.part_no = part_no;
        notifyPropertyChanged(BR.part_no);
    }

    @Bindable
    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
        notifyPropertyChanged(BR.edition);
    }

    @Bindable
    public String getProduction_batch() {
        return production_batch;
    }

    public void setProduction_batch(String production_batch) {
        this.production_batch = production_batch;
        notifyPropertyChanged(BR.production_batch);
    }

    @Bindable
    public String getCheck_quantity() {
        return check_quantity;
    }

    public void setCheck_quantity(String check_quantity) {
        this.check_quantity = check_quantity;
        notifyPropertyChanged(BR.check_quantity);
    }

    @Bindable
    public String getChujian_type() {
        return chujian_type;
    }

    public void setChujian_type(String chujian_type) {
        this.chujian_type = chujian_type;
        notifyPropertyChanged(BR.chujian_type);
    }

    @Bindable
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        notifyPropertyChanged(BR.note);
    }

    @Bindable
    public String getMachine_type() {
        return machine_type;
    }

    public void setMachine_type(String machine_type) {
        this.machine_type = machine_type;
        notifyPropertyChanged(BR.machine_type);
    }

    @Bindable
    public String getFirst_checklist_name() {
        return first_checklist_name;
    }

    public void setFirst_checklist_name(String first_checklist_name) {
        this.first_checklist_name = first_checklist_name;
        notifyPropertyChanged(BR.first_checklist_name);
    }

    @Bindable
    public String getFirst_checklist_code() {
        return first_checklist_code;
    }

    public void setFirst_checklist_code(String first_checklist_code) {
        this.first_checklist_code = first_checklist_code;
        notifyPropertyChanged(BR.first_checklist_code);
    }

    @Bindable
    public String getCheck_person() {
        return check_person;
    }

    public void setCheck_person(String check_person) {
        this.check_person = check_person;
        notifyPropertyChanged(BR.check_person);
    }

    @Bindable
    public String getCheck_time() {
        return check_time;
    }

    public void setCheck_time(String check_time) {
        this.check_time = check_time;
        notifyPropertyChanged(BR.check_time);
    }

    @Bindable
    public String getSign_status() {
        return sign_status;
    }

    public void setSign_status(String sign_status) {
        this.sign_status = sign_status;
        notifyPropertyChanged(BR.sign_status);
    }

    @Bindable
    public List<FirstCheckItemObserver> getFirst_result_details() {
        return first_result_details;
    }

    public void setFirst_result_details(List<FirstCheckItemObserver> first_result_details) {
        this.first_result_details = first_result_details;
        notifyPropertyChanged(BR.first_result_details);
    }

}
