package youdian.apk.ipqc.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
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
import com.qihoo360.replugin.RePlugin;

import java.util.ArrayList;
import java.util.List;

import autodispose2.AutoDisposeConverter;
import youdian.apk.dianjian.utils.DatetimeUtil;
import youdian.apk.ipqc.R;
import youdian.apk.ipqc.adapter.InsCheckDetailAdapter;
import youdian.apk.ipqc.adapter.ProgressAdapter;
import youdian.apk.ipqc.adapter.SnChouyangAdapter;
import youdian.apk.ipqc.base.BaseMvpActivity;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.contract.CheckDetailContract_XUNJIAN;
import youdian.apk.ipqc.databinding.ActivityInscheckdetailBinding;
import youdian.apk.ipqc.obsever.CountModel;
import youdian.apk.ipqc.obsever.FirstCheckItemObserver;
import youdian.apk.ipqc.obsever.InsCheckItemObserver;
import youdian.apk.ipqc.obsever.InsCheckResultObserver;
import youdian.apk.ipqc.obsever.InsCheckSnObserver;
import youdian.apk.ipqc.obsever.OptionObserver;
import youdian.apk.ipqc.obsever.ProgressObserver;
import youdian.apk.ipqc.presenter.CheckDetailPresenter_XUNJIAN;
import youdian.apk.ipqc.utils.CommonUtils;
import youdian.apk.ipqc.utils.Constans;
import youdian.apk.ipqc.utils.DividerItemDecoration;
import youdian.apk.ipqc.utils.MycountDownTimer;
import youdian.apk.ipqc.utils.UserUtils;
import youdian.apk.ipqc.utils.Utils;
import youdian.apk.ipqc.wedige.ControlScrollLayoutManager;
import youdian.apk.ipqc.wedige.CustomPopupWindow;

import static youdian.apk.ipqc.utils.Constans.Abnormal;
import static youdian.apk.ipqc.utils.Constans.Inspection;
import static youdian.apk.ipqc.utils.Constans.NEW;
import static youdian.apk.ipqc.utils.Constans.Normal;
import static youdian.apk.ipqc.utils.Constans.QRACTIVITY;
import static youdian.apk.ipqc.utils.Constans.REQ_QR_CODE;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/21 0021
 * Time: 上午 11:39
 * Function:1. 判断是否需要恢复暂存记录
 */
public class CheckDetail_Xunjian_Activity extends BaseMvpActivity<CheckDetailPresenter_XUNJIAN> implements CheckDetailContract_XUNJIAN.View, InsCheckDetailAdapter.onCountChangeListener, SnChouyangAdapter.onCountChangeListener {

    ActivityInscheckdetailBinding binding;
    private CustomPopupWindow customPopupWindow;
    private BottomSheetDialog dialog;
    private ProgressAdapter progressAdapter;
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
        binding.tvFrequency.setText(resultObserver.getFrequency_name());
        binding.tvPeriod.setText(resultObserver.getPeriod_name());
        binding.setCount(countModel);
        snCheckItemList = new ArrayList<>();
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false){
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };
        ControlScrollLayoutManager layoutManager = new ControlScrollLayoutManager(this, RecyclerView.VERTICAL, false, binding.rvAction);
        layoutManager.setCanAutoScroll(true);
        binding.rvAction.setHasFixedSize(true);
//        binding.rvAction.addItemDecoration(new DividerItemDecoration(CheckDetail_Xunjian_Activity.this,DividerItemDecoration.VERTICAL_LIST));
        binding.rvAction.setItemAnimator(new DefaultItemAnimator());
        binding.rvAction.setLayoutManager(layoutManager);
        binding.rvAction.setItemAnimator(null);
        mPresenter = new CheckDetailPresenter_XUNJIAN();
        mPresenter.attachView(this);
        mPresenter.getProcess(resultObserver.getIns_checklist_id() + "");

        binding.heardview.setTitleText(resultObserver.getIns_checklist_name());
        binding.heardview.setLeftIcon(R.mipmap.home_icon_return);
        binding.heardview.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(CheckDetail_Xunjian_Activity.this)
                        .setTitle("退出")
                        .setMessage(getResources().getString(R.string.returnmsg))
                        .setNegativeButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        finish();

                                    }
                                }).setPositiveButton("取消", null).show();
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
                if (Utils.isFastDoubleClick()) {
                    return;
                }
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
        customPopupWindow = new CustomPopupWindow.Builder(this)
                .setContentView(R.layout.popwindow_result)
                .setCancleClickOutSide(true)
                .setwidth(getWindow().getWindowManager().getDefaultDisplay().getWidth())
