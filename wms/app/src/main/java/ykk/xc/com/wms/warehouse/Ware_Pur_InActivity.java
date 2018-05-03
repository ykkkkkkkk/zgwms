package ykk.xc.com.wms.warehouse;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ykk.xc.com.wms.R;
import ykk.xc.com.wms.comm.BaseActivity;
import ykk.xc.com.wms.model.t_Supplier;

public class Ware_Pur_InActivity extends BaseActivity {

    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.btn_maker_code)
    Button btnMakerCode;
    @BindView(R.id.et_custSel)
    EditText etCustSel;
    @BindView(R.id.btn_custSel)
    Button btnCustSel;
    @BindView(R.id.et_sourceNo)
    EditText etSourceNo;
    @BindView(R.id.et_whName)
    EditText etWhName;
    @BindView(R.id.btn_whName)
    Button btnWhName;
    @BindView(R.id.et_whArea)
    EditText etWhArea;
    @BindView(R.id.btn_whArea)
    Button btnWhArea;
    @BindView(R.id.et_whPos)
    EditText etWhPos;
    @BindView(R.id.btn_whPos)
    Button btnWhPos;
    @BindView(R.id.et_deptName)
    EditText etDeptName;
    @BindView(R.id.btn_deptName)
    Button btnDeptName;
    @BindView(R.id.et_matNo)
    EditText etMatNo;
    @BindView(R.id.tv_matName)
    TextView tvMatName;
    @BindView(R.id.tv_tupe)
    TextView tvTupe;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.et_batchNo)
    EditText etBatchNo;
    @BindView(R.id.btn_add)
    Button btnAdd;
    private Ware_Pur_InActivity context = this;
    private static final int SEL_CUST = 10;
    private t_Supplier supplier; // 供应商

    private OkHttpClient okHttpClient = new OkHttpClient();
    private FormBody formBody = null;

    // 消息处理
    private MyHandler mHandler = new MyHandler(this);
    private static class MyHandler extends Handler {
        private final WeakReference<Ware_Pur_InActivity> mActivity;

        public MyHandler(Ware_Pur_InActivity activity) {
            mActivity = new WeakReference<Ware_Pur_InActivity>(activity);
        }

        public void handleMessage(Message msg) {
            Ware_Pur_InActivity activity = mActivity.get();
            if (activity != null) {

            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.wh_pur_in);
        ButterKnife.bind(this);

        initDatas();
    }

    private void initDatas() {

    }


    @OnClick({R.id.btn_close, R.id.btn_maker_code, R.id.btn_custSel, R.id.btn_whName, R.id.btn_whArea, R.id.btn_whPos, R.id.btn_deptName, R.id.btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close: // 关闭
                closeHandler(mHandler);
                context.finish();

                break;
            case R.id.btn_maker_code: // 打印条码界面
                show(context, Ware_Makter_CodeActivity.class, null);

                break;
            case R.id.btn_custSel:
                showForResult(context, Cust_DialogActivity.class, SEL_CUST, null);

                break;
            case R.id.btn_whName:
                showForResult(context, Cust_DialogActivity.class, SEL_CUST, null);

                break;
            case R.id.btn_whArea:
                showForResult(context, Cust_DialogActivity.class, SEL_CUST, null);

                break;
            case R.id.btn_whPos:
                showForResult(context, Cust_DialogActivity.class, SEL_CUST, null);

                break;
            case R.id.btn_deptName:
                showForResult(context, Cust_DialogActivity.class, SEL_CUST, null);

                break;
            case R.id.btn_add:

                break;
        }
    }



    private void okhttpGet() {
        // step 2： 创建一个请求，不指定请求方法时默认是GET。
        Request.Builder requestBuilder = new Request.Builder().url("http://183.62.46.37:8008/api/mtl");
        //可以省略，默认是GET请求
        requestBuilder.method("GET", null);

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                Log.e("--------", "请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                //获得返回体
                ResponseBody body = response.body();
                Log.e("MMMMMMMMMMMMM--------", body.string());
            }
        });
    }

    private void okhttpPost() {
        formBody = new FormBody.Builder()
                .add("name", "dsd")
                .build();
        Request request = new Request.Builder().url("http://183.62.46.37:8008/api/t_Supplier")
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                Log.e("--------", "请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                Log.e("MMMMMMMMMMMMM--------", response.body().string());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SEL_CUST: //查询供应商	返回
                if (resultCode == RESULT_OK) {
                    supplier = data.getParcelableExtra("obj");
                    Log.e("onActivityResult---", supplier.getFname());
                    if (supplier != null) {
                        setTexts(etCustSel, supplier.getFname());
                    }
                }

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeHandler(mHandler);
            context.finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
       closeHandler(mHandler);
        super.onDestroy();
    }
}
