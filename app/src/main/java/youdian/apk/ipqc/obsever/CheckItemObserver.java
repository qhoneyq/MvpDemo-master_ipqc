package youdian.apk.ipqc.obsever;

import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class CheckItemObserver extends BaseObservable implements Serializable {

    int id;
    String item;
    String item_sn;
    String method;
    String reference_value;
    String control;
    String control_code;


}
