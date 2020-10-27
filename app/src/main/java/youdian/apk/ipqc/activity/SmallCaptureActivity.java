package youdian.apk.ipqc.activity;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import youdian.apk.ipqc.R;


/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2020/5/9 0009
 * Time: 下午 1:26
 * Function:
 */
public class SmallCaptureActivity extends CaptureActivity {
    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.capture_small);
        return (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
    }
}
