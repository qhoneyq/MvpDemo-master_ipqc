package youdian.apk.ipqc.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import autodispose2.AutoDisposeConverter;
import youdian.apk.dianjian.utils.DatetimeUtil;
import youdian.apk.ipqc.R;
import youdian.apk.ipqc.adapter.ActionDetailAdapter;
import youdian.apk.ipqc.adapter.BottomSheetAdapter;
import youdian.apk.ipqc.adapter.OptionBottomSheetAdapter;
import youdian.apk.ipqc.adapter.ProgressAdapter;
import youdian.apk.ipqc.adapter.SuggestionAdapter;
import youdian.apk.ipqc.base.BaseMvpActivity;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.contract.CheckDetailContract_CHUJIAN;
import youdian.apk.ipqc.databinding.ActivityFirstcheckdetailBinding;
import youdian.apk.ipqc.obsever.CountModel;
import youdian.apk.ipqc.obsever.FirstCheckItemObserver;
import youdian.apk.ipqc.obsever.FirstCheckResultObserver;
import youdian.apk.ipqc.obsever.InsCheckItemObserver;
import youdian.apk.ipqc.obsever.OptionObserver;
import youdian.apk.ipqc.obsever.ProgressObserver;
import youdian.apk.ipqc.presenter.CheckDetailPresenter_CHUJIAN;
import youdian.apk.ipqc.presenter.NewChujianPresenter;
import youdian.apk.ipqc.utils.CommonUtils;
import youdian.apk.ipqc.utils.Constans;
import youdian.apk.ipqc.utils.MyUtils;
import youdian.apk.ipqc.utils.MycountDownTimer;
import youdian.apk.ipqc.utils.UserUtils;
import youdian.apk.ipqc.wedige.CustomPopupWindow;

import static youdian.apk.ipqc.utils.Constans.Abnormal;
import static youdian.apk.ipqc.utils.Constans.FirstCheck;
import static youdian.apk.ipqc.utils.Constans.NEW;
import static youdian.apk.ipqc.utils.Constans.Normal;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/21 0021
 * Time: 上午 11:39
 * Function:1. 判断是否需要恢复暂存记录
 */
public class CheckDetail_Chujian_Activity extends BaseMvpActivity<CheckDetailPresenter_CHUJIAN> implements CheckDetailContract_CHUJIAN.View, ActionDetailAdapter.onCountChangeListener {

    ActivityFirstcheckdetailBinding binding;
    private CustomPopupWindow customPopupWindow;
    private BottomSheetDialog dialog;
    private ProgressAdapter progressAdapter;
    private SuggestionAdapter suggestionAdapter;
    ObservableList<ProgressObserver> processList = new ObservableArrayList<>();
    private ActionDetailAdapter checkDetailAdapter;
    private FirstCheckResultObserver resultObserver;//检验记录表头
    List<FirstCheckItemObserver> allCheckItemList;//全部检验项
    List<FirstCheckItemObserver> onCheckItemList;//显示检验项
    ObservableList<OptionObserver> suggestionList;
    private String INTENTFLAG;//修改 or 新建
    private int process_id;
    private CountModel countModel = new CountModel();


