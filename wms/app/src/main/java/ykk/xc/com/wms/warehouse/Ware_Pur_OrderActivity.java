package ykk.xc.com.wms.warehouse;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
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
import ykk.xc.com.wms.model.PO_list;
import ykk.xc.com.wms.model.mtl;
import ykk.xc.com.wms.model.t_MeasureUnit;
import ykk.xc.com.wms.model.t_Supplier;
import ykk.xc.com.wms.util.JsonUtil;
import ykk.xc.com.wms.util.LoadingDialog;
import ykk.xc.com.wms.util.xrecyclerview.XRecyclerView;
import ykk.xc.com.wms.warehouse.adapter.Ware_Pur_OrderAdapter;

public class Ware_Pur_OrderActivity extends BaseActivity implements XRecyclerView.LoadingListener {

    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.tv_custInfo)
    TextView tvCustInfo;
    @BindView(R.id.cbAll)
    CheckBox cbAll;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private Ware_Pur_OrderActivity context = this;
    private static final int SUCC1 = 200, UNSUCC1 = 500, SUCC2 = 201, UNSUCC2 = 501, SUCC3 = 202, UNSUCC3 = 502;
    private LoadingDialog mLoadDialog;
    private t_Supplier supplier; // 供应商
    private OkHttpClient okHttpClient = new OkHttpClient();
    private FormBody formBody = null;
    private Ware_Pur_OrderAdapter mAdapter;
    private List<PO_list> listDatas;
    private t_MeasureUnit unit;

    // 消息处理
    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<Ware_Pur_OrderActivity> mActivity;

        public MyHandler(Ware_Pur_OrderActivity activity) {
            mActivity = new WeakReference<Ware_Pur_OrderActivity>(activity);
        }

        public void handleMessage(Message msg) {
            Ware_Pur_OrderActivity m = mActivity.get();
            if (m != null) {
                if (m.mLoadDialog != null && m.mLoadDialog.isShowing()) {
                    m.mLoadDialog.dismiss();
                    m.mLoadDialog = null;
                }

                switch (msg.what) {
                    case SUCC1: // 成功
                        m.listDatas = JsonUtil.stringToList((String) msg.obj, PO_list.class);
//                        m.listDatas = m.gGson().fromJson((String) msg.obj, new TypeToken<List<PO_list>>(){}.getType());

                        m.updateUI();

                        break;
                    case UNSUCC1: // 数据加载失败！
                        m.print_fun(m, "抱歉，没有加载到数据！");

                        break;
                    case SUCC2: // 成功物料
                        mtl met = JsonUtil.stringToObject((String) msg.obj, mtl.class);
                        m.updateUI();

                        break;
                    case UNSUCC2: // 物料数据加载失败！
                        m.print_fun(m, "抱歉，没有加载到数据！！！！！");

                        break;
                    case SUCC3: // 成功单位
                        t_MeasureUnit tUnit = JsonUtil.stringToObject((String) msg.obj, t_MeasureUnit.class);
                        m.updateUI();

                        break;
                    case UNSUCC3: // 单位数据加载失败！
                        m.print_fun(m, "抱歉，没有加载到数据！！！！！");

                        break;
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.wh_pur_order);
        ButterKnife.bind(this);
        UncaughtException.getInstance().setContext(context);

        initDatas();
        bundle();
        okhttpGet();
    }

    private void initDatas() {

    }

    private void bundle() {
        Bundle bundle = context.getIntent().getExtras();
        if (bundle != null) {
            supplier = bundle.getParcelable("supplier");
            tvCustInfo.setText("供应商：" + supplier.getFname());
        }
    }


    @OnClick({R.id.btn_close, R.id.btn_confirm})
    public void onViewClicked(View view) {
        Bundle bundle = null;
        switch (view.getId()) {
            case R.id.btn_close: // 关闭
                closeHandler(mHandler);
                context.finish();

                break;
            case R.id.btn_confirm: // 确认
                if(listDatas == null || listDatas.size() == 0) {
                    print_fun(context, "请选择数据在确认！");
                    return;
                }
                List<PO_list> list = new ArrayList<PO_list>();
                for(int i = 0, size = listDatas.size(); i<size; i++) {
                    PO_list p = listDatas.get(i);
                    if(p.getIsCheck() == 1) {
                        list.add(p);
                    }
                }
                if(list.size() == 0) {
                    print_fun(context, "请勾选数据行！");
                    return;
                }
                bundle = new Bundle();
                bundle.putSerializable("checkDatas", (Serializable)list);
                setResults(context, bundle);
                context.finish();

                break;
        }
    }

    @OnCheckedChanged(R.id.cbAll)
    public void onViewChecked(CompoundButton buttonView, boolean isChecked) {
        if (listDatas == null) {
            return;
        }
        if (isChecked) {
            for (int i = 0, size = listDatas.size(); i < size; i++) {
                PO_list p = listDatas.get(i);
                p.setIsCheck(1);
            }
        } else {
            for (int i = 0, size = listDatas.size(); i < size; i++) {
                PO_list p = listDatas.get(i);
                p.setIsCheck(0);
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 通过okhttp加载数据
     * 供应商信息
     */
    private void okhttpGet() {
        mLoadDialog = new LoadingDialog(context, "加载中", true);
        int id = supplier.getId();
//        int id = 0;
        String mUrl = Comm.getURL("PO_list" + (id > 0 ? "/" + id : ""));
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
                Log.e("Ware_Pur_OrderActivity --> onResponse", result);
                mHandler.sendMessage(msg);
            }
        });
    }

    private void okhttpGet2() {
        mLoadDialog = new LoadingDialog(context, "加载中", true);
        int id = supplier.getId();
        String mUrl = Comm.getURL("stock_position" + (id > 0 ? "/" + id : ""));
        Request.Builder requestBuilder = new Request.Builder().url(mUrl);
        requestBuilder.method("GET", null);

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(UNSUCC2);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String result = body.string();
                Message msg = mHandler.obtainMessage(SUCC2, result);
                Log.e("Ware_Pur_OrderActivity --> onResponse", result);
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 更新UI
     */
    private void updateUI() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        xRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new Ware_Pur_OrderAdapter(context, listDatas);
        xRecyclerView.setAdapter(mAdapter);
        xRecyclerView.setLoadingListener(context);

        xRecyclerView.setPullRefreshEnabled(false); // 上啦刷新禁用
        xRecyclerView.setLoadingMoreEnabled(false); // 不显示下拉刷新的view
    }


    @Override
    public void onRefresh() {
//        isRefresh = true;
//        isLoadMore = false;
//        page = 1;
//        initData();
    }

    @Override
    public void onLoadMore() {
//        isRefresh = false;
//        isLoadMore = true;
//        page += 1;
//        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case SEL_CUST: //查询供应商	返回
//                if (resultCode == RESULT_OK) {
//                    supplier = data.getParcelableExtra("obj");
//                    Log.e("onActivityResult --> SEL_CUST", supplier.getFname());
//                    if (supplier != null) {
//                        setTexts(etCustSel, supplier.getFname());
//                    }
//                }
//
//                break;
//        }
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
