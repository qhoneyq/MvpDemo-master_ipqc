package youdian.apk.ipqc.activity;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.foxconn.youdian.apk.ipqc.bean.ListResponseData;

import autodispose2.AutoDisposeConverter;
import youdian.apk.ipqc.R;
import youdian.apk.ipqc.adapter.TableListAdapter;
import youdian.apk.ipqc.adapter.ZhichengAdapter;
import youdian.apk.ipqc.base.BaseMvpActivity;
import youdian.apk.ipqc.bean.HomeTableData;
import youdian.apk.ipqc.bean.Response;
import youdian.apk.ipqc.bean.SEData;
import youdian.apk.ipqc.contract.ChujianContract;
import youdian.apk.ipqc.databinding.IpqcHolderBinding;
import youdian.apk.ipqc.obsever.HomeTableObsever;
import youdian.apk.ipqc.obsever.SEObsever;
import youdian.apk.ipqc.presenter.ChujianPresenter;
import youdian.apk.ipqc.utils.Constans;
import youdian.apk.ipqc.utils.UserUtils;
import youdian.apk.ipqc.utils.Utils;

public class ChujianTableActivity extends BaseMvpActivity<ChujianPresenter> implements ChujianContract.View {

    private IpqcHolderBinding binding;
    private ZhichengAdapter seAdapter;
    private TableListAdapter tableListAdapter;
    private ObservableArrayList<SEObsever> seObservablelList;
    private ObservableArrayList<HomeTableObsever> tableObseversList;

    /**
     * 静态方法跳转到当前页面
     */
    public static void startMainActivity(Context context, String data) {
        Intent intent = new Intent(context, ChujianTableActivity.class);
        intent.putExtra("param", data);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.ipqc_holder;
    }

    @Override
    public void initView() {
        binding.headerview.setTitleText(getResources().getString(R.string.pagetitle_chujian));
        binding.headerview.setLeftIcon(R.mipmap.home_icon_return);
        binding.headerview.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPresenter = new ChujianPresenter();
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
                mPresenter.getTableList(itemZhichengRvBinding.getSedata().getSe_code(), Constans.FirstCheck);
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
                //跳转表头
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
