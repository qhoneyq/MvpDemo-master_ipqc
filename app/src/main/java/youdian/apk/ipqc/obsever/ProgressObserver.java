package youdian.apk.ipqc.obsever;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;

import youdian.apk.ipqc.BR;

public class ProgressObserver extends BaseObservable implements Serializable {

    int id;
    String process_name;
    String process_code;
    String progress_sn;
    String note;

    @Bindable
    public int getId() {
        return id;
    }

    @Bindable
    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);}


    @Bindable
    public String getProcess_name() {
        return process_name;
    }

    public void setProcess_name(String process_name) {
        this.process_name = process_name;
        notifyPropertyChanged(BR.process_name);}


    @Bindable
    public String getProcess_code() {
        return process_code;
    }

    public void setProcess_code(String process_code) {
        this.process_code = process_code;
        notifyPropertyChanged(BR.process_code);}


    @Bindable
    public String getProgress_sn() {
        return progress_sn;
    }

    public void setProgress_sn(String progress_sn) {
        this.progress_sn = progress_sn;
        notifyPropertyChanged(BR.progress_sn);}

    @Bindable
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    notifyPropertyChanged(BR.note);}
}
