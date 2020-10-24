package youdian.apk.ipqc.adapter;


import pw.xiaohaozi.adapter_plus.adapter.SingleTypeAdapter;
import youdian.apk.ipqc.databinding.ItemHometableRvBinding;
import youdian.apk.ipqc.databinding.ItemZhichengRvBinding;
import youdian.apk.ipqc.obsever.HomeTableObsever;
import youdian.apk.ipqc.obsever.SEObsever;
import youdian.apk.ipqc.viewHolder.SEListHolder;
import youdian.apk.ipqc.viewHolder.TableListHolder;


/**
 * 继承SingleTypeAdapter，可自定义viewholder
 */
public class TableListAdapter extends SingleTypeAdapter<ItemHometableRvBinding, HomeTableObsever,
        TableListHolder> {

    @Override
    protected void onBindViewHolder(TableListHolder tableListHolder,
                                    int position, ItemHometableRvBinding itemHometableRvBinding,
                                    HomeTableObsever homeTableObsever) {
        itemHometableRvBinding.setTableitem(homeTableObsever);
    }


}
