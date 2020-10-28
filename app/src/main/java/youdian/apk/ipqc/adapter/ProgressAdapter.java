package youdian.apk.ipqc.adapter;


import pw.xiaohaozi.adapter_plus.adapter.SingleTypeAdapter;
import youdian.apk.ipqc.databinding.ItemProgressRvBinding;
import youdian.apk.ipqc.obsever.ProgressObserver;
import youdian.apk.ipqc.viewHolder.ProgresstHolder;

/**
 * 继承SingleTypeAdapter，可自定义viewholder
 */
public class ProgressAdapter extends SingleTypeAdapter<ItemProgressRvBinding, ProgressObserver,
        ProgresstHolder> {

    @Override
    protected void onBindViewHolder(ProgresstHolder ProgresstHolder,
                                    int position, ItemProgressRvBinding itemProgressRvBinding,
                                    ProgressObserver progressObserver) {
        itemProgressRvBinding.setSedata(progressObserver);
    }


}
