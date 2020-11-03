package youdian.apk.ipqc.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import autodispose2.AutoDisposeConverter;
import youdian.apk.dianjian.utils.DatetimeUtil;
import youdian.apk.ipqc.R;
import youdian.apk.ipqc.adapter.ActionDetailAdapter;
import youdian.apk.ipqc.adapter.InsCheckDetailAdapter;
import youdian.apk.ipqc.adapter.ProgressAdapter;
import youdian.apk.ipqc.adapter.SnChouyangAdapter;
import youdian.apk.ipqc.adapter.SuggestionAdapter;
import youdian.apk.ipqc.base.BaseMvpActivity;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.contract.CheckDetailContract_CHUJIAN;
import youdian.apk.ipqc.contract.CheckDetailContract_XUNJIAN;
import youdian.apk.ipqc.databinding.ActivityFirstcheckdetailBinding;
import youdian.apk.ipqc.databinding.ActivityInscheckdetailBinding;
import youdian.apk.ipqc.obsever.CountModel;
import youdian.apk.ipqc.obsever.FirstCheckItemObserver;
import youdian.apk.ipqc.obsever.FirstCheckResultObserver;
import youdian.apk.ipqc.obsever.InsCheckItemObserver;
import youdian.apk.ipqc.obsever.InsCheckResultObserver;
import youdian.apk.ipqc.obsever.InsCheckSnObserver;
import youdian.apk.ipqc.obsever.OptionObserver;
import youdian.apk.ipqc.obsever.ProgressObserver;
import youdian.apk.ipqc.presenter.CheckDetailPresenter_CHUJIAN;
import youdian.apk.ipqc.presenter.CheckDetailPresenter_XUNJIAN;
import youdian.apk.ipqc.utils.Constans;
import youdian.apk.ipqc.utils.UserUtils;

import static youdian.apk.ipqc.utils.Constans.Abnormal;
import static youdian.apk.ipqc.utils.Constans.FLAG_SN;
import static youdian.apk.ipqc.utils.Constans.FirstCheck;
import static youdian.apk.ipqc.utils.Constans.Inspection;
import static youdian.apk.ipqc.utils.Constans.NEW;
import static youdian.apk.ipqc.utils.Constans.Normal;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/21 0021
 * Time: 上午 11:39
 * Function:1. 判断是否需要恢复暂存记录
 */
public class CheckDetail_Xunjian_Activity extends BaseMvpActivity<CheckDetailPresenter_XUNJIAN> implements CheckDetailContract_XUNJIAN.View, InsCheckDetailAdapter.onCountChangeListener, SnChouyangAdapter.onCountChangeListener {

    ActivityInscheckdetailBinding binding;
    private BottomSheetDialog dialog;
    private ProgressAdapter progressAdapter;
    private SuggestionAdapter suggestionAdapter;
    List<ProgressObserver> processList = new ObservableArrayList<>();
    private InsCheckDetailAdapter checkDetailAdapter;
    private SnChouyangAdapter snChouyangAdapter;
    private InsCheckResultObserver resultObserver;//检验记录表头
    List<InsCheckItemObserver> allCheckItemList;//全部检验项
    List<InsCheckItemObserver> onCheckItemList;//显示检验项
    List<InsCheckSnObserver> snCheckItemList;//抽样检验
    ObservableList<OptionObserver> suggestionList;
    private String INTENTFLAG;
    private int process_id;
    private CountModel countModel = new CountModel();


