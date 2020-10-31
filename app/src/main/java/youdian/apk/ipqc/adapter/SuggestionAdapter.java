package youdian.apk.ipqc.adapter;


import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import pw.xiaohaozi.adapter_plus.adapter.SelectAdapter;
import pw.xiaohaozi.adapter_plus.holder.SelectHolder;
import youdian.apk.ipqc.databinding.DialogSuggestionItemBinding;
import youdian.apk.ipqc.obsever.OptionObserver;


/**
 * 继承SingleTypeAdapter，可自定义viewholder
 */
public class SuggestionAdapter extends SelectAdapter<DialogSuggestionItemBinding, OptionObserver,
        SelectHolder<DialogSuggestionItemBinding>> {

    @Override
    protected <VG extends ViewGroup> SelectHolder<DialogSuggestionItemBinding>
    onCreateViewHolder(@NonNull VG vg, DialogSuggestionItemBinding vdb, int viewType) {
        SelectHolder<DialogSuggestionItemBinding> viewHolder = super.onCreateViewHolder(vg, vdb, viewType);

        //指定由哪个控件触发选中事件，默认 binding.getRoot()
        viewHolder.setTrigger(vdb.cbItem);
        return viewHolder;
    }

    @Override
    protected void onSelectChange(int position, boolean isSelect) {

    }

    @Override
    protected void onBindViewHolder(SelectHolder<DialogSuggestionItemBinding> itemBindingSelectHolder,
                                    int position, DialogSuggestionItemBinding itemBinding, OptionObserver optionObserver, boolean isSelect) {
        itemBinding.setSuggest(optionObserver);
        itemBinding.cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                optionObserver.setCheck(isChecked);
//                itemBinding.getCheckrecord().setCheck(isChecked);
            }
        });
    }


}
