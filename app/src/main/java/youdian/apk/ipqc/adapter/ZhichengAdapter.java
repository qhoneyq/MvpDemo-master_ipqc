package youdian.apk.ipqc.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.databinding.ObservableArrayList;

import java.util.List;

import youdian.apk.ipqc.R;
import youdian.apk.ipqc.obsever.SEObsever;


/**
 * 继承SingleTypeAdapter，可自定义viewholder
 */
public class ZhichengAdapter extends BaseAdapter {

    ObservableArrayList<SEObsever> seList;
    Context context;

    public ZhichengAdapter(Context context, ObservableArrayList<SEObsever> seList) {
        this.seList = seList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return seList.size();
    }

    @Override
    public Object getItem(int position) {
        return seList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SEObsever seObsever = (SEObsever) getItem(position);
        View view;
        ZhichengAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_zhicheng_rv, parent, false);
            viewHolder = new ZhichengAdapter.ViewHolder();
            viewHolder.tv = view.findViewById(R.id.item_ipqc_zhicheng_tv);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ZhichengAdapter.ViewHolder) view.getTag();
        }
        viewHolder.tv.setText(seObsever.getSe_name());
        return view;
    }

    class ViewHolder {
        TextView tv;
    }
}
