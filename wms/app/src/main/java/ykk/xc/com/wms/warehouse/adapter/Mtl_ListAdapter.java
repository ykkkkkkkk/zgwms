package ykk.xc.com.wms.warehouse.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

import java.util.List;

import ykk.xc.com.wms.R;
import ykk.xc.com.wms.model.mtl;
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter;

public class Mtl_ListAdapter extends BaseArrayRecyclerAdapter<mtl> {

    private Activity context;
    private MyCallBack callBack;
    private List<mtl> datas;

    public Mtl_ListAdapter(Activity context, List<mtl> datas) {
        super(datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.wh_mtl_list_item;
    }

    @Override
    public void onBindHoder(RecyclerHolder holder, mtl entity, final int pos) {
        // 初始化id
        TextView tv_row = holder.obtainView(R.id.tv_row);
        TextView tv_fnumber = holder.obtainView(R.id.tv_fnumber);
        TextView tv_fname = holder.obtainView(R.id.tv_fname);
        TextView tv_FModel = holder.obtainView(R.id.tv_FModel);
        TextView tv_isBatch = holder.obtainView(R.id.tv_isBatch);
        TextView tv_isSn = holder.obtainView(R.id.tv_isSn);
        // 赋值
        tv_row.setText(String.valueOf(pos + 1));
        tv_fnumber.setText(entity.getFnumber());
        tv_fname.setText(entity.getFname());
        tv_FModel.setText(entity.getFModel());
        if (entity.getIs_batch()) {
            tv_isBatch.setText("已启用");
            tv_isBatch.setTextColor(Color.parseColor("#009900"));
        } else {
            tv_isBatch.setText("未启用");
            tv_isBatch.setTextColor(Color.parseColor("#666666"));
        }
        if (entity.getIs_sn()) {
            tv_isSn.setText("已启用");
            tv_isSn.setTextColor(Color.parseColor("#009900"));
        } else {
            tv_isSn.setText("未启用");
            tv_isSn.setTextColor(Color.parseColor("#666666"));
        }
//        holder.onClick();
//        View.OnClickListener click = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()){
//                    case R.id.tv_check: // 选中
////                            if(callBack != null) {
////                                callBack.onClick(entity, pos);
////                            }
//                        Log.e("setListener", "我点记录");
//                        int check = datas.get(pos).getIsCheck();
//                        if (check == 1) {
//                            datas.get(pos).setIsCheck(0);
//                        } else {
//                            datas.get(pos).setIsCheck(1);
//                        }
//                        notifyDataSetChanged();
//                        break;
//                }
//            }
//        };
//        tv_check.setOnClickListener(click);
    }

    public void setCallBack(MyCallBack callBack) {
        this.callBack = callBack;
    }

    public interface MyCallBack {
        void onClick(mtl entity, int position);
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
