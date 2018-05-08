package ykk.xc.com.wms.warehouse;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.EditText;
import android.widget.TextView;

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
import okhttp3.RequestBody;
import okhttp3.Response;
import ykk.xc.com.wms.R;
import ykk.xc.com.wms.comm.BaseActivity;
import ykk.xc.com.wms.comm.Comm;
import ykk.xc.com.wms.comm.UncaughtException;
import ykk.xc.com.wms.model.PO_list;
import ykk.xc.com.wms.model.Scanning_record;
import ykk.xc.com.wms.model.barcode_list;
import ykk.xc.com.wms.model.stock_area;
import ykk.xc.com.wms.model.stock_position;
import ykk.xc.com.wms.model.t_Department;
import ykk.xc.com.wms.model.t_Supplier;
import ykk.xc.com.wms.model.t_stock;
import ykk.xc.com.wms.util.JsonUtil;
import ykk.xc.com.wms.util.LoadingDialog;
import ykk.xc.com.wms.warehouse.adapter.Ware_Pur_InAdapter;

public class Ware_Pur_InActivity extends BaseActivity {

    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.btn_maker_code)
    Button btnMakerCode;
    @BindView(R.id.et_custSel)
    EditText etCustSel;
    @BindView(R.id.btn_custSel)
    Button btnCustSel;
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
    @BindView(R.id.tv_type)
    TextView tvTupe;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.et_batchNo)
    EditText etBatchNo;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.tv_sourceNo)
    TextView tvSourceNo;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_save)
    Button btnSave;

    private Ware_Pur_InActivity context = this;
    private LoadingDialog mLoadDialog;
    private static final int SEL_ORDER = 10, SEL_CUST = 11, SEL_STOCK = 12, SEL_STOCKA = 13, SEL_STOCKP = 14, SEL_DEPT = 15;
    private t_Supplier supplier; // 供应商
    private t_stock stock; // 仓库
    private stock_area stockA; // 库区
    private stock_position stockP; // 库位
    private t_Department department; // 部门
    private List<PO_list> checkDatas;
    private Ware_Pur_InAdapter mAdapter;


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
            Ware_Pur_InActivity m = mActivity.get();
            if (m != null) {
                if (m.mLoadDialog != null && m.mLoadDialog.isShowing()) {
                    m.mLoadDialog.dismiss();
                    m.mLoadDialog = null;
                }


            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.wh_pur_in);
        ButterKnife.bind(this);
        UncaughtException.getInstance().setContext(context);

        initDatas();
    }

    private void initDatas() {

    }


    @OnClick({R.id.btn_close,R.id.tv_sourceNo,R.id.btn_maker_code,R.id.btn_custSel,R.id.btn_whName,
              R.id.btn_whArea,R.id.btn_whPos,R.id.btn_deptName,R.id.btn_add,R.id.btn_save})
    public void onViewClicked(View view) {
        Bundle bundle = null;
        switch (view.getId()) {
            case R.id.btn_close: // 关闭
                closeHandler(mHandler);
                context.finish();

                break;
            case R.id.btn_maker_code: // 打印条码界面
                show(context, Ware_Makter_CodeActivity.class, null);

                break;
            case R.id.tv_sourceNo: // 选择来源单号
                if (supplier == null) {
                    print_fun(context, "请选择供应商！");
                    return;
                }
                bundle = new Bundle();
                bundle.putParcelable("supplier", supplier);
                showForResult(context, Ware_Pur_OrderActivity.class, SEL_ORDER, bundle);

                break;
            case R.id.btn_custSel: // 选择供应商
                showForResult(context, Cust_DialogActivity.class, SEL_CUST, null);

                break;
            case R.id.btn_whName: // 选择仓库
                showForResult(context, Stock_DialogActivity.class, SEL_STOCK, null);

                break;
            case R.id.btn_whArea: // 选择库区
                if (stock == null) {
                    print_fun(context, "请先选择仓库！");
                    return;
                }
                bundle = new Bundle();
                bundle.putInt("stockId", stock.getId());
                showForResult(context, StockArea_DialogActivity.class, SEL_STOCKA, bundle);

                break;
            case R.id.btn_whPos: // 选择库位
                if (stockA == null) {
                    print_fun(context, "请先选择库区！");
                    return;
                }
                bundle = new Bundle();
                bundle.putInt("areaId", stockA.getID());
                showForResult(context, StockPos_DialogActivity.class, SEL_STOCKP, bundle);

                break;
            case R.id.btn_deptName:
                showForResult(context, Dept_DialogActivity.class, SEL_DEPT, null);

                break;
            case R.id.btn_add:


                break;
            case R.id.btn_save: // 保存
                okhttpPost();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SEL_CUST: //查询供应商	返回
                if (resultCode == RESULT_OK) {
                    supplier = data.getParcelableExtra("obj");
                    Log.e("onActivityResult --> SEL_CUST", supplier.getFname());
                    if (supplier != null) {
                        setTexts(etCustSel, supplier.getFname());
                    }
                }

                break;
            case SEL_ORDER: // 查询订单返回
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        checkDatas = (List<PO_list>) bundle.getSerializable("checkDatas");
                        updateUI();
                    }
                }

                break;
            case SEL_STOCK: //查询仓库	返回
                if (resultCode == RESULT_OK) {
                    stock = data.getParcelableExtra("obj");
                    Log.e("onActivityResult --> SEL_STOCK", stock.getFname());
                    if (stock != null) {
                        setTexts(etWhName, stock.getFname());
                    }
                }

                break;
            case SEL_STOCKA: //查询库区	返回
                if (resultCode == RESULT_OK) {
                    stockA = data.getParcelableExtra("obj");
                    Log.e("onActivityResult --> SEL_STOCKA", stockA.getFname());
                    if (stockA != null) {
                        setTexts(etWhArea, stockA.getFname());
                    }
                }

                break;
            case SEL_STOCKP: //查询库位	返回
                if (resultCode == RESULT_OK) {
                    stockP = data.getParcelableExtra("obj");
                    Log.e("onActivityResult --> SEL_STOCKP", stockP.getFname());
                    if (stockP != null) {
                        setTexts(etWhPos, stockP.getFname());
                    }
                }

                break;
            case SEL_DEPT: //查询部门	返回
                if (resultCode == RESULT_OK) {
                    department = data.getParcelableExtra("obj");
                    Log.e("onActivityResult --> SEL_DEPT", department.getFname());
                    if (department != null) {
                        setTexts(etDeptName, department.getFname());
                    }
                }

                break;
        }
    }

    /**
     * 更新UI
     */
    private void updateUI() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        } else {
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mAdapter = new Ware_Pur_InAdapter(context, checkDatas);
            recyclerView.setAdapter(mAdapter);
        }
//        mAdapter.setOnItemClickListener(new OnItemClickListener2() {
////            @Override
////            public void onItemClick(View view, int pos) {
////                PO_list supplier = checkDatas.get(pos);
////                Intent intent = new Intent();
////                intent.putExtra("obj", supplier);
////                context.setResult(RESULT_OK, intent);
////                context.finish();
////            }
////        });
    }


    private void okhttpPost() {
        formBody = new FormBody.Builder()
                .add("Email", "sample string 1")
                .build();
        Scanning_record record = new Scanning_record();
        record.setID(1);
        record.setType(2);
        record.setSource_finterID(1);
        record.setFitemID(3);
        record.setBatchno("1112");
        record.setFqty(12.34);
        record.setStock_id(31);
        record.setStock_area_id(32);
        record.setStock_position_id(33);
        record.setSupplierID(34);
        record.setCustomerID(35);
        record.setFdate("2018-05-08");
        record.setEmpID(1);
        record.setOperationID(1);
        String mJson = JsonUtil.objectToString(record);
        RequestBody body = RequestBody.create(Comm.JSON, mJson);

        Request request = new Request.Builder()
                .url(Comm.getURL("Scanning_record/PostAdd"))
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
