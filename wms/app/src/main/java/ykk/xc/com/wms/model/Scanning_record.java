package ykk.xc.com.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Scanning_record {

    /**
     * ID : 1
     * type : 2
     * source_finterID : 1
     * fitemID : 3
     * batchno : sample string 4
     * fqty : 1.0
     * stock_id : 5
     * stock_area_id : 1
     * stock_position_id : 1
     * supplierID : 1
     * customerID : 1
     * fdate : 2018-05-08T17:38:51.9715695+08:00
     * empID : 1
     * operationID : 1
     */

    private int ID;
    private int type;
    private int source_finterID;
    private int fitemID;
    private String batchno;
    private double fqty;
    private int stock_id;
    private int stock_area_id;
    private int stock_position_id;
    private int supplierID;
    private int customerID;
    private String fdate;
    private int empID;
    private int operationID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSource_finterID() {
        return source_finterID;
    }

    public void setSource_finterID(int source_finterID) {
        this.source_finterID = source_finterID;
    }

    public int getFitemID() {
        return fitemID;
    }

    public void setFitemID(int fitemID) {
        this.fitemID = fitemID;
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public double getFqty() {
        return fqty;
    }

    public void setFqty(double fqty) {
        this.fqty = fqty;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public int getStock_area_id() {
        return stock_area_id;
    }

    public void setStock_area_id(int stock_area_id) {
        this.stock_area_id = stock_area_id;
    }

    public int getStock_position_id() {
        return stock_position_id;
    }

    public void setStock_position_id(int stock_position_id) {
        this.stock_position_id = stock_position_id;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getFdate() {
        return fdate;
    }

    public void setFdate(String fdate) {
        this.fdate = fdate;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public int getOperationID() {
        return operationID;
    }

    public void setOperationID(int operationID) {
        this.operationID = operationID;
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
//    public Scanning_record(Parcel source) {
//        ID = source.readInt();
//        type = source.readInt();
//        source_finterID = source.readInt();
//        fitemID = source.readInt();
//        batchno = source.readString();
//        fqty = source.readDouble();
//        stock_id = source.readInt();
//        stock_area_id = source.readInt();
//        stock_position_id = source.readInt();
//        supplierID = source.readInt();
//        customerID = source.readInt();
//        fdate = source.readString();
//        customerID = source.readInt();
//        empID = source.readInt();
//        operationID = source.readInt();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(ID);
//        dest.writeInt(type);
//        dest.writeInt(source_finterID);
//        dest.writeInt(fitemID);
//        dest.writeString(batchno);
//        dest.writeDouble(fqty);
//        dest.writeInt(stock_id);
//        dest.writeInt(stock_area_id);
//        dest.writeInt(stock_position_id);
//        dest.writeInt(supplierID);
//        dest.writeInt(customerID);
//        dest.writeString(fdate);
//        dest.writeInt(empID);
//        dest.writeInt(operationID);
//    }
//
//    public static final Parcelable.Creator<Scanning_record> CREATOR = new Parcelable.Creator<Scanning_record>() {
//        /**
//         * 供外部类反序列化本类数组使用
//         */
//        @Override
//        public Scanning_record[] newArray(int size) {
//            return new Scanning_record[size];
//        }
//
//        /**
//         * 从Parcel中读取数据
//         */
//        @Override
//        public Scanning_record createFromParcel(Parcel source) {
//            return new Scanning_record(source);
//        }
//    };
}
