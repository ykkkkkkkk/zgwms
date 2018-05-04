package ykk.xc.com.wms.warehouse;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tscdll.TSCActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.Set;

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
import ykk.xc.com.wms.comm.UncaughtException;

public class Ware_Makter_CodeActivity extends BaseActivity {

    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;

    private Ware_Makter_CodeActivity context = this;
    private TSCActivity printUtils = new TSCActivity();
    private BluetoothAdapter mBluetoothAdapter;
    private AlertDialog alertDialog; // 已配对蓝牙列表dialog
    private boolean isConnected; // 判断是否连接蓝牙设备
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wh_maker_code);
        ButterKnife.bind(this);
        UncaughtException.getInstance().setContext(context);

        initDatas();
    }

    private void initDatas() {
        // 获取所有已经绑定的蓝牙设备
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        registerReceiver(mReceiver , makeFilters());

    }

    @OnClick({R.id.btn_close, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                unregisterReceiver(mReceiver);
                printUtils.closeport(); // 关闭打印端口
                closeHandler(mHandler);
                context.finish();

                break;
            case R.id.btn_search:
                //获取蓝牙适配器实例。如果设备不支持蓝牙则返回null
                if(mBluetoothAdapter == null) {
                    print_fun(context, "设备不支持蓝牙！");
                    return;
                }
                // 判断蓝牙是否开启
                if(!mBluetoothAdapter.isEnabled()) {
                    // 蓝牙未开启，打开蓝牙
                    openBluetooth();
                    return;
                }
                // 判断状态为连接
                if(isConnected) {
                    printContent();
                } else {
                    pair();
                }

                break;
        }
    }

    /**
     * 打开蓝牙
     */
    private void openBluetooth() {
        BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mAdapter.isEnabled()) {
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enabler);
        }
    }

    /**
     * 得到已配对的列表进行配对
     */
    private void pair() {
        alertDialog = null;
        View v = context.getLayoutInflater().inflate(R.layout.bluetooth_oklist, null);
        alertDialog = new AlertDialog.Builder(context).setView(v).create();
        // 初始化id
        Button btn_close = (Button) v.findViewById(R.id.btn_close);
        LinearLayout lin_oklist = v.findViewById(R.id.lin_oklist);
        if(lin_oklist.getChildCount() > 0) { // 每次都清空子View
            lin_oklist.removeAllViews();
        }

        // 单击事件
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                switch (v.getId()) {
                    case R.id.btn_close: // 关闭
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                        break;
                }
            }
        };
        btn_close.setOnClickListener(click);
        // 得到已配对的蓝牙设备
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        if (devices.size() > 0) {
            for (BluetoothDevice blueDevice : devices) {
                String str = blueDevice.getName()+" : ("+blueDevice.getAddress()+")";
                addView(lin_oklist, str); // 添加到布局
            }

            Window window = alertDialog.getWindow();
            alertDialog.setCancelable(false);
            alertDialog.show();
            window.setGravity(Gravity.CENTER);
        } else {
            print_fun(context, "请先配对蓝牙打印机！");
        }
    }

    /**
     * 添加到LinearLayout
     */
    private void addView(LinearLayout lin, String twoMenuName) {
        View v = LayoutInflater.from(context).inflate(R.layout.bluetooth_oklist_item, null);
        final TextView tv_item = (TextView) v.findViewById(R.id.tv_item);
        tv_item.setText(twoMenuName);
        // 设置单击事件
        tv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击就配对，然后关闭这个dialog
                String item = getValues(tv_item);
                // 截取的格式为：名称:(20:20:20:20:20),只截取括号里的
                String address = item.substring(item.indexOf("(")+1, item.indexOf(")"));
                printUtils.openport(address);

                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        });
        ViewGroup parent = (ViewGroup)tv_item.getParent();
        if(parent != null) {
            parent.removeAllViews();
        }
        // 添加到容器中
        lin.addView(tv_item);
    }

    /**
     * 打印条码的方法
     */
    private void printContent() {
        printUtils.setup(40, 30, 4, 5, 0, 2, 0);
        printUtils.clearbuffer();
        String s = "TEXT 90,5,\"TSS24.BF2\",0,1,2,\"人工牙种植体 \n" +
                   "TEXT 20,50,\"TSS24.BF2\",0,1,1,\"生产厂商:ykk \n" +
                   "TEXT 20,75,\"TSS24.BF2\",0,1,1,\"型——号:38*10mm \n" +
                   "TEXT 20,100,\"TSS24.BF2\",0,1,1,\"生产日期:2018-05-03 \n" +
                   "TEXT 20,125,\"TSS24.BF2\",0,1,1,\"失效日期:2020-05-03 \n" +
                   "TEXT 20,150,\"TSS24.BF2\",0,1,1,\"批——号:201805003 \n";
        byte b[] = new byte[0];
        try {
            b = s.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        printUtils.sendcommand(b); // 打印字体
        // 条码和字体的行间距：28
        printUtils.barcode(25,178,"39",40,1,0,1,3,"123456789");
        printUtils.printlabel(1, 1);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            unregisterReceiver(mReceiver);
            printUtils.closeport(); // 关闭打印端口
            closeHandler(mHandler);
            context.finish();
        }
        return false;
    }

    /**
     * 广播监听蓝牙状态
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context2, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothDevice.ACTION_ACL_CONNECTED: // 已连接
                    print_fun(context, "已连接蓝牙设备√");
                    isConnected = true;

                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:// 断开连接
                    //蓝牙连接被切断
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String name = device.getName();
                    print_fun(context, name + "的连接已断开！");
                    isConnected = false;

                    break;
            }
        }
    };

    /**
     * 蓝牙监听需要添加的Action
     */
    private IntentFilter makeFilters(){
        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.openBluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
//        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//        intentFilter.addAction("android.openBluetooth.BluetoothAdapter.STATE_OFF");
//        intentFilter.addAction("android.openBluetooth.BluetoothAdapter.STATE_ON");
        return intentFilter;
    }

}
