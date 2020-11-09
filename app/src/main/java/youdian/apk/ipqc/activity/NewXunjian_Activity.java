package youdian.apk.ipqc.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialog;

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
import youdian.apk.ipqc.contract.NewXunjianContract;
import youdian.apk.ipqc.databinding.ActivityIpqcTabletitleJianyanBinding;
import youdian.apk.ipqc.obsever.InsCheckResultObserver;
import youdian.apk.ipqc.presenter.NewChujianPresenter;
import youdian.apk.ipqc.presenter.NewXunjianPresenter;
import youdian.apk.ipqc.utils.Constans;

import static youdian.apk.ipqc.utils.Constans.FLAG_LINE;
import static youdian.apk.ipqc.utils.Constans.Frequency;
import static youdian.apk.ipqc.utils.Constans.HuangXian;
import static youdian.apk.ipqc.utils.Constans.Inspection;
import static youdian.apk.ipqc.utils.Constans.Shift;


/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/9/21 0021
 * Time: 上午 9:58
 * Function: 新建巡检表
 */
public class NewXunjian_Activity extends BaseMvpActivity<NewXunjianPresenter> implements NewXunjianContract.View {

    ActivityIpqcTabletitleJianyanBinding binding;

    private InsCheckResultObserver resultObserver;
    private BottomSheetDialog dialog;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDialog dialogshift;
    private String INTENTFLAG;//新建表头 or 修改表头
    private List<OptionData> listbottom;
    private String flag;

    OptionBottomSheetAdapter adapter;

    /**
     * 静态方法跳转到当前页面
     */
    public static void startActivity(Context context, Bundle data) {
        Intent intent = new Intent(context, NewXunjian_Activity.class);
        intent.putExtra("param", data);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_ipqc_tabletitle_jianyan;
    }

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        Bundle bundle = getIntent().getBundleExtra("param");
        resultObserver = (InsCheckResultObserver) bundle.getSerializable(Inspection);
        INTENTFLAG = (String) bundle.get(Constans.INTENTFLAG);
        if (INTENTFLAG.equals(Constans.NEW)) {
            String time = DatetimeUtil.INSTANCE.getNows_ss();
            resultObserver.setCheck_time(time);
            resultObserver.setCheck_quantity("Check_quantity");
            resultObserver.setWork_no("Work_no");
            resultObserver.setPart_no("part_no");
            resultObserver.setEdition("edition");
            resultObserver.setProduction_batch("Production_batch");
            resultObserver.setCheck_quantity("123");
            resultObserver.setMachine_type("Machine_type");
        }
        binding.jianyanshuliang.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        binding.setInspection(resultObserver);
        binding.headview.setTitleText(resultObserver.getIns_checklist_name() + getResources().getString(R.string.biaotouxinxi));
        binding.headview.setLeftIcon(R.mipmap.home_icon_return);
        binding.headview.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mPresenter = new NewXunjianPresenter();
        mPresenter.attachView(this);
        initBottomDialog();
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
            dialog = new BottomSheetDialog(this);
        }
        if (dialog.isShowing())
            dialog.dismiss();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(NewXunjian_Activity.this).inflate(R.layout.bottom_dialog, null);
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
     * init bottomdialog
     */
    public void initBottomDialog() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(this);
        }
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(NewXunjian_Activity.this).inflate(R.layout.bottom_dialog, null);
        ListView bottom_lv = view.findViewById(R.id.bottom_lv);
        listbottom = new ArrayList<>();
        adapter = new OptionBottomSheetAdapter(listbottom, this);
        bottom_lv.setAdapter(adapter);
        bottom_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (flag.equals(Frequency)) {
                    resultObserver.setFrequency(listbottom.get(i).getOption_value());
                    resultObserver.setFrequency_name(listbottom.get(i).getOption_name());
                    //根据value确认时间段是否可选
                    if (listbottom.get(i).getOption_value().equals(Constans.MeiBan) || listbottom.get(i).getOption_value().equals(HuangXian)) {
                        resultObserver.setPeriod(listbottom.get(i).getOption_value());
                        resultObserver.setPeriod_name(listbottom.get(i).getOption_name());
                        binding.shijianduan.setClickable(false);
                    } else {
                        binding.shijianduan.setClickable(true);
                        resultObserver.setPeriod("");
                    }

                } else if (flag.equals(Shift)) {
                    resultObserver.setShift(listbottom.get(i).getOption_value());
                    binding.banbie.setText(listbottom.get(i).getOption_name());
                } else {
                    resultObserver.setPeriod(listbottom.get(i).getOption_value());
                    binding.shijianduan.setText(listbottom.get(i).getOption_name());
                }
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(view);
    }


    @Override
    public void showCheckTypeBottomDialog(List<OptionData> list) {

        if (bottomSheetDialog.isShowing())
            bottomSheetDialog.dismiss();
        this.listbottom.clear();
        this.listbottom.addAll(list);
        adapter.notifyDataSetChanged();
        bottomSheetDialog.show();
    }

    @Override
    public void showSomeMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
        binding.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.showBottomDialog(FLAG_LINE);
            }
        });

        binding.jianyanpinlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = Frequency;
                mPresenter.getSelectInfo(Constans.Frequency);

            }
        });
        binding.shijianduan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = "";
                flag = resultObserver.getFrequency();
                if (!flag.equals("")) {
                    mPresenter.getSelectInfo(flag);
                } else
                    showSomeMsg(getResources().getString(R.string.frequency_err));
            }
        });

        binding.banbie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = Shift;
                mPresenter.getSelectInfo(Constans.Shift);
            }
        });


        binding.commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit();
            }
        });

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
        if (resultObserver.getPart_no().isEmpty()) {
            binding.liaohao.setError(getResources().getString(R.string.partno_err));
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
            binding.shengchanpici.setError(getResources().getString(R.string.product_err));
            return;
        } else {
            binding.shengchanpici.setError(null);
        }
        if (resultObserver.getMachine_type().isEmpty()) {
            binding.shengchanpici.setError(getResources().getString(R.string.jizhong_err));
            return;
        } else {
            binding.shengchanpici.setError(null);
        }
        if (resultObserver.getFrequency().isEmpty()) {
            binding.shengchanpici.setError(getResources().getString(R.string.frequency_err));
            return;
        } else {
            binding.shengchanpici.setError(null);
        }
        if (resultObserver.getPeriod().isEmpty()) {
            binding.shijianduan.setError(getResources().getString(R.string.shijianduan_err));
            return;
        } else {
            binding.shengchanpici.setError(null);
        }
        if (resultObserver.getCheck_quantity().isEmpty()) {
            binding.jianyanshuliang.setError(getResources().getString(R.string.checkno_err));
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

        if (resultObserver.getMachine_type().isEmpty()) {
            binding.jizhong.setError(getResources().getString(R.string.jizhong_err));
            return;
        } else {
            binding.jizhong.setError(null);
        }
        //页面跳转
        Bundle bundle = new Bundle();
        bundle.putSerializable(Inspection, resultObserver);
        bundle.putString(Constans.INTENTFLAG,INTENTFLAG);
        CheckDetail_Xunjian_Activity.startActivity(this, bundle);
        finish();
    }

}