    /**
     * 静态方法跳转到当前页面
     */
    public static void startActivity(Context context, Bundle data) {
        Intent intent = new Intent(context, CheckDetail_Chujian_Activity.class);
        intent.putExtra("param", data);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_firstcheckdetail;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getBundleExtra("param");
        resultObserver = (FirstCheckResultObserver) bundle.getSerializable(FirstCheck);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        INTENTFLAG = bundle.getString(Constans.INTENTFLAG);
        binding.tvSn.setText(resultObserver.getSn());
        binding.setCount(countModel);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        binding.rvAction.setItemAnimator(new DefaultItemAnimator());
        binding.rvAction.setHasFixedSize(true);
        binding.rvAction.setHasFixedSize(true);
        binding.rvAction.setLayoutManager(layoutManager);
        mPresenter = new CheckDetailPresenter_CHUJIAN();
        mPresenter.attachView(this);
        mPresenter.getProcess(resultObserver.getFirst_checklist_id() + "");
        mPresenter.getCheckSuggestion(Constans.FirstSug);
        binding.heardview.setTitleText(resultObserver.getFirst_checklist_name());
        binding.heardview.setLeftIcon(R.mipmap.home_icon_return);
        binding.heardview.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onError(getResources().getString(R.string.returnmsg));
            }
        });
        binding.btnRewritetitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                resultObserver.setFirst_result_details(allCheckItemList);
                bundle.putSerializable(Constans.FirstCheck, resultObserver);
                bundle.putString(Constans.INTENTFLAG, Constans.REINPUT);
                //跳转表头
                NewChujian_Activity.startActivity(CheckDetail_Chujian_Activity.this, bundle);
                finish();
            }
        });
        binding.btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getNumber() == allCheckItemList.size()) {
                    showDialog();
                } else {
                    onError(getResources().getString(R.string.lackmsg));
                }
            }
        });
        customPopupWindow = new CustomPopupWindow.Builder(this)
                .setContentView(R.layout.popwindow_result)
                .setCancleClickOutSide(true)
                .setwidth(getResources().getDimensionPixelSize(R.dimen.dp_240))
                .setheight(getResources().getDimensionPixelSize(R.dimen.dp_160))
                .build();


    }

    @Override
    public void showLoading() {
        waitDialog.show();
    }

    @Override
    public void hideLoading() {
        waitDialog.dismiss();
    }

    /**
     * 显示信息
     *
     * @param msg
     */
    @Override
    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(String errMessage) {
        Toast.makeText(this, errMessage, Toast.LENGTH_LONG).show();

    }

    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return null;
    }

    /**
     * 显示工序列表
     *
     * @param list
     */
    @Override
    public void setprocess(ObservableList<ProgressObserver> list) {
        processList.clear();
        this.processList = list;
        if (progressAdapter == null) {
            progressAdapter = new ProgressAdapter(this, processList);

            binding.rvProgress.setAdapter(progressAdapter);
            progressAdapter.setSelectPosition(0);
            process_id = list.get(0).getId();
            binding.tvProgressNote.setText(processList.get(0).getNote());
        }
        progressAdapter.notifyDataSetChanged();
        if (INTENTFLAG.equals(NEW))
            mPresenter.getCheckListData(resultObserver.getFirst_checklist_id() + "");
        else {
            allCheckItemList = resultObserver.getFirst_result_details();
            setCheckListData(allCheckItemList);
        }
        binding.rvProgress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                progressAdapter.setSelectPosition(i);
                process_id = processList.get(i).getId();
                //获取单个工序检验项
                binding.tvProgressNote.setText(processList.get(i).getNote());
                showCheckItemByProcess(process_id);
            }
        });
    }


    /**
     * 响应presenter,显示底部弹窗
     *
     * @param list
     */
    @Override
    public void showBottomDialog(List<OptionData> list) {
        if (dialog == null) {
            dialog = new BottomSheetDialog(this);
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(CheckDetail_Chujian_Activity.this).inflate(R.layout.dialog_suggest, null);
        RecyclerView bottom_lv = view.findViewById(R.id.bottom_lv);
        Button btn_commit = view.findViewById(R.id.re_commit);
        EditText edt_sug = view.findViewById(R.id.edt_sug);
        bottom_lv.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false));
        suggestionList = new ObservableArrayList<>();
        for (OptionData optionData : list) {
            suggestionList.add(new OptionObserver(optionData));
        }
        if (suggestionAdapter == null) {
            suggestionAdapter = new SuggestionAdapter();
            bottom_lv.setAdapter(suggestionAdapter);
        }
        suggestionAdapter.refresh(suggestionList);
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sug = "";
                for (OptionObserver optionObserver : suggestionList) {
                    if (optionObserver.isCheck()) {
                        sug = sug + optionObserver.getOption_value() + ";";
                    }
                }
                sug = sug + edt_sug.getText().toString();
                resultObserver.setSuggestion(sug);
                resultObserver.setResult_status(getStatus());
                resultObserver.setFirst_result_details(allCheckItemList);
                mPresenter.postFirstResult(resultObserver);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);

    }

    /**
     * 记录提交成功
     */
    @Override
    public void showPopWindow(boolean isSucceed, String result) {
        TextView tvofftime = (TextView) customPopupWindow.getItemView(R.id.tv_offtime);
        AppCompatImageView img_icon = (AppCompatImageView) customPopupWindow.getItemView(R.id.img_pop);
        TextView tvResult = (TextView) customPopupWindow.getItemView(R.id.tv_pop);
        if (isSucceed) {
            img_icon.setImageResource(R.mipmap.icon_green);
            tvResult.setTextColor(Color.BLACK);
            tvResult.setText(result);
        } else {
            img_icon.setImageResource(R.mipmap.icon_red);
            tvResult.setTextColor(Color.BLACK);
            tvResult.setText(result);
        }
        MycountDownTimer downTimer = new MycountDownTimer(this, tvofftime, 6000, 1);
        downTimer.start();
        customPopupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);
        CommonUtils.setBackgroundAlpha((float) 0.3, getWindow());

    }

    public void showDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
        else
            dialog.show();
    }

    /**
     * 获取全部检验项列表
     *
     * @param list
     */
    @Override
    public void setCheckListData(List<FirstCheckItemObserver> list) {
        allCheckItemList = new ArrayList<>();
        onCheckItemList = new ArrayList<>();
        allCheckItemList.addAll(list);
        countModel.setCount_all(allCheckItemList.size() + "");
        showCheckItemByProcess(process_id);
    }

    /**
     * 显示单个工序检验项
     *
     * @param p_id
     */
    public void showCheckItemByProcess(int p_id) {
        onCheckItemList.clear();
        for (FirstCheckItemObserver checkItemObserver : allCheckItemList) {
            if (checkItemObserver.getProcess_id() == p_id) {
                onCheckItemList.add(checkItemObserver);
            }
        }
        if (checkDetailAdapter == null) {
            checkDetailAdapter = new ActionDetailAdapter(this, onCheckItemList, this);
            binding.rvAction.setAdapter(checkDetailAdapter);
        }
        countModel.setCount_ed("0");
        countModel.setCount_all(onCheckItemList.size() + "");
        binding.rvAction.removeAllViews();

        checkDetailAdapter.noyify(onCheckItemList);
        binding.rvAction.setAdapter(checkDetailAdapter);
        checkDetailAdapter.notifyDataSetChanged();
    }

    /**
     * 查询当前点检结果
     *
     * @return
     */
    private String getStatus() {
        String isNomal = Normal;
        for (FirstCheckItemObserver check : allCheckItemList) {
            if (check.getDetail_status() != null && check.getDetail_status().equals(Abnormal)) {
                isNomal = Abnormal;
                break;
            }
        }
        return isNomal;
    }

    /**
     * 查询当前点检结数量
     *
     * @return
     */
    private int getNumber() {
        int count = 0;
        for (FirstCheckItemObserver check : allCheckItemList) {
            if (!check.getDetail_status().equals("")) {
                check.setEmp_no(UserUtils.getInstance().getPnum());
                count++;
            }
        }
        return count;
    }


    @Override
    public void getCheckedCount(int s) {
        countModel.setCount_ed(s + "");
    }

    @Override
    public void getCheckDetail(FirstCheckItemObserver checkItemObserver) {
        checkItemObserver.setCheck_time(DatetimeUtil.INSTANCE.getNows_ss());
        for (int i = 0; i < allCheckItemList.size(); i++) {
            FirstCheckItemObserver check = allCheckItemList.get(i);
            if (checkItemObserver.getItem().equals(check.getItem())) {
                check.setNote(checkItemObserver.getNote());
                check.setCheck_time(DatetimeUtil.INSTANCE.getNows_ss());
                check.setDetail_value(checkItemObserver.getDetail_value());
                check.setDetail_status(checkItemObserver.getDetail_status());
                break;
            } else
                return;
        }
    }


}

