package youdian.apk.ipqc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import youdian.apk.ipqc.R;
import youdian.apk.ipqc.bean.Lines;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/21 0021
 * Time: 上午 9:45
 */
public class BottomSheetAdapter extends BaseAdapter {

    private List<Lines> list;
    private Context context;

    public BottomSheetAdapter(List<Lines> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class Info {
        public TextView tv_name;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        BottomSheetAdapter.Info info = new BottomSheetAdapter.Info();
        convertView = LayoutInflater.from(context).inflate(R.layout.bottom_lv_item, parent, false);
        info.tv_name = convertView.findViewById(R.id.bootom_lv_item_tv);
        String name = list.get(position).getLine_name();
        info.tv_name.setText(name);
        return convertView;
    }
}