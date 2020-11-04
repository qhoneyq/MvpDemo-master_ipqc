package youdian.apk.ipqc.obsever;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;

import youdian.apk.ipqc.BR;
import youdian.apk.ipqc.bean.FirstCheckListItem;
import youdian.apk.ipqc.bean.FirstCheckProcess;
import youdian.apk.ipqc.utils.UserUtils;


public class FirstCheckItemObserver extends BaseObservable implements Serializable {

    private int process_id;
    private String process_name;
    private String process_code;
    private String process_sn;
    private String process_note;
    private int item_id;
    private String item_sn;
    private String item;
    private String reference_value;
    private String method;
    private String control;
    private String control_code="";
    private String detail_value="";
    private String detail_status="";
    private String note="";
    private String check_time = "";
    private String emp_no = "";
    private boolean isvisiable=false;

    public FirstCheckItemObserver(FirstCheckProcess firstCheckProcess, FirstCheckListItem firstCheckListItem) {
        this.process_id = firstCheckProcess.getId();
        this.process_code = firstCheckProcess.getProcess_code();
        this.process_name = firstCheckProcess.getProcess_name();
        this.process_note = firstCheckProcess.getNote();
        this.process_sn = firstCheckProcess.getProcess_sn();
        this.item_id = firstCheckListItem.getId();
        this.item = firstCheckListItem.getItem();
        this.item_sn = firstCheckListItem.getItem_sn();
        this.reference_value = firstCheckListItem.getReference_value();
        this.method = firstCheckListItem.getMethod();
        this.control = firstCheckListItem.getControl();
        this.control_code = firstCheckListItem.getControl_code();
        this.emp_no = UserUtils.getInstance().getPnum();
    }

    @Bindable
    public int getProcess_id() {
        return process_id;
    }

    public void setProcess_id(int process_id) {
        this.process_id = process_id;
        notifyPropertyChanged(BR.process_id);
    }

    @Bindable
    public String getProcess_name() {
        return process_name;
    }

    public void setProcess_name(String process_name) {
        this.process_name = process_name;
        notifyPropertyChanged(BR.process_name);
    }

    @Bindable
    public String getProcess_code() {
        return process_code;
    }

    public void setProcess_code(String process_code) {
        this.process_code = process_code;

    }

    @Bindable
    public String getProcess_sn() {
        return process_sn;
    }

    public void setProcess_sn(String process_sn) {
        this.process_sn = process_sn;
        notifyPropertyChanged(BR.process_sn);
    }

    @Bindable
    public String getProcess_note() {
        return process_note;
    }

    public void setProcess_note(String process_note) {
        this.process_note = process_note;
        notifyPropertyChanged(BR.process_note);
    }

    @Bindable
    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
        notifyPropertyChanged(BR.item_id);
    }

    @Bindable
    public String getItem_sn() {
        return item_sn;
    }

    public void setItem_sn(String item_sn) {
        this.item_sn = item_sn;
        notifyPropertyChanged(BR.item_sn);
    }

    @Bindable
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
        notifyPropertyChanged(BR.item);
    }

    @Bindable
    public String getReference_value() {
        return reference_value;
    }

    public void setReference_value(String reference_value) {
        this.reference_value = reference_value;
        notifyPropertyChanged(BR.reference_value);
    }

    @Bindable
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
        notifyPropertyChanged(BR.method);
    }

    @Bindable
    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
        notifyPropertyChanged(BR.control);
    }

    @Bindable
    public String getControl_code() {
        return control_code;
    }

    public void setControl_code(String control_code) {
        this.control_code = control_code;
        notifyPropertyChanged(BR.control_code);
    }

    @Bindable
    public String getDetail_value() {
        return detail_value;
    }

    public void setDetail_value(String detail_value) {
        this.detail_value = detail_value;
        notifyPropertyChanged(BR.detail_value);
    }

    @Bindable
    public String getDetail_status() {
        return detail_status;
    }

    public void setDetail_status(String detail_status) {
        this.detail_status = detail_status;
        notifyPropertyChanged(BR.detail_status);
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
    public String getCheck_time() {
        return check_time;
    }

    public void setCheck_time(String check_time) {
        this.check_time = check_time;
        notifyPropertyChanged(BR.check_time);
    }

    @Bindable
    public String getEmp_no() {
        return emp_no;
    }

    public void setEmp_no(String emp_no) {
        this.emp_no = emp_no;
        notifyPropertyChanged(BR.emp_no);
    }

    @Bindable
    public boolean isIsvisiable() {
        return isvisiable;
    }

    public void setIsvisiable(boolean isvisiable) {
        this.isvisiable = isvisiable;
        notifyPropertyChanged(BR.isvisiable);
    }
}
