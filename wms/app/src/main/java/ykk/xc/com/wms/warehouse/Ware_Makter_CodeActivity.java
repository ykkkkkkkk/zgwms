package ykk.xc.com.wms.warehouse;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

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

public class Ware_Makter_CodeActivity extends BaseActivity  implements Runnable{


    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;

    private Ware_Makter_CodeActivity context = this;
    private TSCActivity printUtils = new TSCActivity();
    private BluetoothAdapter mBluetoothAdapter;
    private AlertDialog alertDialog;
    private boolean isConnect; // 蓝牙是否连接

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
        UncaughtException.getInstance().setContext(context);

        initDatas();
    }

    private void initDatas() {
        bluetooth(); // 跳到打开蓝牙设置
        // 获取所有已经绑定的蓝牙设备
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    @OnClick({R.id.btn_close, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                printUtils.closeport(); // 关闭打印端口
                closeHandler(mHandler);
                context.finish();

                break;
            case R.id.btn_search:
                if(isConnect) {
                    printContent();
                } else {
                    pair();
                }

                break;
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
                addLinearLayout(lin_oklist, str); // 添加到布局
            }

            Window window = alertDialog.getWindow();
            alertDialog.setCancelable(false);
            alertDialog.show();
            window.setGravity(Gravity.CENTER);
        } else {
            print_fun(context, "请先配对蓝牙打印机！");
        }

//        Window window = alertDialog.getWindow();
//        alertDialog.setCancelable(false);
//        alertDialog.show();
//        window.setGravity(Gravity.CENTER);
    }

    /**
     * 添加到LinearLayout
     */
    private void addLinearLayout(LinearLayout lin, String twoMenuName) {
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
                isConnect = true;
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
        printUtils.setup(40, 30, 4, 4, 0, 2, 0);
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
        printUtils.barcode(25,178,"39",50,0,0,1,3,"123456789");
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


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            printUtils.closeport(); // 关闭打印端口
//            closeHandler(mHandler);
//            context.finish();
//        }
//        return false;
//    }

    @Override
    protected void onDestroy() {
        closeHandler(mHandler);
        super.onDestroy();
    }
}
