package ykk.xc.com.wms.warehouse;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.tscdll.TSCActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.List;
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
import ykk.xc.com.wms.comm.Comm;
import ykk.xc.com.wms.comm.UncaughtException;
import ykk.xc.com.wms.model.mtl;
import ykk.xc.com.wms.util.JsonUtil;
import ykk.xc.com.wms.util.LoadingDialog;
import ykk.xc.com.wms.warehouse.adapter.Ware_Makter_CodeAdapter;

public class Ware_Makter_CodeActivity extends BaseActivity {

    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.tv_selectType)
    TextView tvSelectType;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Ware_Makter_CodeActivity context = this;
    private static final int SUCC1 = 200, UNSUCC1 = 501;
    private TSCActivity printUtils = new TSCActivity();
    private BluetoothAdapter mBluetoothAdapter;
    private AlertDialog alertDialog; // 已配对蓝牙列表dialog
    private boolean isConnected; // 判断是否连接蓝牙设备
    private OkHttpClient okHttpClient = new OkHttpClient();
    private FormBody formBody = null;
    private char selectType = '1'; // 记录打印的是那个表
    private LoadingDialog mLoadDialog;
    private List<mtl> list;
    private Ware_Makter_CodeAdapter mAdapter;

    // 消息处理
    final MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<Ware_Makter_CodeActivity> mActivity;

        public MyHandler(Ware_Makter_CodeActivity activity) {
            mActivity = new WeakReference<Ware_Makter_CodeActivity>(activity);
        }

        public void handleMessage(Message msg) {
            Ware_Makter_CodeActivity m = mActivity.get();
            if (m != null) {
                if(m.mLoadDialog != null && m.mLoadDialog.isShowing()) {
                    m.mLoadDialog.dismiss();
                    m.mLoadDialog = null;
                }
                switch (msg.what) {
                    case SUCC1: // 成功
                        m.list = JsonUtil.stringToList((String) msg.obj, mtl.class);
                        m.updateUI();

                        break;
                    case UNSUCC1: // 数据加载失败！
                        m.print_fun(m, "抱歉，没有加载到数据！");

                        break;
                }
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
        registerReceiver(mReceiver, makeFilters());

    }

    @OnClick({R.id.btn_close, R.id.tv_selectType, R.id.btn_search})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_close: // 关闭
                unregisterReceiver(mReceiver);
                if (mBluetoothAdapter != null) {
                    printUtils.closeport(); // 关闭打印端口
                }
                closeHandler(mHandler);
                context.finish();

                break;
            case R.id.tv_selectType: // 选择打印的表
                pop_selectType(v);
                popWindow.showAsDropDown(v);

                break;
            case R.id.btn_search:
                okhttpGetDatas();
                /*
                //获取蓝牙适配器实例。如果设备不支持蓝牙则返回null
                if (mBluetoothAdapter == null) {
                    print_fun(context, "设备不支持蓝牙！");
                    return;
                }
                // 判断蓝牙是否开启
                if (!mBluetoothAdapter.isEnabled()) {
                    // 蓝牙未开启，打开蓝牙
                    openBluetooth();
                    return;
                }
                // 判断状态为连接
                if (isConnected) {
                    printContent();
                } else {
                    pair();
                }
                */

                break;
        }
    }

    /**
     * 创建PopupWindow 【 查询来源类型 】
     */
    private PopupWindow popWindow;

    @SuppressWarnings("deprecation")
    private void pop_selectType(View v) {
        if (null != popWindow) {//不为空就隐藏
            popWindow.dismiss();
            return;
        }
        // 获取自定义布局文件popupwindow_left.xml的视图
        final View popV = getLayoutInflater().inflate(R.layout.wh_maker_code_tablename, null);
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popWindow = new PopupWindow(popV, v.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        // popWindow.setAnimationStyle(R.style.AnimationFade);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOutsideTouchable(true);
        popWindow.setFocusable(true);

        // 点击其他地方消失
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmpId = 0;
                switch (v.getId()) {
                    case R.id.btn1:// 物料表
                        tmpId = v.getId();
                        selectType = '1';

                        break;
                    case R.id.btn2:// 仓库表
                        tmpId = v.getId();
                        selectType = '2';

                        break;

                    case R.id.btn3:// 库区表
                        tmpId = v.getId();
                        selectType = '3';

                        break;
                    case R.id.btn4:// 库区表
                        tmpId = v.getId();
                        selectType = '4';

                        break;
                    case R.id.btn5:// 部门表
                        tmpId = v.getId();
                        selectType = '5';

                        break;
                }
                popWindow.dismiss();
                tvSelectType.setText(getValues((Button) popV.findViewById(tmpId)) + "-生成条码");
            }
        };
        popV.findViewById(R.id.btn1).setOnClickListener(click);
        popV.findViewById(R.id.btn2).setOnClickListener(click);
        popV.findViewById(R.id.btn3).setOnClickListener(click);
        popV.findViewById(R.id.btn4).setOnClickListener(click);
        popV.findViewById(R.id.btn5).setOnClickListener(click);
    }

    /**
     * 打开蓝牙
     */
    private void openBluetooth() {
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
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
        if (lin_oklist.getChildCount() > 0) { // 每次都清空子View
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
                String str = blueDevice.getName() + " : (" + blueDevice.getAddress() + ")";
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
                String address = item.substring(item.indexOf("(") + 1, item.indexOf(")"));
                printUtils.openport(address);

                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        });
        ViewGroup parent = (ViewGroup) tv_item.getParent();
        if (parent != null) {
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
        printUtils.barcode(25, 178, "39", 40, 1, 0, 1, 3, "123456789");
        printUtils.printlabel(1, 1);
    }

    /**
     * 通过okhttp加载数据
     * 供应商信息
     */
    private void okhttpGetDatas() {
        mLoadDialog = new LoadingDialog(context, "加载中", true);
        String mUrl = Comm.getURL("mtl");
        Request.Builder requestBuilder = new Request.Builder().url(mUrl);
        requestBuilder.method("GET", null);

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(UNSUCC1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String result = body.string();
                Message msg = mHandler.obtainMessage(SUCC1, result);
                Log.e("Ware_Makter_CodeActivity --> onResponse", result);
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 更新UI
     */
    private void updateUI() {
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new Ware_Makter_CodeAdapter(context, list);
        recyclerView.setAdapter(mAdapter);

//        mAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int pos) {
//                mtl m = list.get(pos);
//            }
//        });
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
    private IntentFilter makeFilters() {
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
