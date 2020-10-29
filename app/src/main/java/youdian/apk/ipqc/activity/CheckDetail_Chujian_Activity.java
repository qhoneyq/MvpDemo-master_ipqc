package youdian.apk.ipqc.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import autodispose2.AutoDisposeConverter;
import youdian.apk.dianjian.utils.DatetimeUtil;
import youdian.apk.ipqc.R;
import youdian.apk.ipqc.adapter.ActionDetailAdapter;
import youdian.apk.ipqc.adapter.ProgressAdapter;
import youdian.apk.ipqc.base.BaseMvpActivity;
import youdian.apk.ipqc.contract.CheckDetailContract_CHUJIAN;
import youdian.apk.ipqc.databinding.ActivityFirstcheckdetailBinding;
import youdian.apk.ipqc.obsever.CountModel;
import youdian.apk.ipqc.obsever.FirstCheckItemObserver;
import youdian.apk.ipqc.obsever.FirstCheckResultObserver;
import youdian.apk.ipqc.obsever.ProgressObserver;
import youdian.apk.ipqc.presenter.CheckDetailPresenter_CHUJIAN;
import youdian.apk.ipqc.utils.Constans;

import static youdian.apk.ipqc.utils.Constans.FirstCheck;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/21 0021
 * Time: 上午 11:39
 * Function:1. 判断是否需要恢复暂存记录
 */
public class CheckDetail_Chujian_Activity extends BaseMvpActivity<CheckDetailPresenter_CHUJIAN> implements CheckDetailContract_CHUJIAN.View, ActionDetailAdapter.onCountChangeListener {

    ActivityFirstcheckdetailBinding binding;
    private ProgressAdapter progressAdapter;
    private ActionDetailAdapter checkDetailAdapter;
    private FirstCheckResultObserver resultObserver;//检验记录表头
    ObservableList<FirstCheckItemObserver> allCheckItemList;//全部检验项
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
        binding.tvSn.setText(resultObserver.getSn());
        binding.heardview.setTitleText(resultObserver.getFirst_checklist_name());
        binding.heardview.setLeftIcon(R.mipmap.home_icon_return);
        binding.heardview.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnRewritetitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constans.FirstCheck, resultObserver);
                //跳转表头
                NewChujian_Activity.startActivity(CheckDetail_Chujian_Activity.this, bundle);
                finish();
            }
        });
        mPresenter.getProcess(resultObserver.getFirst_checklist_code());

    }

    @Override
    public void showLoading() {
        waitDialog.show();
    }

    @Override
    public void hideLoading() {
        waitDialog.dismiss();
    }

    @Override
    public void onError(String errMessage) {

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
        binding.rvProgress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvProgress.setItemAnimator(new DefaultItemAnimator());
        if (progressAdapter == null) {
            progressAdapter = new ProgressAdapter();
            progressAdapter.setOnItemClickListener(itemProgressRvBinding -> {
                process_id = itemProgressRvBinding.getProgressdata().getId();
                //获取单个工序检验项
                showCheckItemByProcess(process_id);
            });
            binding.rvProgress.setAdapter(progressAdapter);
            process_id = list.get(0).getId();
        }
        progressAdapter.refresh((ObservableList<ProgressObserver>) list);
    }

    /**
     * 获取全部检验项列表
     *
     * @param list
     */
    @Override
    public void setCheckListData(ObservableList<FirstCheckItemObserver> list) {
        allCheckItemList = new ObservableArrayList<>();
        allCheckItemList.addAll(list);
        binding.rvAction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvAction.setItemAnimator(new DefaultItemAnimator());
        if (checkDetailAdapter == null) {
            checkDetailAdapter = new ActionDetailAdapter(this, allCheckItemList, this);
            binding.rvAction.setAdapter(checkDetailAdapter);

        }
        showCheckItemByProcess(process_id);
        checkDetailAdapter.notifyDataSetChanged();
    }

    /**
     * 显示单个工序检验项
     * @param p_id
     */
    public void showCheckItemByProcess(int p_id) {
        for (FirstCheckItemObserver checkItemObserver:allCheckItemList){
            if (checkItemObserver.getProcess_id() == p_id){
                checkItemObserver.setIsvisiable(true);
            }else
                checkItemObserver.setIsvisiable(false);
        }
        checkDetailAdapter.notifyDataSetChanged();
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
            }
        }
    }
}

