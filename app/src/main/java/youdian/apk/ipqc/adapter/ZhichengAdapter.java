package youdian.apk.ipqc.adapter;


import pw.xiaohaozi.adapter_plus.adapter.SingleTypeAdapter;
import youdian.apk.ipqc.databinding.ItemZhichengRvBinding;
import youdian.apk.ipqc.obsever.SEObsever;
import youdian.apk.ipqc.viewHolder.SEListHolder;


/**
 * 继承SingleTypeAdapter，可自定义viewholder
 */
public class ZhichengAdapter extends SingleTypeAdapter<ItemZhichengRvBinding, SEObsever,
        SEListHolder> {

    @Override
    protected void onBindViewHolder(SEListHolder seListHolder,
                                    int position, ItemZhichengRvBinding itemZhichengRvBinding,
                                    SEObsever seObsever) {
        itemZhichengRvBinding.setSedata(seObsever);
    }


}
