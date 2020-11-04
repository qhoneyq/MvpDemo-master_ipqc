package youdian.apk.ipqc.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import youdian.apk.dianjian.utils.DatetimeUtil;
import youdian.apk.ipqc.R;
import youdian.apk.ipqc.adapter.BottomSheetAdapter;
import youdian.apk.ipqc.adapter.OptionBottomSheetAdapter;
import youdian.apk.ipqc.base.BaseMvpActivity;
import youdian.apk.ipqc.bean.Lines;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.contract.NewChujianContract;
import youdian.apk.ipqc.databinding.ActivityIpqcTabletitleChujianBinding;
import youdian.apk.ipqc.obsever.FirstCheckResultObserver;
import youdian.apk.ipqc.presenter.LoginPresenter;
import youdian.apk.ipqc.presenter.NewChujianPresenter;
import youdian.apk.ipqc.utils.Constans;

import static youdian.apk.ipqc.utils.Constans.FLAG_LINE;
import static youdian.apk.ipqc.utils.Constans.FLAG_SHIFT;
import static youdian.apk.ipqc.utils.Constans.FLAG_SN;
import static youdian.apk.ipqc.utils.Constans.FirstCheck;


/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/21 0021
 * Time: 上午 9:58
 * Function: 新建初件表
 */
public class NewChujian_Activity extends BaseMvpActivity<NewChujianPresenter> implements NewChujianContract.View {

    ActivityIpqcTabletitleChujianBinding binding;

    private FirstCheckResultObserver resultObserver;
    private BottomSheetDialog dialog;
    private BottomSheetDialog checktypedialog;
    private BottomSheetDialog dialogshift;
    private String INTENTFLAG;


    /**
     * 静态方法跳转到当前页面
     */
    public static void startActivity(Context context, Bundle data) {
        Intent intent = new Intent(context, NewChujian_Activity.class);
        intent.putExtra("param", data);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_ipqc_tabletitle_chujian;
    }

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        Bundle bundle = getIntent().getBundleExtra("param");
        resultObserver = (FirstCheckResultObserver) bundle.getSerializable(FirstCheck);
        INTENTFLAG = (String) bundle.get(Constans.INTENTFLAG);
        if (INTENTFLAG.equals(Constans.NEW)) {
            String time = DatetimeUtil.INSTANCE.getNows_ss();
            resultObserver.setCheck_time(time);
        }
//        resultObserver.setSn("12345sn");
//        resultObserver.setCheck_quantity("Check_quantity");
//        resultObserver.setWork_no("Work_no");
//        resultObserver.setPart_no("part_no");
//        resultObserver.setEdition("edition");
//        resultObserver.setProduction_batch("Production_batch");
//        resultObserver.setCheck_quantity("123");
//        resultObserver.setMachine_type("Machine_type");
        binding.setFirstcheck(resultObserver);
        binding.jianyanshuliang.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        binding.headview.setTitleText(resultObserver.getFirst_checklist_name() + getResources().getString(R.string.biaotouxinxi));
        binding.headview.setLeftIcon(R.mipmap.home_icon_return);
        binding.headview.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mPresenter = new NewChujianPresenter();
        mPresenter.attachView(this);
        int width = 50;
        Drawable drawable = getResources().getDrawable(R.drawable.divider);
        binding.llAll.setDividerDrawable(drawable);
        binding.llAll.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        dealData();
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
    public void showError(int flag, String msg) {

    }

    @Override
    public void setLines(List<Lines> list) {

    }

