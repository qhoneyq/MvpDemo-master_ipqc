package youdian.apk.ipqc.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import youdian.apk.dianjian.utils.DatetimeUtil;
import youdian.apk.ipqc.R;
import youdian.apk.ipqc.databinding.ItemCheckactionBinding;
import youdian.apk.ipqc.obsever.FirstCheckItemObserver;
import youdian.apk.ipqc.utils.Constans;


public class ActionDetailAdapter extends RecyclerView.Adapter<ActionDetailAdapter.MyHolder> {

    ObservableArrayList<FirstCheckItemObserver> list_action;
    Context context;
    private PopupWindow mPopWindow;          //动作规范弹窗
    private PopupWindow popuList;            //点检动作下拉控件
    private int Count = 0;                    //已点检项目数量
    private onCountChangeListener onCountChangeListener;    //点检状态监听
    private boolean[] ischeck;

    //    onClick onclick;
    public ActionDetailAdapter(Context context, ObservableArrayList<FirstCheckItemObserver> list_action, ActionDetailAdapter.onCountChangeListener listener) {
        this.list_action = list_action;
        this.context = context;
        this.onCountChangeListener = listener;
        ischeck = new boolean[list_action.size()];
        for (int i = 0; i < list_action.size(); i++) {
            ischeck[i] = false;
        }
    }

    public interface onCountChangeListener {
        public void getCheckedCount(int s);

        public void getCheckDetail(FirstCheckItemObserver checkDetailObsever);
//public void getCheckDetail(String item, String method,String reference_value, String control, String control_code,  String check_time,
//                                   Object detail_value, String detail_status, String note);

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCheckactionBinding selectBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_checkaction, parent,
                false
        );
        return new MyHolder(selectBinding);
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ItemCheckactionBinding binding;

        /**
         * @param binding 可以看作是这个hodler代表的布局的马甲，getRoot()方法会返回整个holder的最顶层的view
         */
        public MyHolder(ItemCheckactionBinding binding) {
            //
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(FirstCheckItemObserver item) {
            binding.setAction(item);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder viewHolder, int p) {
        if (viewHolder instanceof MyHolder) {

            FirstCheckItemObserver checkDetail = list_action.get(p);
            checkDetail.setNote("");
            viewHolder.binding.setResult(checkDetail);

            //检查标准tip
            viewHolder.binding.imgTip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        showPopupWindow(viewHolder.binding.imgTip, checkDetail.getMethod());
                }
            });

            //根据check_control_id确定所显示控件 --------- 1:radiogroup;2:number(100-200);3:下拉列表;其他：文本框
            //除了数字输入，其他都有radio选择
            if (checkDetail.getControl_code().equals("Radio")) {
                viewHolder.binding.ctEdt.setVisibility(View.GONE);
                viewHolder.binding.ctDropdown.setVisibility(View.GONE);
                viewHolder.binding.ctSelect.setVisibility(View.VISIBLE);
            } else if (checkDetail.getControl_code().equals("Number")) {
                viewHolder.binding.ctEdt.setVisibility(View.VISIBLE);
                viewHolder.binding.ctDropdown.setVisibility(View.GONE);
                viewHolder.binding.ctSelect.setVisibility(View.GONE);
                viewHolder.binding.ctEdt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            } else if (checkDetail.getControl_code().equals("Check")) {//下拉
                viewHolder.binding.ctEdt.setVisibility(View.GONE);
                viewHolder.binding.ctDropdown.setVisibility(View.VISIBLE);
                viewHolder.binding.ctSelect.setVisibility(View.VISIBLE);
                String droptext[] = checkDetail.getReference_value().split(",");
                viewHolder.binding.ctDropdown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 点击控件后显示popup窗口
                        initSelectPopup(viewHolder.binding.ctDropdown, droptext);
                        // 使用isShowing()检查popup窗口是否在显示状态
                        if (popuList != null && !popuList.isShowing()) {
                            popuList.showAsDropDown(viewHolder.binding.ctDropdown, 0, 10);
                        }
                    }
                });

