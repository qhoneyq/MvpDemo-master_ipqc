package youdian.apk.ipqc.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import autodispose2.AutoDisposeConverter;
import youdian.apk.ipqc.R;
import youdian.apk.ipqc.adapter.TableListAdapter;
import youdian.apk.ipqc.adapter.ZhichengAdapter;
import youdian.apk.ipqc.base.BaseMvpActivity;
import youdian.apk.ipqc.contract.TableListContract;
import youdian.apk.ipqc.databinding.IpqcHolderBinding;
import youdian.apk.ipqc.obsever.FirstCheckResultObserver;
import youdian.apk.ipqc.obsever.HomeTableObsever;
import youdian.apk.ipqc.obsever.InsCheckResultObserver;
import youdian.apk.ipqc.obsever.SEObsever;
import youdian.apk.ipqc.presenter.TableListPresenter;
import youdian.apk.ipqc.utils.Constans;
import youdian.apk.ipqc.utils.UserUtils;
import youdian.apk.ipqc.utils.Utils;

public class TableListActivity extends BaseMvpActivity<TableListPresenter> implements TableListContract.View {

    private IpqcHolderBinding binding;
    private ZhichengAdapter seAdapter;
    private TableListAdapter tableListAdapter;
    private ObservableArrayList<SEObsever> seObservablelList;
    private ObservableArrayList<HomeTableObsever> tableObseversList;
    private String flag;//初件 or 巡检

    private FirstCheckResultObserver firstCheckResult;//初件记录，首页确定se name and code,checklist name and code ,and checkpersion
    private InsCheckResultObserver InsCheckResult;//巡检记录，首页确定se name and code,checklist name and code ,and checkpersion

    /**
     * 静态方法跳转到当前页面
     */
    public static void startTableListActivity(Context context, String data) {
        Intent intent = new Intent(context, TableListActivity.class);
        intent.putExtra("param", data);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.ipqc_holder;
    }

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this,getLayoutId());
        flag = getIntent().getStringExtra("param");
        if (flag.equals(Constans.FirstCheck)) {
            binding.headerview.setTitleText(getResources().getString(R.string.pagetitle_chujian));
        } else
            binding.headerview.setTitleText(getResources().getString(R.string.pagetitle_xunjain));

        binding.headerview.setLeftIcon(R.mipmap.home_icon_return);
        binding.headerview.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPresenter = new TableListPresenter();
        mPresenter.attachView(this);
        getSEList();
    }

    private void showSomeMsg(Object res) {
        if (res instanceof String) {
            Utils.getInstance().showMsg(this, (String) res);
        } else if (res instanceof Integer) {
            Utils.getInstance().showMsg(this, getText((Integer) res).toString());
        }
    }

    public void getSEList() {
        mPresenter.getSeList(UserUtils.getInstance().getBUCODE());
    }


    @Override
    public void setSEList(ObservableArrayList<SEObsever> seObservablelList) {
        binding.ipqcMainZhichengRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.ipqcMainZhichengRv.setItemAnimator(new DefaultItemAnimator());
        if (seAdapter == null) {
            seAdapter = new ZhichengAdapter();
            seAdapter.setOnItemClickListener(itemZhichengRvBinding -> {
                if (flag.equals(Constans.FirstCheck))
                    mPresenter.getFirstTableList(itemZhichengRvBinding.getSedata().getSe_code());
                else
                    mPresenter.getInsTableList(itemZhichengRvBinding.getSedata().getSe_code());
            });
            binding.ipqcMainZhichengRv.setAdapter(seAdapter);
        }
        seAdapter.refresh(seObservablelList);
    }


    @Override
    public void setTableList(ObservableArrayList<HomeTableObsever> tableObseversList) {
        binding.ipqcMainTablelistRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.ipqcMainTablelistRv.setItemAnimator(new DefaultItemAnimator());
        if (tableListAdapter == null) {
            tableListAdapter = new TableListAdapter();
            tableListAdapter.setOnItemClickListener(itemHometableRvBinding -> {
                if (flag.equals(Constans.FirstCheck)){
                    firstCheckResult = new FirstCheckResultObserver();
                    firstCheckResult.setCheck_person(UserUtils.getInstance().getPnum());
                    firstCheckResult.setSe_name(itemHometableRvBinding.getTableitem().getSe_name());
                    firstCheckResult.setSe_code(itemHometableRvBinding.getTableitem().getSe());
                    firstCheckResult.setFirst_checklist_name(itemHometableRvBinding.getTableitem().getList_name());
                    firstCheckResult.setFirst_checklist_code(itemHometableRvBinding.getTableitem().getList_code());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constans.FirstCheck, firstCheckResult);
                    //跳转表头
                    NewChujian_Activity.startActivity(this, bundle);
                    finish();
                }else {
                    InsCheckResult = new InsCheckResultObserver();
                    InsCheckResult.setCheck_person(UserUtils.getInstance().getPnum());
                    InsCheckResult.setSe_name(itemHometableRvBinding.getTableitem().getSe_name());
                    InsCheckResult.setSe_code(itemHometableRvBinding.getTableitem().getSe());
                    InsCheckResult.setFirst_checklist_name(itemHometableRvBinding.getTableitem().getList_name());
                    InsCheckResult.setFirst_checklist_code(itemHometableRvBinding.getTableitem().getList_code());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constans.Inspection, InsCheckResult);
                    //跳转表头
                    NewChujian_Activity.startActivity(this, bundle);
                    finish();
                }

            });
            binding.ipqcMainTablelistRv.setAdapter(tableListAdapter);
        }
        tableListAdapter.refresh(tableObseversList);
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
}
