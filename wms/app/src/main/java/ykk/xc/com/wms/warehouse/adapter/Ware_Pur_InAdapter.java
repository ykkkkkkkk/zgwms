package ykk.xc.com.wms.warehouse.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ykk.xc.com.wms.R;
import ykk.xc.com.wms.comm.OnItemClickListener2;
import ykk.xc.com.wms.model.Scanning_record2;
import ykk.xc.com.wms.model.mtl;
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter;

public class Ware_Pur_InAdapter extends BaseArrayRecyclerAdapter<Scanning_record2> {

    private Activity context;
    private MyCallBack callBack;

    public Ware_Pur_InAdapter(Activity context, List<Scanning_record2> datas) {
        super(datas);
        this.context = context;
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.wh_pur_in_item;
    }

    @Override
    public void onBindHoder(RecyclerHolder holder, final Scanning_record2 entity, final int pos) {
        // 初始化id
        TextView tv_row = holder.obtainView(R.id.tv_row);
        TextView tv_mats = holder.obtainView(R.id.tv_mats);
        TextView tv_batch = holder.obtainView(R.id.tv_batch);
        TextView tv_nums = holder.obtainView(R.id.tv_nums);
        TextView tv_stockAP = holder.obtainView(R.id.tv_stockAP);
        // 赋值
        tv_row.setText(String.valueOf(pos + 1));
        tv_mats.setText(entity.getMatFnumber()+"\n"+entity.getMatFname()+"\n"+entity.getMatFModel());
        tv_batch.setText(entity.getBatchno());
        tv_nums.setText(Html.fromHtml(entity.getNum1()+"<br><font color='#FF6600'>"+entity.getNum2()+"</font>"));
        tv_stockAP.setText(entity.getStockName()+"\n"+entity.getStockAName()+"\n"+entity.getStockPName());
    }

    public void setCallBack(MyCallBack callBack) {
        this.callBack = callBack;
    }

    public interface MyCallBack {
        void onPrint(mtl entity, int position);
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