    /**
     * 显示线别下拉选项
     */
    @Override
    public void showLineBottomDialog(List<Lines> list) {
        if (dialog == null) {
            dialog = new BottomSheetDialog(this);
        }
        if (dialog.isShowing())
            dialog.dismiss();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(NewChujian_Activity.this).inflate(R.layout.bottom_dialog, null);
        ListView bottom_lv = view.findViewById(R.id.bottom_lv);
        BottomSheetAdapter adapter = new BottomSheetAdapter(list, this);
        bottom_lv.setAdapter(adapter);
        bottom_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                resultObserver.setLine_code(list.get(i).getLine_code());
                resultObserver.setLine_name(list.get(i).getLine_name());
                dialog.dismiss();

            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    /**
     * 初见类型下拉
     *
     * @param list
     */
    @Override
    public void showCheckTypeBottomDialog(List<OptionData> list) {
        if (checktypedialog == null) {
            checktypedialog = new BottomSheetDialog(this);
        }
        if (checktypedialog.isShowing())
            checktypedialog.dismiss();
        checktypedialog.setCancelable(false);
        checktypedialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(NewChujian_Activity.this).inflate(R.layout.bottom_dialog, null);
        ListView bottom_lv = view.findViewById(R.id.bottom_lv);
        OptionBottomSheetAdapter adapter = new OptionBottomSheetAdapter(list, this);
        bottom_lv.setAdapter(adapter);
        bottom_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                resultObserver.setChujian_type(list.get(i).getOption_value());
                binding.chujianleixing.setText(list.get(i).getOption_name());
                checktypedialog.dismiss();
            }
        });

        checktypedialog.setContentView(view);
        checktypedialog.show();
    }


    /**
     * 显示白晚班下拉选项
     */
    @Override
    public void showShiftBottomDialog(List<OptionData> list) {
        if (dialogshift == null) {
            dialogshift = new BottomSheetDialog(this);
        }
        if (dialogshift.isShowing())
            dialogshift.dismiss();

        dialogshift.setCancelable(false);
        dialogshift.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(NewChujian_Activity.this).inflate(R.layout.bottom_dialog, null);
        ListView bottom_lv = view.findViewById(R.id.bottom_lv);
        OptionBottomSheetAdapter adapter = new OptionBottomSheetAdapter(list, this);
        bottom_lv.setAdapter(adapter);
        bottom_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                resultObserver.setShift(list.get(i).getOption_value());
                binding.banbie.setText(list.get(i).getOption_name());
                dialogshift.dismiss();
            }
        });

        dialogshift.setContentView(view);
        dialogshift.show();
    }


    /**
     * 获取准备数据
     */
    public void dealData() {
        //TIME
        binding.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        //LINE
        mPresenter.getLines(resultObserver.getSe_code());
        mPresenter.getSelectInfo("CHUJIAN_TYPE");
        mPresenter.getSelectInfo("SHIFT");

        binding.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.showBottomDialog(FLAG_LINE);
            }
        });

        //CHECKTYPE
        binding.chujianleixing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.showBottomDialog(Constans.FLAG_CHECKTYPE);
            }
        });

        //SHIFT

        binding.banbie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.showBottomDialog(FLAG_SHIFT);
            }
        });

        //SN
        binding.ipqcChujianTabletitleSnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQrCode();
            }
        });

        binding.commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit();
            }
        });

    }

    /**
     * 选择时间
     */
    public void showDateDialog() {
        Calendar d = Calendar.getInstance(Locale.CHINA);        // 创建一个日历引用d，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
        Date myDate = new Date();        // 创建一个Date实例
        d.setTime(myDate);        // 设置日历的时间，把一个新建Date实例myDate传入
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);        //初始化默认日期year, month, day
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
            /**
             * 点击确定后，在这个方法中获取年月日
             */
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                binding.time.setText(date + " " + DatetimeUtil.INSTANCE.getSecond_ss());
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    @Override
    public void hideDialog() {

    }

    @Override
    public boolean isShowingDialog() {
        return false;
    }

    @Override
    public void failed() {

    }

    @Override
    public void onError(String errMessage) {

    }


    /**
     * 提交数据
     */
    private void commit() {
        if (resultObserver.getSn().isEmpty()) {
            binding.snTv.setError(getResources().getString(R.string.sn_err));
            return;
        } else {
            binding.snTv.setError(null);
        }
        if (resultObserver.getShift().isEmpty()) {
            binding.banbie.setError(getResources().getString(R.string.shift_err));
            return;
        } else {
            binding.banbie.setError(null);
        }
        if (resultObserver.getWork_no().isEmpty()) {
            binding.gongling.setError(getResources().getString(R.string.workno_err));
            return;
        } else {
            binding.gongling.setError(null);
        }
        if (resultObserver.getWork_no().isEmpty()) {
            binding.liaohao.setError(getResources().getString(R.string.workno_err));
            return;
        } else {
            binding.liaohao.setError(null);
        }
        if (resultObserver.getEdition().isEmpty()) {
            binding.banci.setError(getResources().getString(R.string.banci_err));
            return;
        } else {
            binding.banci.setError(null);
        }
        if (resultObserver.getProduction_batch().isEmpty()) {
            binding.shengchanpici.setError(getResources().getString(R.string.banci_err));
            return;
        } else {
            binding.shengchanpici.setError(null);
        }
        if (resultObserver.getLine_code().isEmpty() || resultObserver.getLine_name().isEmpty()) {
            binding.line.setError(getResources().getString(R.string.line_err));
            return;
        } else {
            binding.line.setError(null);
        }
        if (resultObserver.getCheck_quantity().isEmpty()) {
            binding.jianyanshuliang.setError(getResources().getString(R.string.checkno_err));
            return;
        } else {
            binding.jianyanshuliang.setError(null);
        }


        if (resultObserver.getChujian_type().isEmpty()) {
            binding.chujianleixing.setError(getResources().getString(R.string.checktype_err));
            return;
        } else {
            binding.chujianleixing.setError(null);
        }
        if (resultObserver.getMachine_type().isEmpty()) {
            binding.jizhong.setError(getResources().getString(R.string.jizhong_err));
            return;
        } else {
            binding.jizhong.setError(null);
        }
        //页面跳转
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constans.FirstCheck, resultObserver);
        bundle.putString(Constans.INTENTFLAG,INTENTFLAG);
        CheckDetail_Chujian_Activity.startActivity(this, bundle);
        finish();
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
            resultObserver.setSn(scanResult);
        } else {
            showError(FLAG_SN, "请重新扫描");
        }
    }


}
