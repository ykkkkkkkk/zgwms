package ykk.xc.com.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Scanning_record2 implements Serializable {

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
    private String matFnumber; // 新加
    private String matFname; // 新加
    private String matFModel; // 新加
    private String batchno;
    private double fqty;
    private int stock_id;
    private String stockName; // 新加
    private int stock_area_id;
    private String stockAName; // 新加
    private int stock_position_id;
    private String stockPName; // 新加
    private int supplierID;
    private String supplierName; // 新加
    private int customerID;
    private String fdate;
    private int empID;
    private int operationID;
    private double num1;

    public double getNum1() {
        return num1;
    }

    public void setNum1(double num1) {
        this.num1 = num1;
    }

    public double getNum2() {
        return num2;
    }

    public void setNum2(double num2) {
        this.num2 = num2;
    }

    private double num2;

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

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public int getStock_area_id() {
        return stock_area_id;
    }

    public void setStock_area_id(int stock_area_id) {
        this.stock_area_id = stock_area_id;
    }

    public String getStockAName() {
        return stockAName;
    }

    public void setStockAName(String stockAName) {
        this.stockAName = stockAName;
    }

    public int getStock_position_id() {
        return stock_position_id;
    }

    public void setStock_position_id(int stock_position_id) {
        this.stock_position_id = stock_position_id;
    }

    public String getStockPName() {
        return stockPName;
    }

    public void setStockPName(String stockPName) {
        this.stockPName = stockPName;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public String getMatFnumber() {
        return matFnumber;
    }

    public void setMatFnumber(String matFnumber) {
        this.matFnumber = matFnumber;
    }

    public String getMatFname() {
        return matFname;
    }

    public void setMatFname(String matFname) {
        this.matFname = matFname;
    }

    public String getMatFModel() {
        return matFModel;
    }

    public void setMatFModel(String matFModel) {
        this.matFModel = matFModel;
    }


    public Scanning_record2() {
        super();
    }

}
