package youdian.apk.ipqc.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import autodispose2.AutoDisposeConverter;
import youdian.apk.ipqc.R;
import youdian.apk.ipqc.adapter.TableAdapter;
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
import youdian.apk.ipqc.utils.DeviceUtil;
import youdian.apk.ipqc.utils.UserUtils;
import youdian.apk.ipqc.utils.Utils;

public class TableListActivity extends BaseMvpActivity<TableListPresenter> implements TableListContract.View {

    private IpqcHolderBinding binding;
    private ZhichengAdapter seAdapter;
    private TableAdapter tableListAdapter;
    private ObservableArrayList<SEObsever> seList;
    private ObservableArrayList<HomeTableObsever> tableObseversList;
    private String flag;//初件 or 巡检
    private String devId;

    private FirstCheckResultObserver firstCheckResult;//初件记录，首页确定se name and code,checklist name and code ,and checkpersion
    private InsCheckResultObserver InsCheckResult;//巡检记录，首页确定se name and code,checklis  t name and code ,and checkpersion

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
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        flag = getIntent().getStringExtra("param");
        tableObseversList = new ObservableArrayList<>();

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                devId = DeviceUtil.readDeviceId(TableListActivity.this);
            }
        }).start();
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
        this.seList = seObservablelList;

        if (seAdapter == null) {
            seAdapter = new ZhichengAdapter(this, seList);
            binding.ipqcMainZhichengRv.setAdapter(seAdapter);
            seAdapter.setSelectPosition(0);
            if (flag.equals(Constans.FirstCheck))
                mPresenter.getFirstTableList(seList.get(0).getSe_code());
            else
                mPresenter.getInsTableList(seList.get(0).getSe_code());
        }
        seAdapter.notifyDataSetChanged();
        binding.ipqcMainZhichengRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                seAdapter.setSelectPosition(i);
                seAdapter.notifyDataSetChanged();
                if (flag.equals(Constans.FirstCheck))
                    mPresenter.getFirstTableList(seList.get(i).getSe_code());
                else
                    mPresenter.getInsTableList(seList.get(i).getSe_code());
            }
        });
    }


    @Override
    public void setTableList(ObservableArrayList<HomeTableObsever> tableList) {
        tableObseversList.clear();
        tableObseversList = tableList;

        tableListAdapter = new TableAdapter(this, tableObseversList);
        binding.ipqcMainTablelistRv.setAdapter(tableListAdapter);
        binding.ipqcMainTablelistRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                HomeTableObsever tableObsever = tableObseversList.get(i);

                if (flag.equals(Constans.FirstCheck)) {
                    firstCheckResult = new FirstCheckResultObserver();
                    firstCheckResult.setCheck_person(UserUtils.getInstance().getPnum());
                    firstCheckResult.setOrganization_name(UserUtils.getInstance().getBUName());
                    firstCheckResult.setOrganization_code(UserUtils.getInstance().getBUCODE());
                    firstCheckResult.setSe_name(tableObsever.getSe_name());
                    firstCheckResult.setSe_code(tableObsever.getSe());
                    firstCheckResult.setFirst_checklist_id(tableObsever.getId());
                    firstCheckResult.setFirst_checklist_name(tableObsever.getList_name());
                    firstCheckResult.setFirst_checklist_code(tableObsever.getList_code());
                    firstCheckResult.setDev_code(devId);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constans.FirstCheck, firstCheckResult);
                    bundle.putString(Constans.INTENTFLAG, Constans.NEW);
                    //跳转表头
                    NewChujian_Activity.startActivity(TableListActivity.this, bundle);
                } else {
                    InsCheckResult = new InsCheckResultObserver();
                    InsCheckResult.setCheck_person(UserUtils.getInstance().getPnum());
                    InsCheckResult.setSe_name(tableObsever.getSe_name());
                    InsCheckResult.setOrganization_name(UserUtils.getInstance().getBUName());
                    InsCheckResult.setOrganization_code(UserUtils.getInstance().getBUCODE());
                    InsCheckResult.setSe_code(tableObsever.getSe());
                    InsCheckResult.setIns_checklist_id(tableObsever.getId());
                    InsCheckResult.setIns_checklist_name(tableObsever.getList_name());
                    InsCheckResult.setIns_checklist_code(tableObsever.getList_code());
                    InsCheckResult.setDev_code(devId);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constans.Inspection, InsCheckResult);
                    bundle.putString(Constans.INTENTFLAG, Constans.NEW);
                    //跳转表头
                    NewXunjian_Activity.startActivity(TableListActivity.this, bundle);
                }
            }
        });
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
