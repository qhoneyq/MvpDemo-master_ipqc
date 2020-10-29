package youdian.apk.ipqc.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

import youdian.apk.ipqc.R;
import youdian.apk.ipqc.adapter.BottomSheetAdapter;
import youdian.apk.ipqc.adapter.OptionBottomSheetAdapter;
import youdian.apk.ipqc.base.BaseMvpActivity;
import youdian.apk.ipqc.bean.Lines;
import youdian.apk.ipqc.bean.OptionData;
import youdian.apk.ipqc.contract.NewChujianContract;
import youdian.apk.ipqc.databinding.ActivityIpqcTabletitleChujianBinding;
import youdian.apk.ipqc.obsever.FirstCheckResultObserver;
import youdian.apk.ipqc.presenter.NewChujianPresenter;
import youdian.apk.ipqc.utils.Constans;

import static youdian.apk.ipqc.utils.Constans.FLAG_LINE;
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

    private List<String> shiftList;


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
        binding = DataBindingUtil.setContentView(this,getLayoutId());
        Bundle bundle = getIntent().getBundleExtra("param");
        resultObserver = (FirstCheckResultObserver) bundle.getSerializable(FirstCheck);
        binding.setFirstcheck(resultObserver);
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
        View view = LayoutInflater.from(NewChujian_Activity.this).inflate(R.layout.bottom_dialog, null);
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

    /**
     * 初见类型下拉
     * @param list
     */
    @Override
    public void showCheckTypeBottomDialog(List<OptionData> list) {
        if (checktypedialog == null) {
            checktypedialog = new BottomSheetDialog(this);
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
            }
        });

        checktypedialog.setContentView(view);
        checktypedialog.show();
    }

    /**
     * 显示白晚班下拉选项
     */
    public void showShiftBottomDialog() {
        if (dialogshift == null) {
            dialogshift = new BottomSheetDialog(this);
            dialogshift = new BottomSheetDialog(this);
        }
        if (dialogshift.isShowing())
            dialogshift.dismiss();

        dialogshift.setCancelable(false);
        dialogshift.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(NewChujian_Activity.this).inflate(R.layout.bottom_dialog, null);
        ListView bottom_lv = view.findViewById(R.id.bottom_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                NewChujian_Activity.this, R.layout.bottom_lv_item, shiftList);
        bottom_lv.setAdapter(adapter);
        bottom_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                resultObserver.setShift_name(shiftList.get(i));
            }
        });

        dialogshift.setContentView(view);
        dialogshift.show();
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

        //CHECKTYPE
        mPresenter.getSelectInfo("CHUJIAN_TYPE");
        binding.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getSelectInfo(Constans.CHUJIAN_TYPE);
            }
        });

        //SHIFT
        shiftList = new ArrayList<>();
        shiftList.add("白班");
        shiftList.add("晚班");
        binding.banbie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShiftBottomDialog();
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
            binding.snTl.setErrorEnabled(true);
            binding.snTl.setError(getResources().getString(R.string.sn_err));
            return;
        } else {
            binding.snTl.setErrorEnabled(false);
        }
        if (resultObserver.getShift().isEmpty()) {
            binding.shiftrl.setErrorEnabled(true);
            binding.shiftrl.setError(getResources().getString(R.string.shift_err));
            return;
        } else {
            binding.shiftrl.setErrorEnabled(false);
        }
        if (resultObserver.getWork_no().isEmpty()) {
            binding.gonglingrl.setErrorEnabled(true);
            binding.gonglingrl.setError(getResources().getString(R.string.workno_err));
            return;
        } else {
            binding.gonglingrl.setErrorEnabled(false);
        }
        if (resultObserver.getWork_no().isEmpty()) {
            binding.liaohaotl.setErrorEnabled(true);
            binding.liaohaotl.setError(getResources().getString(R.string.workno_err));
            return;
        } else {
            binding.liaohaotl.setErrorEnabled(false);
        }
        if (resultObserver.getEdition().isEmpty()) {
            binding.bancitl.setErrorEnabled(true);
            binding.bancitl.setError(getResources().getString(R.string.banci_err));
            return;
        } else {
            binding.bancitl.setErrorEnabled(false);
        }
        if (resultObserver.getProduction_batch().isEmpty()) {
            binding.shengchanpicitl.setErrorEnabled(true);
            binding.shengchanpicitl.setError(getResources().getString(R.string.banci_err));
            return;
        } else {
            binding.shengchanpicitl.setErrorEnabled(false);
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
        if (resultObserver.getCheck_quantity().isEmpty()) {
            binding.checkrl.setErrorEnabled(true);
            binding.checkrl.setError(getResources().getString(R.string.checkno_err));
            return;
        } else {
            binding.checkrl.setErrorEnabled(false);
        }
        if (resultObserver.getChujian_type().isEmpty()) {
            binding.chujiantyperl.setErrorEnabled(true);
            binding.chujiantyperl.setError(getResources().getString(R.string.chujianleixing));
            return;
        } else {
            binding.chujiantyperl.setErrorEnabled(false);
        }
        if (resultObserver.getChujian_type().isEmpty()) {
            binding.chujiantyperl.setErrorEnabled(true);
            binding.chujiantyperl.setError(getResources().getString(R.string.checktype_err));
            return;
        } else {
            binding.chujiantyperl.setErrorEnabled(false);
        }
        if (resultObserver.getMachine_type().isEmpty()) {
            binding.jizhongrl.setErrorEnabled(true);
            binding.jizhongrl.setError(getResources().getString(R.string.jizhong_err));
            return;
        } else {
            binding.jizhongrl.setErrorEnabled(false);
        }
        //页面跳转
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constans.FirstCheck, resultObserver);
        CheckDetail_Chujian_Activity.startActivity(this,bundle);
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
