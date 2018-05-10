package ykk.xc.com.wms.warehouse;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import ykk.xc.com.wms.comm.UncaughtException;
import ykk.xc.com.wms.model.mtl;
import ykk.xc.com.wms.util.JsonUtil;
import ykk.xc.com.wms.util.LoadingDialog;
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter;
import ykk.xc.com.wms.util.xrecyclerview.XRecyclerView;
import ykk.xc.com.wms.warehouse.adapter.Mtl_ListAdapter;

public class Mtl_ListActivity extends BaseActivity implements XRecyclerView.LoadingListener {

    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private Mtl_ListActivity context = this;
    private static final int SUCC1 = 200, UNSUCC1 = 500;
    private LoadingDialog mLoadDialog;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private FormBody formBody = null;
    private Mtl_ListAdapter mAdapter;
    private List<mtl> listDatas;

    // 消息处理
    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<Mtl_ListActivity> mActivity;

        public MyHandler(Mtl_ListActivity activity) {
            mActivity = new WeakReference<Mtl_ListActivity>(activity);
        }

        public void handleMessage(Message msg) {
            Mtl_ListActivity m = mActivity.get();
            if (m != null) {
                if (m.mLoadDialog != null && m.mLoadDialog.isShowing()) {
                    m.mLoadDialog.dismiss();
                    m.mLoadDialog = null;
                }

                switch (msg.what) {
                    case SUCC1: // 成功
                        m.listDatas = JsonUtil.stringToList((String) msg.obj, mtl.class);
//                        m.listDatas = m.gGson().fromJson((String) msg.obj, new TypeToken<List<mtl>>(){}.getType());

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.wh_mtl_list);
        ButterKnife.bind(this);
        UncaughtException.getInstance().setContext(context);

        initDatas();
        bundle();
//        okhttpGet();
    }

    private void initDatas() {

    }

    private void bundle() {
        Bundle bundle = context.getIntent().getExtras();
        if (bundle != null) {
        }
    }


    @OnClick({R.id.btn_close, R.id.btn_search})
    public void onViewClicked(View view) {
        Bundle bundle = null;
        switch (view.getId()) {
            case R.id.btn_close: // 关闭
                closeHandler(mHandler);
                context.finish();

                break;
            case R.id.btn_search: // 查询
                okhttpGet();

                break;
        }
    }

    /**
     * 通过okhttp加载数据
     * 供应商信息
     */
    private void okhttpGet() {
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
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        xRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        xRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new Mtl_ListAdapter(context, listDatas);
        xRecyclerView.setAdapter(mAdapter);
        xRecyclerView.setLoadingListener(context);

        xRecyclerView.setPullRefreshEnabled(false); // 上啦刷新禁用
        xRecyclerView.setLoadingMoreEnabled(false); // 不显示下拉刷新的view

        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.RecyclerHolder holder, View view, int pos) {
                mtl m = listDatas.get(pos);
                Intent intent = new Intent();
                intent.putExtra("obj", m);
                context.setResult(RESULT_OK, intent);
                context.finish();
            }
        });
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