//                .setwidth(getResources().getDimensionPixelSize(R.dimen.dp_240))
                .setheight(getWindow().getWindowManager().getDefaultDisplay().getHeight())
//                .setheight(getResources().getDimensionPixelSize(R.dimen.dp_160))
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
            mPresenter.getCheckListData(resultObserver.getIns_checklist_id() + "", resultObserver.getFrequency());
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

            MycountDownTimer downTimer = new MycountDownTimer(this, tvofftime, 6000, 1);
            downTimer.start();
            customPopupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);
            CommonUtils.setBackgroundAlpha((float) 0.3, getWindow());
        } else {
            img_icon.setImageResource(R.mipmap.icon_red);
            tvResult.setTextColor(Color.BLACK);
            tvResult.setText(result);

            MycountDownTimer downTimer = new MycountDownTimer(customPopupWindow, tvofftime, 6000, 1);
            downTimer.start();
            customPopupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);
            CommonUtils.setBackgroundAlpha((float) 1.0, getWindow());
        }

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
            checkDetailAdapter.setHasStableIds(true);
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
            if (!check.getDetail_status().equals("")) {//有狀態值
                if (!check.getControl_code().equals(Constans.Number)) {//除數字框外,you狀態值判斷
                    if (check.getDetail_status().equals(Abnormal)
                            && check.getNote().equals("")) {
                        break;
                    } else
                        count++;
                } else {
                    count++;
                }
                check.setEmp_no(UserUtils.getInstance().getPnum());
                check.setTime_period(resultObserver.getPeriod());
                check.setFrequency(resultObserver.getFrequency());
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
        for (int i = 0; i < allCheckItemList.size(); i++) {
            InsCheckItemObserver check = allCheckItemList.get(i);
            if (checkItemObserver.getItem().equals(check.getItem())) {
                check.setNote(checkItemObserver.getNote());
                check.setDetail_value(checkItemObserver.getDetail_value());
                check.setDetail_status(checkItemObserver.getDetail_status());
                if (check.getCheck_time().equals("")){
                    check.setCheck_time(DatetimeUtil.INSTANCE.getNows_ss());
                }
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
//        IntentIntegrator integrator = new IntentIntegrator(this);
//        integrator.setOrientationLocked(false);
//        integrator.setCaptureActivity(SmallCaptureActivity.class);
//        integrator.initiateScan();


        Intent intent = new Intent();
        intent.setComponent(new ComponentName(RePlugin.getHostContext().getPackageName(), QRACTIVITY));
        CheckDetail_Xunjian_Activity.this.startActivityForResult(intent, REQ_QR_CODE);

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
//        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == RESULT_OK) {
//            IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//            String scanResult = Result.getContents();
        if (requestCode == REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constans.INTENT_EXTRA_KEY_QR_SCAN);
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(CheckDetail_Xunjian_Activity.this)
                    .setTitle("退出")
                    .setMessage(getResources().getString(R.string.returnmsg))
                    .setNegativeButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    finish();

                                }
                            }).setPositiveButton("取消", null).show();
            return true;

        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            System.out.println("HOME has been pressed yet ...");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获取当前焦点所在的控件；
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText) {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();
                // 判断点击的点是否落在当前焦点所在的 view 上；
                if (!r.contains(rawX, rawY)) {
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideInput(v, ev)) {
//
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
//    }
//
//    /**
//     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
//     *
//     * @param v
//     * @param event
//     * @return
//     */
//    public  boolean isShouldHideInput(View v, MotionEvent event) {
//        if (v != null && (v instanceof EditText)) {
//            int[] leftTop = { 0, 0 };
//            //获取输入框当前的location位置
//            v.getLocationInWindow(leftTop);
//            int left = leftTop[0];
//            int top = leftTop[1];
//            int bottom = top + v.getHeight();
//            int right = left + v.getWidth();
//            if (event.getX() > left && event.getX() < right
//                    && event.getY() > top && event.getY() < bottom) {
//                // 点击的是输入框区域，保留点击EditText的事件
//                return false;
//            } else {
//                return true;
//            }
//        }
//        return false;
//    }

}

