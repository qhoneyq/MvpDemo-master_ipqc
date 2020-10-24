package youdian.apk.ipqc.viewHolder;

import androidx.annotation.NonNull;

import pw.xiaohaozi.adapter_plus.holder.ViewHolder;
import youdian.apk.ipqc.databinding.ItemHometableRvBinding;
import youdian.apk.ipqc.databinding.ItemZhichengRvBinding;

public class TableListHolder extends ViewHolder<ItemHometableRvBinding> {
        public TableListHolder(@NonNull ItemHometableRvBinding binding) {
            super(binding);
            //添加点击事件
            binding.getRoot().setOnClickListener(this);
        }
    }
