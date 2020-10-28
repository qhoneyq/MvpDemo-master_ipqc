package youdian.apk.ipqc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import youdian.apk.ipqc.R;
import youdian.apk.ipqc.adapter.BottomSheetAdapter;
import youdian.apk.ipqc.adapter.OptionBottomSheetAdapter;
import youdian.apk.ipqc.base.BaseMvpActivity;
import youdian.apk.ipqc.bean.Lines;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.contract.NewXunjianContract;
import youdian.apk.ipqc.databinding.ActivityIpqcTabletitleJianyanBinding;
import youdian.apk.ipqc.obsever.InsCheckResultObserver;
import youdian.apk.ipqc.presenter.NewXunjianPresenter;
import youdian.apk.ipqc.utils.Constans;

import static youdian.apk.ipqc.utils.Constans.FLAG_LINE;
import static youdian.apk.ipqc.utils.Constans.Frequency;
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

    private String flag;


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
        binding = DataBindingUtil.setContentView(this,getLayoutId());
        Bundle bundle = getIntent().getBundleExtra("param");
        resultObserver = (InsCheckResultObserver) bundle.getSerializable(Inspection);
        binding.setInspection(resultObserver);
        binding.headview.setTitleText(getResources().getString(R.string.biaotouxinxi));
        binding.headview.setLeftIcon(R.mipmap.home_icon_return);
        binding.headview.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public void showCheckTypeBottomDialog(List<OptionData> list) {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog = new BottomSheetDialog(this);
        }
        if (bottomSheetDialog.isShowing())
            bottomSheetDialog.dismiss();
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(NewXunjian_Activity.this).inflate(R.layout.bottom_dialog, null);
        ListView bottom_lv = view.findViewById(R.id.bottom_lv);
        OptionBottomSheetAdapter adapter = new OptionBottomSheetAdapter(list, this);
        bottom_lv.setAdapter(adapter);
        bottom_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (flag.equals(Frequency)){
                    resultObserver.setFrequency(list.get(i).getOption_value());
                    binding.jianyanpinlv.setText(list.get(i).getOption_name());
                }else if (flag.equals(Shift)){
                    resultObserver.setShift(list.get(i).getOption_value());
                    binding.banbie.setText(list.get(i).getOption_name());
                }else{
                    resultObserver.setPeriod(list.get(i).getOption_value());
                    binding.shijianduan.setText(list.get(i).getOption_name());
                }
            }
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    /**
     * 获取准备数据
     */
    public void dealData() {
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
                flag = binding.jianyanpinlv.getText().toString();
                if (!flag.equals("")){
                    mPresenter.getSelectInfo(flag);
                }
            }
        });

        binding.banbie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        if (resultObserver.getWork_no().isEmpty()) {
            binding.gonglingrl.setErrorEnabled(true);
            binding.gonglingrl.setError(getResources().getString(R.string.workno_err));
            return;
        } else {
            binding.gonglingrl.setErrorEnabled(false);
        }
        if (resultObserver.getShift().isEmpty()) {
            binding.liaohao.setErrorEnabled(true);
            binding.liaohao.setError(getResources().getString(R.string.shift_err));
            return;
        } else {
            binding.liaohao.setErrorEnabled(false);
        }
        if (resultObserver.getEdition().isEmpty()) {
            binding.banci.setErrorEnabled(true);
            binding.banci.setError(getResources().getString(R.string.banci_err));
            return;
        } else {
            binding.banci.setErrorEnabled(false);
        }
        if (resultObserver.getProduction_batch().isEmpty()) {
            binding.shengchanpici.setErrorEnabled(true);
            binding.shengchanpici.setError(getResources().getString(R.string.shengchanpici));
            return;
        } else {
            binding.shengchanpici.setErrorEnabled(false);
        }
        if (resultObserver.getLine_code().isEmpty()||resultObserver.getLine_name().isEmpty()) {
            binding.linerl.setErrorEnabled(true);
            binding.linerl.setError(getResources().getString(R.string.line_err));
            return;
        } else {
            binding.linerl.setErrorEnabled(false);
        }
        if (resultObserver.getCheck_quantity().isEmpty()) {
            binding.checkrl.setErrorEnabled(true);
            binding.checkrl.setError(getResources().getString(R.string.checkno_err));
            return;
        } else {
            binding.checkrl.setErrorEnabled(false);
        }
        if (resultObserver.getPart_no().isEmpty()) {
            binding.checkrl.setErrorEnabled(true);
            binding.checkrl.setError(getResources().getString(R.string.partno_err));
            return;
        } else {
            binding.checkrl.setErrorEnabled(false);
        }

        if (resultObserver.getMachine_type().isEmpty()) {
            binding.jizhongrl.setErrorEnabled(true);
            binding.jizhongrl.setError(getResources().getString(R.string.jizhong_err));
            return;
        } else {
            binding.jizhongrl.setErrorEnabled(false);
        }
        //页面跳转
    }

}
