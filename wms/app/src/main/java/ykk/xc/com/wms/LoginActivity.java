package ykk.xc.com.wms;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ykk.xc.com.wms.comm.BaseActivity;
import ykk.xc.com.wms.comm.Comm;
import ykk.xc.com.wms.model.barcode_list;
import ykk.xc.com.wms.util.JsonUtil;
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
    private String result;


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

    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        show(context, Ware_Pur_InActivity.class, null);
//        req2();
//        okhttpGet();
//        okhttpPost();
    }

    private void okhttpGet() {
        // step 2： 创建一个请求，不指定请求方法时默认是GET。
        Request.Builder requestBuilder = new Request.Builder().url(Comm.getURL("barcode_list"));
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
                .add("Email", "sample string 1")
                .build();
        barcode_list b = new barcode_list();
        b.setId(11);
        b.setMtl_id(123);
        b.setK3_fitemID(7);
        b.setType(3);
        b.setFnumber("311220011");
        b.setSource_fnumber("1");
        b.setSource_finterID(1);
        b.setSource_fenteryID(5);
        b.setBatch("d");
        String mJson = JsonUtil.objectToString(b);
        RequestBody body = RequestBody.create(Comm.JSON, mJson);

        Request request = new Request.Builder()
                .url(Comm.getURL("barcode_list/PostAdd"))
//                .post(formBody)
                .post(body)
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

    private void req2() {
        //启动后台异步线程进行连接webService操作，并且根据返回结果在主线程中改变UI
//        QueryAddressTask queryAddressTask = new QueryAddressTask();
//        //启动后台任务
//        queryAddressTask.execute("13888888888");

    }


    /**
     * 手机号段归属地查询
     */
    public String getRemoteInfo(String mothod) throws Exception{
        String methodName = "CheckLogin"; // 方法名称
        SoapObject request = new SoapObject(Comm.XMLNS, methodName);
        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        request.addProperty("userName", "1");
        request.addProperty("password", "1");

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(Comm.WEB_URI);
        String soapAction = Comm.XMLNS + methodName;
        httpTransportSE.call(soapAction, envelope);//调用

        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的结果
        result = object.getProperty(0).toString();
        Log.d("getRemoteInfo-----",result);
        return result;

    }

    class QueryAddressTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            // 查询手机号码（段）信息*/
            try {
                result = getRemoteInfo(params[0]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //将结果返回给onPostExecute方法
            return result;
        }

        @Override
        //此方法可以在主线程改变UI
        protected void onPostExecute(String result) {
            // 将WebService返回的结果显示在TextView中
            Log.e("onPostExecute--","result:"+result);
        }
    }

    @Override
    protected void onDestroy() {
        closeHandler(mHandler);
        super.onDestroy();
    }
}
