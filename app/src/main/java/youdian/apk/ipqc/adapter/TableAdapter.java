package youdian.apk.ipqc.adapter;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.databinding.ObservableArrayList;

import youdian.apk.ipqc.R;
import youdian.apk.ipqc.obsever.HomeTableObsever;


/**
 * 继承SingleTypeAdapter，可自定义viewholder
 */
public class TableAdapter extends BaseAdapter {

    ObservableArrayList<HomeTableObsever> seList;
    Context context;

    private int defaultSelection = -1;
    private int text_selected_color;
    private Drawable bg_selected_color;
    private ColorStateList colors;


    public TableAdapter(Context context, ObservableArrayList<HomeTableObsever> seList) {
        this.seList = seList;
        this.context = context;
        Resources resources = context.getResources();
        text_selected_color = resources.getColor(R.color.colorPrimary);// 文字选中的颜色
        colors = context.getResources().getColorStateList(
                R.color.bg_textcolor_table);// 文字未选中状态的selector


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
        HomeTableObsever seObsever = (HomeTableObsever) getItem(position);
//        View view;
        TableAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new TableAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hometable_rv, parent, false);
            viewHolder.tv = convertView.findViewById(R.id.item_ipqc_biaodanlist_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TableAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(seObsever.getList_name());

        if (position == defaultSelection) {// 选中时设置单纯颜色
            viewHolder.tv.setTextColor(text_selected_color);
        } else {// 未选中时设置selector
            viewHolder.tv.setTextColor(colors);
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv;
    }


    /**
     * @param position
     *            设置高亮状态的item
     */
    public void setSelectPosition(int position) {
        if (!(position < 0 || position > seList.size())) {
            defaultSelection = position;
            notifyDataSetChanged();
        }
    }
    }
