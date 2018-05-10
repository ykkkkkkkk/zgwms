package ykk.xc.com.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 供应商表t_Supplier
 */
public class t_Supplier implements Parcelable {
    //供应商表	t_Supplier
    //id
   // fitemID	K3 ID
    //fnumber	供应商代码
    //fname	供应商名称
    private int id;
    private int fitemID;
    private String fnumber;
    private String fname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFitemID() {
        return fitemID;
    }

    public void setFitemID(int fitemID) {
        this.fitemID = fitemID;
    }

    public String getFnumber() {
        return fnumber;
    }

    public void setFnumber(String fnumber) {
        this.fnumber = fnumber;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public t_Supplier() {
        super();
    }

    /**
     * 这里的读的顺序必须与writeToParcel(Parcel dest, int flags)方法中
     * 写的顺序一致，否则数据会有差错，比如你的读取顺序如果是：
     * nickname = source.readString();
     * username=source.readString();
     * age = source.readInt();
     * 即调换了username和nickname的读取顺序，那么你会发现你拿到的username是nickname的数据，
     * 而你拿到的nickname是username的数据
     * @param source
     */
    public t_Supplier(Parcel source) {
        id = source.readInt();
        fitemID = source.readInt();
        fnumber = source.readString();
        fname = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(fitemID);
        dest.writeString(fnumber);
        dest.writeString(fname);
    }

    public static final Creator<t_Supplier> CREATOR = new Creator<t_Supplier>() {
        /**
         * 供外部类反序列化本类数组使用
         */
        @Override
        public t_Supplier[] newArray(int size) {
            return new t_Supplier[size];
        }

        /**
         * 从Parcel中读取数据
         */
        @Override
        public t_Supplier createFromParcel(Parcel source) {
            return new t_Supplier(source);
        }
    };

}
