package youdian.apk.ipqc.adapter;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.SuggestionsInfo;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import youdian.apk.ipqc.R;
import youdian.apk.ipqc.obsever.OptionObserver;
import youdian.apk.ipqc.obsever.SEObsever;


/**
 * 继承SingleTypeAdapter，可自定义viewholder
 */
public class FirstSugAdapter extends BaseAdapter {

    ObservableList<OptionObserver> seList;
    Context context;

    private int defaultSelection=100;
    private int text_selected_color;
    private ColorStateList colors;


    public FirstSugAdapter(Context context, ObservableList<OptionObserver> seList) {
        this.seList = seList;
        this.context = context;
        Resources resources = context.getResources();
        text_selected_color = resources.getColor(R.color.colorPrimary);// 文字选中的颜色
        colors = context.getResources().getColorStateList(
                R.color.bg_textcolor_table);// 文字未选中状态的selector
        resources = null;

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
        OptionObserver optionObserver = (OptionObserver) getItem(position);
//        View view;
        FirstSugAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new FirstSugAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.dialog_suggestion_item, parent, false);
            viewHolder.tv = convertView.findViewById(R.id.tv_item_suggest);
            viewHolder.cb = convertView.findViewById(R.id.cb_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FirstSugAdapter.ViewHolder) convertView.getTag();
        }

        if (position == defaultSelection) {// 选中时设置单纯颜色
            viewHolder.tv.setTextColor(text_selected_color);
            viewHolder.cb.setSelected(true);
        } else {// 未选中时设置selector
            viewHolder.tv.setTextColor(colors);
            viewHolder.cb.setSelected(false);
        }

        viewHolder.tv.setText(optionObserver.getOption_name());

        return convertView;
}

class ViewHolder {
    TextView tv;
    ImageView cb;
}


    /**
     * @param position 设置高亮状态的item
     */
    public void setSelectPosition(int position) {
        if (!(position < 0 || position > seList.size())) {
            defaultSelection = position;
            notifyDataSetChanged();
        }
    }
    /**

     */
    public int getSelectPosition() {
        return defaultSelection;
    }
}
