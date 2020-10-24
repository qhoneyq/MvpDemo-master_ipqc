package youdian.apk.ipqc.viewHolder;

import androidx.annotation.NonNull;

import pw.xiaohaozi.adapter_plus.holder.ViewHolder;
import youdian.apk.ipqc.databinding.ItemZhichengRvBinding;

public class SEListHolder extends ViewHolder<ItemZhichengRvBinding> {
        public SEListHolder(@NonNull ItemZhichengRvBinding binding) {
            super(binding);
            //添加点击事件
            binding.getRoot().setOnClickListener(this);
        }
    }
