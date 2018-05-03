package ykk.xc.com.wms.warehouse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

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
import ykk.xc.com.wms.comm.OnItemClickListener;
import ykk.xc.com.wms.comm.UncaughtException;
import ykk.xc.com.wms.model.t_Supplier;
import ykk.xc.com.wms.util.JsonUtil;
import ykk.xc.com.wms.util.LoadingDialog;
import ykk.xc.com.wms.warehouse.adapter.Cust_DialogAdapter;

/**
 * 选择供应商dialog
 */
public class Cust_DialogActivity extends BaseActivity {

    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private Cust_DialogActivity context = this;
    private static final int SUCC1 = 200, UNSUCC1 = 501;
    private List<t_Supplier> list;
    private Cust_DialogAdapter mAdapter;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private FormBody formBody = null;
    private LoadingDialog mLoadDialog;

    // 消息处理
    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<Cust_DialogActivity> mActivity;

        public MyHandler(Cust_DialogActivity activity) {
            mActivity = new WeakReference<Cust_DialogActivity>(activity);
        }

        public void handleMessage(Message msg) {
            Cust_DialogActivity m = mActivity.get();
            if (m != null) {
                if(m.mLoadDialog != null && m.mLoadDialog.isShowing()) {
                    m.mLoadDialog.dismiss();
                    m.mLoadDialog = null;
                }
                switch (msg.what) {
                    case SUCC1: // 成功
                        m.list = JsonUtil.stringToList((String) msg.obj, t_Supplier.class);
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
        setContentView(R.layout.wh_cust_dialog);
        ButterKnife.bind(this);
        UncaughtException.getInstance().setContext(context);

        initDatas();
        okhttpGetDatas();
    }

    private void initDatas() {

    }

    // 监听事件
    @OnClick(R.id.btn_close)
    public void onViewClicked() {
        closeHandler(mHandler);
        context.finish();
    }

    /**
     * 通过okhttp加载数据
     * 供应商信息
     */
    private void okhttpGetDatas() {
        mLoadDialog = new LoadingDialog(context, "加载中", true);
        String mUrl = Comm.getURL("t_Supplier");
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
                Message msg = mHandler.obtainMessage(SUCC1, body.string());
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
        mAdapter = new Cust_DialogAdapter(context, list);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                t_Supplier supplier = list.get(pos);
                Intent intent = new Intent();
                intent.putExtra("obj", supplier);
                context.setResult(RESULT_OK, intent);
                context.finish();
            }
        });
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
