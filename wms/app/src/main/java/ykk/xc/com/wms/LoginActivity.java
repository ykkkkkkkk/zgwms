package ykk.xc.com.wms;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

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
import ykk.xc.com.wms.comm.BaseActivity;
import ykk.xc.com.wms.comm.Comm;
import ykk.xc.com.wms.warehouse.Ware_Pur_InActivity;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_userName)
    EditText etUserName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private LoginActivity context = this;

    private OkHttpClient okHttpClient = new OkHttpClient();
    private FormBody formBody = null;


    // 消息处理
    final MyHandler mHandler = new MyHandler(this);
    private static class MyHandler extends Handler {
        private final WeakReference<LoginActivity> mActivity;
        public MyHandler(LoginActivity activity) {
            mActivity = new WeakReference<LoginActivity>(activity);
        }
        public void handleMessage(android.os.Message msg) {
            LoginActivity m = mActivity.get();
            if(m!=null){

            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.login);
        ButterKnife.bind(this);

        initDatas();
    }

    private void initDatas() {
        Comm.setIp("183.62.46.37");
        Comm.setPort("8008");


    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        show(context, Ware_Pur_InActivity.class, null);

    }

//    @OnClick({R.id.btn1, R.id.btn2})
//    public void onViewClicked(View view) {
//        Intent intent = null;
//        switch (view.getId()) {
//            case R.id.btn1:
////                okhttpGet();
//                intent = new Intent(this, LoadMoreActivity.class);
//
//                break;
//            case R.id.btn2:
//                intent = new Intent(this, LoadMoreWrapperActivity.class);
//
//                break;
//        }
//
//        if (intent != null) {
//            startActivity(intent);
//        }
//    }

    private void okhttpGet() {
        // step 2： 创建一个请求，不指定请求方法时默认是GET。
        Request.Builder requestBuilder = new Request.Builder().url("http://japi.juhe.cn/joke/content/text.from?key=ae240f7fba620fc370b803566654949e&page=1&pagesize=10");
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
    protected void onDestroy() {
        closeHandler(mHandler);
        super.onDestroy();
    }
}
