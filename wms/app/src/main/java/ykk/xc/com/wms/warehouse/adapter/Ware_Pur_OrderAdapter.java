package ykk.xc.com.wms.warehouse.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import ykk.xc.com.wms.R;
import ykk.xc.com.wms.model.PO_list;
import ykk.xc.com.wms.model.mtl;
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter;

public class Ware_Pur_OrderAdapter extends BaseArrayRecyclerAdapter<PO_list> {

    private Activity context;
    private MyCallBack callBack;
    private List<PO_list> datas;

    public Ware_Pur_OrderAdapter(Activity context, List<PO_list> datas) {
        super(datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.wh_pur_order_item;
    }

    @Override
    public void onBindHoder(RecyclerHolder holder, PO_list entity, final int pos) {
            // 初始化id
            TextView tv_row = holder.obtainView(R.id.tv_row);
            TextView tv_date = holder.obtainView(R.id.tv_date);
            TextView tv_stNo = holder.obtainView(R.id.tv_stNo);
            TextView tv_mts = holder.obtainView(R.id.tv_mts);
            TextView tv_numUnit = holder.obtainView(R.id.tv_numUnit);
            TextView tv_check = holder.obtainView(R.id.tv_check);
            // 赋值
            tv_row.setText(String.valueOf(pos + 1));
            tv_date.setText(entity.getPO_fdate());
            tv_stNo.setText(entity.getPO_number());
//            tv_mts.setText(entity.getMat().getFnumber()+"/"+entity.getMat().getFname());
//            tv_numUnit.setText(entity.getPO_fqty()+""+entity.getMat().getFunitName());
            tv_numUnit.setText(entity.getPO_fqty()+"");
            if(entity.getIsCheck() == 1) {
                tv_check.setBackgroundResource(R.drawable.check_true);
            } else {
                tv_check.setBackgroundResource(R.drawable.check_false);
            }
            View.OnClickListener click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.tv_check: // 选中
//                            if(callBack != null) {
//                                callBack.onClick(entity, pos);
//                            }
                            Log.e("setListener", "我点记录");
                            int check = datas.get(pos).getIsCheck();
                            if (check == 1) {
                                datas.get(pos).setIsCheck(0);
                            } else {
                                datas.get(pos).setIsCheck(1);
                            }
                            notifyDataSetChanged();
                            break;
                    }
                }
            };
            tv_check.setOnClickListener(click);
    }

    public void setCallBack(MyCallBack callBack) {
        this.callBack = callBack;
    }

    public interface MyCallBack {
        void onClick(PO_list entity, int position);
    }







    /*之下的方法都是为了方便操作，并不是必须的*/

    //在指定位置插入，原位置的向后移动一格
//    public boolean addItem(int position, String msg) {
//        if (position < datas.size() && position >= 0) {
//            datas.add(position, msg);
//            notifyItemInserted(position);
//            return true;
//        }
//        return false;
//    }
//
//    //去除指定位置的子项
//    public boolean removeItem(int position) {
//        if (position < datas.size() && position >= 0) {
//            datas.remove(position);
//            notifyItemRemoved(position);
//            return true;
//        }
//        return false;
//    }
    //清空显示数据
//    public void clearAll() {
//        datas.clear();
//        notifyDataSetChanged();
//    }


}