//        } else if (actionDetail.getControl_code().equals("Text")) {
            } else {//文本
                viewHolder.binding.ctEdt.setVisibility(View.VISIBLE);
                viewHolder.binding.ctDropdown.setVisibility(View.GONE);
                viewHolder.binding.ctSelect.setVisibility(View.VISIBLE);
            }


            /******************************************** 各种监听 ****************************************************/

            //radiogroup控件选择监听
            viewHolder.binding.ctRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (!ischeck[viewHolder.getBindingAdapterPosition()]) {
                        ischeck[viewHolder.getBindingAdapterPosition()] = true;
                        Count = getCount();
                        onCountChangeListener.getCheckedCount(Count);
                    }
                    checkDetail.setCheck_time(DatetimeUtil.INSTANCE.getNows_ss());
                    if (checkedId == R.id.ct_rb_no) {
                        viewHolder.binding.ctRgEdtNote.setVisibility(View.VISIBLE);
                        checkDetail.setDetail_status(Constans.Abnormal);
                        if (checkDetail.getControl_code().equals("Radio")) {
                            checkDetail.setDetail_value("False");
                        }
                    } else {
                        viewHolder.binding.ctRgEdtNote.setVisibility(View.GONE);
                        viewHolder.binding.ctRgEdtNote.setText("");
                        if (checkedId == R.id.ct_rb_yes) {
                            checkDetail.setDetail_status(Constans.Normal);
                            if (checkDetail.getControl_code().equals("Radio")) {
                                checkDetail.setDetail_value("True");
                            }
                        } else {
                            checkDetail.setDetail_status(Constans.NA);
                            if (checkDetail.getControl_code().equals("Radio")) {
                                checkDetail.setDetail_value("NA");
                            }
                        }
                    }

                    onCountChangeListener.getCheckDetail(checkDetail);
                }
            });
            viewHolder.binding.ctEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (s.toString().length() != 0) {//文本框有输入
                        if (!ischeck[viewHolder.getBindingAdapterPosition()]) {
                            ischeck[viewHolder.getBindingAdapterPosition()] = true;
                            Count = getCount();
                            onCountChangeListener.getCheckedCount(Count);
                        }
                    } else {//无输入
//                        ischeck.put(position, false);
                        ischeck[viewHolder.getBindingAdapterPosition()] = false;
                        Count = getCount();
                        onCountChangeListener.getCheckedCount(Count);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {

                        if (checkDetail.getControl_code().equals("Number")) {//数字监听
                            double value1 = 0;
                            double value2 = 0;
                            try {
                                String reference_value = list_action.get(viewHolder.getAdapterPosition()).getReference_value();
                                String value[] = reference_value.split(",");
                                value1 = Double.valueOf(value[0]);
                                value2 = Double.valueOf(value[1]);
                                double d = 0;
                                try {
                                    d = Double.valueOf(s.toString());
                                    if (d >= value1 && d <= value2) {
                                        checkDetail.setDetail_status(Constans.Normal);
                                    } else
                                        checkDetail.setDetail_status(Constans.Abnormal);
                                    checkDetail.setDetail_value(s.toString());
                                    onCountChangeListener.getCheckDetail(checkDetail);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    CommonUtils.showMsg(context, context.getString(R.string.referencecomparevalue_err));

                                }

                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                CommonUtils.showMsg(context, context.getString(R.string.referencevalue_err));
                                return;
                            }
                        } else {
                            //文本监听
                            onCountChangeListener.getCheckDetail(checkDetail);

                        }
                    }
                }
            });
            viewHolder.binding.ctRgEdtNote.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkDetail.setNote(s.toString());
                    onCountChangeListener.getCheckDetail(checkDetail);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            //下拉控件监听
            viewHolder.binding.ctDropdown.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!ischeck[viewHolder.getBindingAdapterPosition()]) {
                        ischeck[viewHolder.getBindingAdapterPosition()] = true;
                        Count = getCount();
                        onCountChangeListener.getCheckedCount(Count);
                    }
                    onCountChangeListener.getCheckDetail(checkDetail);
                }
            });

        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * 查询当前点检项目数量
     *
     * @return
     */
    public int getCount() {
        int count = 0;
        for (Boolean b : ischeck) {
            if (b == true) {
                count += 1;
            }
        }
        return count;
    }


    /**
     * 动作规范弹窗
     *
     * @param ct
     * @param note
     */
    private void showPopupWindow(final CheckBox ct, String note) {
        //设置contentView
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_tip, null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPopWindow.setElevation(30);
        }
        mPopWindow.setWidth(700);
        //设置各个控件的点击响应
        TextView tv = (TextView) contentView.findViewById(R.id.ct_tv_tip);
        tv.setText(note);
        //当点击popwindow以外的地方关闭窗口
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setTouchable(true);
        //显示的位置
        mPopWindow.showAsDropDown(ct, -630, 19);

        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ct.setChecked(false);
            }
        });
    }

    /**
     * 点检动作下拉选择弹窗
     */
    private void initSelectPopup(final TextView tv, final String[] reference_value) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.populist_style, null);
        popuList = new PopupWindow(contentView, tv.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popuList.setContentView(contentView);
//        popuList = new PopupWindow(mTypeLv, tv.getWidth(), ActionBar.LayoutParams.WRAP_CONTENT, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popuList.setElevation(30);
        }
        popuList.setBackgroundDrawable(new BitmapDrawable());
        popuList.setFocusable(true);
        popuList.setOutsideTouchable(true);
        ListView mTypeLv = contentView.findViewById(R.id.populist_lv);
        // 设置适配器
        ArrayAdapter adapter = new ArrayAdapter<String>(context, R.layout.spinner_item, reference_value);
        mTypeLv.setAdapter(adapter);

        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 在这里获取item数据
                String value = reference_value[position];
                // 把选择的数据展示对应的TextView上
                tv.setText(value);
                // 选择完后关闭popup窗口
                popuList.dismiss();
            }
        });

        popuList.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 关闭popup窗口
                popuList.dismiss();
            }
        });
    }


    @Override
    public int getItemCount() {
        return list_action.size();
    }

}