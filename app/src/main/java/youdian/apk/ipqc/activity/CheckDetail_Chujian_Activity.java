package youdian.apk.ipqc.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import autodispose2.AutoDisposeConverter;
import youdian.apk.ipqc.R;
import youdian.apk.ipqc.adapter.ProgressAdapter;
import youdian.apk.ipqc.adapter.ZhichengAdapter;
import youdian.apk.ipqc.base.BaseMvpActivity;
import youdian.apk.ipqc.contract.CheckDetailContract_CHUJIAN;
import youdian.apk.ipqc.databinding.ActivityFirstcheckdetailBinding;
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
public class CheckDetail_Chujian_Activity extends BaseMvpActivity<CheckDetailPresenter_CHUJIAN> implements CheckDetailContract_CHUJIAN.View {

    ActivityFirstcheckdetailBinding binding;
    private ProgressAdapter progressAdapter;
    private FirstCheckResultObserver resultObserver;//检验记录表头


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
        binding.headview.setLeftIcon(R.mipmap.home_icon_return);
        binding.headview.setLeftClick(new View.OnClickListener() {
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
        mPresenter.getProgress(resultObserver.getFirst_checklist_code());

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
     * @param list
     */
    @Override
    public void setprocess(ObservableList<ProgressObserver> list) {
        binding.rvProgress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvProgress.setItemAnimator(new DefaultItemAnimator());
        if (progressAdapter == null) {
            progressAdapter = new ProgressAdapter();
            progressAdapter.setOnItemClickListener(itemProgressRvBinding -> {
                    mPresenter.getInsTableList(itemProgressRvBinding.getProgressdata().getProcess_code());
            });
            binding.rvProgress.setAdapter(progressAdapter);
        }
        progressAdapter.refresh((ObservableList<ProgressObserver>) list);
    }
/**
     * 显示检验项列表
     * @param list
     */
    @Override
    public void setCheckListData(ObservableList<FirstCheckItemObserver> list) {
        binding.rvProgress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvProgress.setItemAnimator(new DefaultItemAnimator());
        if (progressAdapter == null) {
            progressAdapter = new ProgressAdapter();
            progressAdapter.setOnItemClickListener(itemZhichengRvBinding -> {
                    mPresenter.getInsTableList(itemZhichengRvBinding.getSedata().getSe_code());
            });
            binding.rvProgress.setAdapter(progressAdapter);
        }
        progressAdapter.refresh((ObservableList<ProgressObserver>) list);
    }


}

