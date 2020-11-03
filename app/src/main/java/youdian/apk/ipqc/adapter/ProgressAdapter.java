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
import androidx.databinding.ObservableList;

import java.util.List;

import pw.xiaohaozi.adapter_plus.adapter.SingleTypeAdapter;
import youdian.apk.ipqc.R;
import youdian.apk.ipqc.databinding.ItemProgressRvBinding;
import youdian.apk.ipqc.obsever.ProgressObserver;
import youdian.apk.ipqc.obsever.SEObsever;
import youdian.apk.ipqc.viewHolder.ProgresstHolder;

/**
 * 继承SingleTypeAdapter，可自定义viewholder
 */
public class ProgressAdapter extends BaseAdapter {

    List<ProgressObserver> progressList;
    Context context;

    private int defaultSelection = -1;
    private int text_selected_color;
    private ColorStateList colors;


    public ProgressAdapter(Context context,List<ProgressObserver> progressList) {
        this.progressList = progressList;
        this.context = context;
        Resources resources = context.getResources();
        text_selected_color = resources.getColor(R.color.colorPrimary);// 文字选中的颜色
        colors = context.getResources().getColorStateList(
                R.color.bg_textcolor_rb);// 文字未选中状态的selector
        resources = null;

    }

    @Override
    public int getCount() {
        return progressList.size();
    }

    @Override
    public Object getItem(int position) {
        return progressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProgressObserver progressObserver = (ProgressObserver) getItem(position);
//        View view;
        ProgressAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ProgressAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_progress_rv, parent, false);
            viewHolder.tv = convertView.findViewById(R.id.item_ipqc_process_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProgressAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(progressObserver.getProcess_name());

        if (position == defaultSelection) {// 选中时设置单纯颜色
            viewHolder.tv.setTextColor(text_selected_color);
            convertView.setBackgroundResource(R.drawable.bg_sel_zhicheng);
        } else {// 未选中时设置selector
            viewHolder.tv.setTextColor(colors);
            convertView.setBackgroundResource(R.drawable.bg_item_zhicheng);
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
        if (!(position < 0 || position > progressList.size())) {
            defaultSelection = position;
            notifyDataSetChanged();
        }
    }
}