    /**
     * 静态方法跳转到当前页面
     */
    public static void startActivity(Context context, Bundle data) {
        Intent intent = new Intent(context, CheckDetail_Xunjian_Activity.class);
        intent.putExtra("param", data);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_inscheckdetail;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getBundleExtra("param");
        resultObserver = (InsCheckResultObserver) bundle.getSerializable(Inspection);
        INTENTFLAG = bundle.getString(Constans.INTENTFLAG);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        binding.tvLine.setText(resultObserver.getLine_name());
        binding.tvFrequency.setText(resultObserver.getFrequency());
        binding.tvPeriod.setText(resultObserver.getPeriod());
        binding.setCount(countModel);
        snCheckItemList = new ArrayList<>();
        mPresenter = new CheckDetailPresenter_XUNJIAN();
        mPresenter.attachView(this);
        mPresenter.getProcess(resultObserver.getIns_checklist_id() + "");
        mPresenter.getCheckSuggestion(Constans.FirstSug);

        binding.heardview.setTitleText(resultObserver.getIns_checklist_name());
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
                //保存当前检验记录
                List<InsCheckItemObserver> listResult = new ArrayList<>();
                for (InsCheckItemObserver item : allCheckItemList) {
                    listResult.add(item);
                }
                resultObserver.setInspection_result_details(listResult);
                resultObserver.setInspection_check_sns(snCheckItemList);
                bundle.putSerializable(Inspection, resultObserver);
                bundle.putString(Constans.INTENTFLAG, Constans.REINPUT);
                //跳转表头
                NewXunjian_Activity.startActivity(CheckDetail_Xunjian_Activity.this, bundle);
                finish();
            }
        });
        binding.btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getNumber() == allCheckItemList.size()) {
//                    showDialog();
                    resultObserver.setResult_status(getStatus());
                    resultObserver.setInspection_result_details(allCheckItemList);
                    resultObserver.setInspection_check_sns(snCheckItemList);
                    mPresenter.postInsResult(resultObserver);
                } else {
                    onError(getResources().getString(R.string.lackmsg));
                }
            }
        });

        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQrCode();
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

    /**
     * 显示信息
     *
     * @param msg
     */
    @Override
    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
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
    public void setprocess(List<ProgressObserver> list) {
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
            mPresenter.getCheckListData(resultObserver.getIns_checklist_id() + "");
        else {
            allCheckItemList = resultObserver.getInspection_result_details();
            snCheckItemList = resultObserver.getInspection_check_sns();
            setCheckListData(allCheckItemList);
        }
        binding.rvProgress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                progressAdapter.setSelectPosition(i);
                if (processList.get(i).getProcess_code().equals(Constans.ChouYang)) {
                    //抽样巡检
                    binding.rlAction.setVisibility(View.GONE);
                    binding.rlChouyang.setVisibility(View.VISIBLE);
                    if (snChouyangAdapter == null) {
                        snChouyangAdapter = new SnChouyangAdapter(snCheckItemList, CheckDetail_Xunjian_Activity.this, CheckDetail_Xunjian_Activity.this);
                        binding.lvsn.setAdapter(snChouyangAdapter);
                    }
                    snChouyangAdapter.notifyDataSetChanged();

                } else { //正常巡检
                    binding.rlAction.setVisibility(View.VISIBLE);
                    binding.rlChouyang.setVisibility(View.GONE);
                    process_id = processList.get(i).getId();
                    //获取单个工序检验项
                    binding.tvProgressNote.setText(processList.get(i).getNote());
                    showCheckItemByProcess(process_id);
                }

            }
        });
    }


    /**
     * 响应presenter,显示底部弹窗（建议）
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
        View view = LayoutInflater.from(CheckDetail_Xunjian_Activity.this).inflate(R.layout.dialog_suggest, null);
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
                resultObserver.setInspection_result_details(allCheckItemList);
                mPresenter.postInsResult(resultObserver);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);

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
    public void setCheckListData(List<InsCheckItemObserver> list) {
        snCheckItemList = new ArrayList<>();
        onCheckItemList = new ArrayList<>();
        allCheckItemList = new ArrayList<>();
        allCheckItemList.addAll(list);
        countModel.setCount_all(allCheckItemList.size() + "");
        binding.rvAction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvAction.setItemAnimator(new DefaultItemAnimator());
        showCheckItemByProcess(process_id);
    }

    /**
     * 显示单个工序检验项
     *
     * @param p_id
     */
    public void showCheckItemByProcess(int p_id) {
        onCheckItemList.clear();
        for (InsCheckItemObserver checkItemObserver : allCheckItemList) {
            if (checkItemObserver.getProcess_id() == p_id) {
                onCheckItemList.add(checkItemObserver);
            }
        }
        if (checkDetailAdapter == null) {
            checkDetailAdapter = new InsCheckDetailAdapter(this, onCheckItemList, this);
            binding.rvAction.setAdapter(checkDetailAdapter);

        }
        countModel.setCount_all(onCheckItemList.size() + "");
        binding.rvAction.removeAllViews();

        checkDetailAdapter.noyify(onCheckItemList);
        binding.rvAction.setAdapter(checkDetailAdapter);
    }
//
//    public void showCheckItemByProcess(int p_id) {
//        for (InsCheckItemObserver checkItemObserver : allCheckItemList) {
//            if (checkItemObserver.getProcess_id() == p_id) {
//                checkItemObserver.setIsvisiable(true);
//            } else
//                checkItemObserver.setIsvisiable(false);
//        }
//
//        checkDetailAdapter.notifyDataSetChanged();
//    }

    /**
     * 查询当前点检结果
     *
     * @return
     */
    private String getStatus() {
        String isNomal = Normal;
        for (InsCheckItemObserver check : allCheckItemList) {
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
        for (InsCheckItemObserver check : allCheckItemList) {
            if (!check.getDetail_status().equals("")) {
                check.setEmp_no(UserUtils.getInstance().getPnum());
                check.setPeriod(resultObserver.getPeriod());
                check.setFrequency(resultObserver.getFrequency());
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
    public void getCheckDetail(InsCheckItemObserver checkItemObserver) {
        checkItemObserver.setCheck_time(DatetimeUtil.INSTANCE.getNows_ss());
        for (int i = 0; i < allCheckItemList.size(); i++) {
            InsCheckItemObserver check = allCheckItemList.get(i);
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

    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Constans.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(SmallCaptureActivity.class);
        integrator.initiateScan();
    }

    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constans.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //二维码扫描结果回调
        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == RESULT_OK) {
            IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            String scanResult = Result.getContents();
            for (InsCheckSnObserver observer : snCheckItemList) {
                if (observer.getSn().equals(scanResult))
                    snCheckItemList.remove(observer);
            }
            InsCheckSnObserver snObserver = new InsCheckSnObserver();
            snObserver.setCheck_time(DatetimeUtil.INSTANCE.getNows_ss());
            snObserver.setSn(scanResult);
            snCheckItemList.add(snObserver);
            snChouyangAdapter.notifyDataSetChanged();
        } else {
            showMsg("请重新扫描");
        }
    }


    @Override
    public void getChouyangCount(int s) {
        String acation_count = s + "";
        countModel.setCount_all(acation_count);
        countModel.setCount_ed(acation_count);
    }

    @Override
    public void setResult(int position, String result) {

        snCheckItemList.get(position).setDetail_status(result);
        snCheckItemList.get(position).setDetail_value(result);
    }

    @Override
    public void setNote(int position, String note) {
        snCheckItemList.get(position).setNote(note);

    }
}

