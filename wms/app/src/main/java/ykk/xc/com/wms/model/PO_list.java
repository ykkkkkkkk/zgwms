package ykk.xc.com.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 采购表 PO_list
 */
public class PO_list implements Parcelable {
//    [id] [int] IDENTITY(1,1) NOT NULL,
//	[PO_id] [int] NOT NULL,
//	[PO_number] [nvarchar](50) NULL,
//            [supplierID] [int] NOT NULL,
//	[PO_fdate] [date] NOT NULL,
//	[PO_entyID] [int] NULL,
//            [fitemID] [int] NULL,
//            [PO_fqty] [numeric](18, 2) NULL,
//            [po_stockqty] [numeric](18, 2) NULL,
    private int id;
    private int PO_id;
    private String PO_number;
    private int supplierID; // 供应商id
//    private t_Supplier supplier; // 供应商对象
    private String PO_fdate;
    private int PO_entyID;
    private int fitemID; // 物料id
//    private PO_list mat;
    private double PO_fqty;
    private double po_stockqty;

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    private int isCheck; // 新加的是否选中

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPO_id() {
        return PO_id;
    }

    public void setPO_id(int PO_id) {
        this.PO_id = PO_id;
    }

    public String getPO_number() {
        return PO_number;
    }

    public void setPO_number(String PO_number) {
        this.PO_number = PO_number;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

//    public t_Supplier getSupplier() {
//        return supplier;
//    }
//
//    public void setSupplier(t_Supplier supplier) {
//        this.supplier = supplier;
//    }

    public String getPO_fdate() {
        return PO_fdate;
    }

    public void setPO_fdate(String PO_fdate) {
        this.PO_fdate = PO_fdate;
    }

    public int getPO_entyID() {
        return PO_entyID;
    }

    public void setPO_entyID(int PO_entyID) {
        this.PO_entyID = PO_entyID;
    }

    public int getFitemID() {
        return fitemID;
    }

    public void setFitemID(int fitemID) {
        this.fitemID = fitemID;
    }

//    public PO_list getMat() {
//        return mat;
//    }
//
//    public void setMat(PO_list mat) {
//        this.mat = mat;
//    }

    public double getPO_fqty() {
        return PO_fqty;
    }

    public void setPO_fqty(double PO_fqty) {
        this.PO_fqty = PO_fqty;
    }

    public double getPo_stockqty() {
        return po_stockqty;
    }

    public void setPo_stockqty(double po_stockqty) {
        this.po_stockqty = po_stockqty;
    }

    /**
     * 这里的读的顺序必须与writeToParcel(Parcel dest, int flags)方法中
     * 写的顺序一致，否则数据会有差错，比如你的读取顺序如果是：
     * nickname = source.readString();
     * username=source.readString();
     * age = source.readInt();
     * 即调换了username和nickname的读取顺序，那么你会发现你拿到的username是nickname的数据，
     * 而你拿到的nickname是username的数据
     *
     * @param source
     */
    public PO_list(Parcel source) {
        id = source.readInt();
        PO_id = source.readInt();
        PO_number = source.readString();
        supplierID = source.readInt();
        PO_fdate = source.readString();
        PO_entyID = source.readInt();
        fitemID = source.readInt();
        PO_fqty = source.readDouble();
        po_stockqty = source.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(PO_id);
        dest.writeString(PO_number);
        dest.writeInt(supplierID);
        dest.writeString(PO_fdate);
        dest.writeInt(PO_entyID);
        dest.writeInt(fitemID);
        dest.writeDouble(PO_fqty);
        dest.writeDouble(po_stockqty);
    }

    public static final Parcelable.Creator<PO_list> CREATOR = new Parcelable.Creator<PO_list>() {
        /**
         * 供外部类反序列化本类数组使用
         */
        @Override
        public PO_list[] newArray(int size) {
            return new PO_list[size];
        }

        /**
         * 从Parcel中读取数据
         */
        @Override
        public PO_list createFromParcel(Parcel source) {
            return new PO_list(source);
        }
    };


}
