package youdian.apk.ipqc.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import java.util.List;

import youdian.apk.ipqc.R;
import youdian.apk.ipqc.obsever.InsCheckSnObserver;
import youdian.apk.ipqc.utils.Constans;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/10/23 0023
 * Time: 下午 3:14
 * Function:
 */
public class SnChouyangAdapter extends BaseAdapter {
    List<InsCheckSnObserver> list;
    private Context context;
    private onCountChangeListener onCountChangeListener;

    public SnChouyangAdapter(List<InsCheckSnObserver> list, onCountChangeListener onCountChangeListener, Context context) {
        this.list = list;
        this.context = context;
        this.onCountChangeListener = onCountChangeListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SnChouyangAdapter.Info info = new SnChouyangAdapter.Info();
        convertView = LayoutInflater.from(context).inflate(R.layout.item_sn, parent, false);
        InsCheckSnObserver snObserver = list.get(position);
        info.rg_sn = convertView.findViewById(R.id.rg_chouyang);
        info.rb_yes = convertView.findViewById(R.id.rb_yes);
        info.rb_no = convertView.findViewById(R.id.rb_no);
        info.tv_sn = convertView.findViewById(R.id.tv_sn);
        info.edt_sn = convertView.findViewById(R.id.edt_sn_note);
        info.tv_sn.setText(snObserver.getSn());
        info.ll_note = convertView.findViewById(R.id.ll_note);
        info.ll_note.setVisibility(View.GONE);
        if (snObserver.getDetail_status().equals(Constans.Normal)) {
            info.ll_note.setVisibility(View.GONE);
            onCountChangeListener.setResult(position, Constans.Normal);
            info.rb_yes.setChecked(true);
        } else if (snObserver.getDetail_status().equals(Constans.Abnormal)) {
            info.ll_note.setVisibility(View.VISIBLE);
            if (!snObserver.getNote().equals("")) {
                onCountChangeListener.setNote(position, snObserver.getNote());
                info.edt_sn.setText(snObserver.getNote());
            }
            onCountChangeListener.setResult(position, Constans.Abnormal);
            info.rb_no.setChecked(true);
        }
        info.rg_sn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_yes) {
                    info.edt_sn.setText("");
                    onCountChangeListener.setResult(position, "");
                    info.ll_note.setVisibility(View.GONE);
                    onCountChangeListener.setResult(position, Constans.Normal);
                    info.rb_yes.setChecked(true);
                } else {
                    info.ll_note.setVisibility(View.VISIBLE);
                    onCountChangeListener.setResult(position, Constans.Abnormal);
                    info.edt_sn.setText(snObserver.getNote());
                    info.rb_no.setChecked(true);
                }
            }
        });
        info.edt_sn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                onCountChangeListener.setNote(position, editable.toString());
            }
        });
        onCountChangeListener.getChouyangCount(list.size());
        return convertView;
    }

    public class Info {
        public TextView tv_sn;
        public EditText edt_sn;
        private LinearLayout ll_note;
        public RadioGroup rg_sn;
        public RadioButton rb_yes, rb_no;
    }

    public interface onCountChangeListener {
        public void getChouyangCount(int s);

        public void setResult(int position, String result);

        public void setNote(int position, String note);
    }

    public void notify(List<InsCheckSnObserver> list) {
        list.clear();
        list.addAll(list);
        notifyDataSetChanged();
    }
}
