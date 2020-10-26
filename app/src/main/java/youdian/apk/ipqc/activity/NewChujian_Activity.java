package youdian.apk.ipqc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;

import youdian.apk.ipqc.R;
import youdian.apk.ipqc.base.BaseMvpActivity;
import youdian.apk.ipqc.contract.NewChujianContract;
import youdian.apk.ipqc.databinding.ActivityIpqcTabletitleChujianBinding;
import youdian.apk.ipqc.obsever.FirstCheckResultObserver;
import youdian.apk.ipqc.presenter.NewChujianPresenter;
import youdian.apk.ipqc.utils.Constans;


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


    private String title, temp_id;
    private PopupWindow popuList;            //下拉控件
    String sn, classes, work_order, line_code, part_no, edtion, product_quantity, check_quantity, chujian_type, note, machine_type, se_name, se_id, se_no, table_name, table_no, log_type;
    boolean isRecover = false;
    private String table_id;
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
        Bundle bundle = getIntent().getBundleExtra("param");
        resultObserver = (FirstCheckResultObserver) bundle.getSerializable(Constans.FirstCheck);
        binding.setFirstcheck(resultObserver);
        binding.headview.setTitleText(getResources().getString(R.string.biaotouxinxi));
        binding.headview.setLeftIcon(R.mipmap.home_icon_return);
        binding.headview.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
    public void showSfc() {

    }

    @Override
    public void hideSfc() {

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


//
//    private void initView() {
//        title = sp.getString(Constans.IPQC_Table_TITLE_NAME, "");
//        title_text.setText(title);
//        ipqc_chujian_tabletitle_time.setText(MyUtils_P.getTime(this));
//        se_name = sp.getString(Constans.IPQC_SE_NAME, "");
//        se_id = sp.getString(Constans.IPQC_SE_ID, "");
//        table_name = sp.getString(Constans.IPQC_Table_TITLE_NAME, "");
//        table_no = sp.getString(Constans.IPQC_Table_TITLE_NO, "");
//        mpresenter.start();
//        if (isUpdate) {
//            isRecover = true;
//            table_id = sp.getString(Constans.IPQC_TITLE_ID, "");
//            mpresenter.getTableTitle(dbServer, table_id);
//        }
//
//    }
//
//
//    @OnClick({R.id.ipqc_chujian_tabletitle_btn_kaishijianyan, R.id.ipqc_chujian_tabletitle_sn_btn, R.id.ipqc_chujian_tabletitle_chujianleixing})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.ipqc_chujian_tabletitle_sn_btn:
//                //扫描SN
//                startQrCode();
//                break;
//            case R.id.ipqc_chujian_tabletitle_btn_kaishijianyan:
//                //开始巡检
//                String time = MyUtils_P.getTime(this);
////                if (ipqc_chujian_tabletitle_sn_smg.getVisibility() != View.GONE) {
//                work_order = ipqc_chujian_tabletitle_gongling.getText().toString();
//                line_code = ipqc_chujian_tabletitle_xianbie.getText().toString();
//                part_no = ipqc_chujian_tabletitle_liaohao.getText().toString();
//                edtion = ipqc_chujian_tabletitle_banci.getText().toString();
//                product_quantity = ipqc_chujian_tabletitle_shengchanpici.getText().toString();
////                } else {
////                    work_order = "";
////                    line_code = "";
////                    part_no = "";
////                    edtion = "";
////                    product_quantity = "";
////                }
//                sn = ipqc_chujian_tabletitle_sn_tv.getText().toString();
//                classes = ipqc_chujian_tabletitle_banbie.getText().toString();
//                check_quantity = ipqc_chujian_tabletitle_jianyanshuliang.getText().toString();
//                chujian_type = ipqc_chujian_tabletitle_chujianleixing.getText().toString();
//                note = ipqc_chujian_tabletitle_beizhu.getText().toString();
//                machine_type = ipqc_chujian_tabletitle_jizhong.getText().toString();
//                se_no = MyUtils_IPQC.getSeNo(localData, se_id);
//
//                if (!isUpdate) {
//                    log_type = Constans.IPQC_RESTORE_NONE;
//                    if (!sn.equals("") && !sn.equals("error")) {
//                        int category = sp.getInt(Constans.IPQC_CATEGORY, Constans.IPQC_CHUJIAN);
//                        int table_id = dbServer.save_tabletitle_chujian(new TableaTitle_Chujian(sn, classes, work_order, line_code, part_no, edtion, product_quantity, check_quantity, chujian_type, note, machine_type, se_name, se_no, se_id, table_name, table_no, log_type, category, time), temp_id, isRecover);
//                        sp.edit().putString(Constans.IPQC_TITLE_ID, table_id + "").commit();
//                        Intent intent = new Intent(this, CheckDetail_Chujian_Activity.class);
//                        intent.putExtra(Constans.IPQC_RECOVER_TEMP, isRecover);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        MyUtils.MyToast(this, getResources().getString(R.string.input_sn));
//                    }
//                }else{
//                    int category = sp.getInt(Constans.IPQC_CATEGORY, Constans.IPQC_CHUJIAN);
//                    dbServer.save_tabletitle_chujian(new TableaTitle_Chujian(sn, classes, work_order, line_code, part_no, edtion, product_quantity, check_quantity, chujian_type, note, machine_type, se_name, se_no, se_id, table_name, table_no, tableaTitle_chujian.getLog_type(), category, time), table_id, isRecover);
////                    Intent intent = new Intent(this, CheckDetail_Chujian_Activity.class);
////                    sp.edit().putString(Constans.IPQC_TITLE_ID, table_id).commit();
//
////                    intent.putExtra(Constans.IPQC_RECOVER_TEMP, isRecover);
////                    startActivity(intent);
////                    finish();
//                    setResult(Constans.RSP_UPDATE_OK);
//                    finish();
//
//                }
//                break;
//            case R.id.ipqc_chujian_tabletitle_chujianleixing:
//                // 点击控件后显示popup窗口
//                initSelectPopup(ipqc_chujian_tabletitle_chujianleixing, getResources().getStringArray(R.array.chujian_category));
//                // 使用isShowing()检查popup窗口是否在显示状态
//                if (popuList != null && !popuList.isShowing()) {
//                    popuList.showAsDropDown(ipqc_chujian_tabletitle_chujianleixing, 0, 10);
//                }
//        }
//
//    }
//
//    // 开始扫码
//    private void startQrCode() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            // 申请权限
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Constans.REQ_PERM_CAMERA);
//            return;
//        }
//        // 二维码扫码
//        Intent intent = new Intent(this, Inventory_QRCodeScanActivity.class);
//        startActivityForResult(intent, Constans.IPQC_RQ_CHUJIAN_SN);
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case Constans.REQ_PERM_CAMERA:
//                // 摄像头权限申请
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // 获得授权
//                    startQrCode();
//                } else {
//                    // 被禁止授权
//                    Toast.makeText(this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
//                }
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //二维码扫描结果回调
//        if (requestCode == Constans.IPQC_RQ_CHUJIAN_SN && resultCode == RESULT_OK) {
//            Bundle bundle = data.getExtras();
//            String scanResult = bundle.getString(Constans.INTENT_EXTRA_KEY_QR_SCAN);
//            ipqc_chujian_tabletitle_sn_tv.setText(scanResult);
//            sp.edit().putString(Constans.IPQC_SN, scanResult).commit();
//            int category = sp.getInt(Constans.IPQC_CATEGORY, Constans.IPQC_CHUJIAN);
//            mpresenter.handlerSN();
//            String time = MyUtils_P.getTime(this);
//            temp_id = dbServer.getTableId_temp(scanResult, table_no, time, category);
//            if (temp_id != null) {
//                CommonDialog dialog = null;
//                if (dialog == null) {
//                    dialog = new CommonDialog(this, getDrawable(R.mipmap.mine_icon_wdzl),
//                            getString(R.string.tip_tempstore), getString(R.string.recover), getString(R.string.cancel), new CommonDialog.DialogClickListener() {
//                        @Override
//                        public void onDialogClick() {
//                            //恢复暂存
//                            TableaTitle_Chujian tableaTitle_chujian = dbServer.get_tabletitle(temp_id);
//                            ipqc_chujian_tabletitle_sn_tv.setText(tableaTitle_chujian.getSn());
//                            ipqc_chujian_tabletitle_banbie.setText(tableaTitle_chujian.getClasses());
//                            ipqc_chujian_tabletitle_jianyanshuliang.setText(tableaTitle_chujian.getCheck_quantity());
//                            ipqc_chujian_tabletitle_chujianleixing.setText(tableaTitle_chujian.getChujian_type());
//                            ipqc_chujian_tabletitle_beizhu.setText(tableaTitle_chujian.getNote());
//                            ipqc_chujian_tabletitle_jizhong.setText(tableaTitle_chujian.getMachine_type());
//                            ipqc_chujian_tabletitle_gongling.setText(tableaTitle_chujian.getWork_order());
//                            ipqc_chujian_tabletitle_xianbie.setText(tableaTitle_chujian.getLine_code());
//                            ipqc_chujian_tabletitle_liaohao.setText(tableaTitle_chujian.getPart_no());
//                            ipqc_chujian_tabletitle_banci.setText(tableaTitle_chujian.getEdtion());
//                            ipqc_chujian_tabletitle_shengchanpici.setText(tableaTitle_chujian.getProduct_quantity());
//                            ipqc_chujian_tabletitle_sn_btn.setClickable(false);
//                            ipqc_chujian_tabletitle_sn_btn.setEnabled(false);
//                            isRecover = true;
//                            return;
//                        }
//                    }, new CommonDialog.DialogClickListener() {
//                        @Override
//                        public void onDialogClick() {
//                            //删除暂存记录，不恢复
//                            isRecover = false;
//                            dbServer.delete_chujian_temp(temp_id);
//                        }
//                    });
//                    dialog.setCanceledOnTouchOutside(false);
//                }
//
//                dialog.show();
//
//            }
//
//        } else {
//            ipqc_chujian_tabletitle_sn_tv.setText("error");
//
//        }
//    }
//
//    /**
//     * 点检动作下拉选择弹窗
//     */
//    private void initSelectPopup(final TextView tv, final String[] reference_value) {
//        View contentView = LayoutInflater.from(this).inflate(R.layout.populist_style, null);
//        popuList = new PopupWindow(contentView, tv.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        popuList.setContentView(contentView);
////        popuList = new PopupWindow(mTypeLv, tv.getWidth(), ActionBar.LayoutParams.WRAP_CONTENT, true);
//        popuList.setElevation(30);
//        popuList.setBackgroundDrawable(new BitmapDrawable());
//        popuList.setFocusable(true);
//        popuList.setOutsideTouchable(true);
//        ListView mTypeLv = contentView.findViewById(R.id.populist_lv);
//        // 设置适配器
//        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, reference_value);
//        mTypeLv.setAdapter(adapter);
//
//        // 设置ListView点击事件监听
//        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // 在这里获取item数据
//                String value = getResources().getStringArray(R.array.chujian_category)[position];
//                // 把选择的数据展示对应的TextView上
//                tv.setText(value);
//                // 选择完后关闭popup窗口
//                popuList.dismiss();
//            }
//        });
//
//        popuList.setOnDismissListener(new PopupWindow.OnDismissListener()
//
//        {
//            @Override
//            public void onDismiss() {
//                // 关闭popup窗口
//                popuList.dismiss();
//            }
//        });
//    }
//
//    @Override
//    public void showLoading() {
//        waitDialog.show();
//    }
//
//    @Override
//    public void hideLoading() {
//        waitDialog.dismiss();
//    }
//
//
//    @Override
//    public void hideDialog() {
//
//    }
//
//    @Override
//    public boolean isShowingDialog() {
//        return false;
//    }
//
//    @Override
//    public void showSfc() {
//        ipqc_chujian_tabletitle_sn_smg.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void hideSfc() {
//        ipqc_chujian_tabletitle_sn_smg.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void showDialog(Drawable drawable, String contentmsg, String confirm_text, String table_id) {
//
//    }
//
//    @Override
//    public void failed() {
//
//    }
//
//    @Override
//    public void recoverTable(TableaTitle_Chujian tableaTitle_chujian) {
//        this.tableaTitle_chujian = tableaTitle_chujian;
//        ipqc_chujian_tabletitle_sn_tv.setText(tableaTitle_chujian.getSn());
//        ipqc_chujian_tabletitle_banbie.setText(tableaTitle_chujian.getClasses());
//        ipqc_chujian_tabletitle_jianyanshuliang.setText(tableaTitle_chujian.getCheck_quantity());
//        ipqc_chujian_tabletitle_chujianleixing.setText(tableaTitle_chujian.getChujian_type());
//        ipqc_chujian_tabletitle_beizhu.setText(tableaTitle_chujian.getNote());
//        ipqc_chujian_tabletitle_jizhong.setText(tableaTitle_chujian.getMachine_type());
//        ipqc_chujian_tabletitle_gongling.setText(tableaTitle_chujian.getWork_order());
//        ipqc_chujian_tabletitle_xianbie.setText(tableaTitle_chujian.getLine_code());
//        ipqc_chujian_tabletitle_liaohao.setText(tableaTitle_chujian.getPart_no());
//        ipqc_chujian_tabletitle_banci.setText(tableaTitle_chujian.getEdtion());
//        ipqc_chujian_tabletitle_shengchanpici.setText(tableaTitle_chujian.getProduct_quantity());
//        ipqc_chujian_tabletitle_sn_btn.setClickable(false);
//        ipqc_chujian_tabletitle_sn_btn.setEnabled(false);
//        isRecover = true;
//    }
//
//    @Override
//    public void setPresenter(NewChujianContract.Presenter presenter) {
//        mpresenter = presenter;
//    }
}
