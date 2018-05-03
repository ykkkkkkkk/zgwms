package ykk.xc.com.wms.warehouse;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tscdll.TSCActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

public class Ware_Makter_CodeActivity extends BaseActivity  implements Runnable{


    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;

    private Ware_Makter_CodeActivity context = this;
    TSCActivity printUtils = new TSCActivity();

    private OkHttpClient okHttpClient = new OkHttpClient();
    private FormBody formBody = null;

    // 消息处理
    final MyHandler mHandler = new MyHandler(this);
    private static class MyHandler extends Handler {
        private final WeakReference<Ware_Makter_CodeActivity> mActivity;

        public MyHandler(Ware_Makter_CodeActivity activity) {
            mActivity = new WeakReference<Ware_Makter_CodeActivity>(activity);
        }

        public void handleMessage(Message msg) {
            Ware_Makter_CodeActivity activity = mActivity.get();
            if (activity != null) {

            }
        }
    }

    //打开蓝牙
    public void bluetooth() {
        BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mAdapter.isEnabled()) {
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enabler);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wh_maker_code);
        ButterKnife.bind(this);

        initDatas();
    }

    private void initDatas() {
        bluetooth(); // 跳到打开蓝牙设置
//        printUtils.openport(new );
    }

    @OnClick({R.id.btn_close, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
//                closeHandler(mHandler);
//                context.finish();
                printContent();

                break;
            case R.id.btn_search:
                run();
//                printContent();
                break;
        }
    }

    private void printContent() {
        printUtils.setup(80, 50, 4, 4, 0, 0, 0);
        printUtils.clearbuffer();
        String s = "TEXT 10,10,\"TSS24.BF2\",0,1,1,\"你好打印机\n";
        byte b[] = new byte[0];
        try {
            b = s.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        printUtils.sendcommand(b);
        printUtils.printlabel(1, 1);
    }

    @Override
    public void run() {
//        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
//        if (devices.size() > 0) {
//            for (final BluetoothDevice bluetoothDevice : devices) {
//                String str = bluetoothDevice.getAddress();
//                int index = 0;
//                //if (str.equals("Gprinter")){}
//
        String str = "DC:0D:30:0D:85:E4";
                final String[] sexArry = {str};//地址列表

                AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("选择你要打开的蓝牙");
                //final String[] finalSexArry = sexArry;
                builder.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 默认的选中

                    @Override
                    public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                        //showToast(which+"");
                        //selectAdress.setText(sexArry[which]);
                        Toast.makeText(context, "已连接", Toast.LENGTH_LONG).show();

                        printUtils.openport(sexArry[which]);

                        dialog.dismiss();//随便点击一个item消失对话框，不用点击确认取消

                    }
                });
                builder.show();// 让弹出框显示
//            }
//
//        }
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
    protected void onDestroy() {
        closeHandler(mHandler);
        super.onDestroy();
    }
}
