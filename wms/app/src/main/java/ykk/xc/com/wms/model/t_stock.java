package ykk.xc.com.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 仓库表 t_stock
 */
public class t_stock implements Parcelable {
//    [id] [int] IDENTITY(1,1) NOT NULL,
//	[FItemID] [int] NOT NULL, 金蝶内码
//	[FNumber] [nvarchar](50) NOT NULL, 金蝶代码
//	[fname] [nvarchar](50) NOT NULL, 仓库名称
    private int id;
    private int FItemID;
    private String FNumber;
    private String fname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFItemID() {
        return FItemID;
    }

    public void setFItemID(int FItemID) {
        this.FItemID = FItemID;
    }

    public String getFNumber() {
        return FNumber;
    }

    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
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
    public t_stock(Parcel source) {
        id = source.readInt();
        FItemID = source.readInt();
        FNumber = source.readString();
        fname = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(FItemID);
        dest.writeString(FNumber);
        dest.writeString(fname);
    }

    public static final Parcelable.Creator<t_stock> CREATOR = new Parcelable.Creator<t_stock>() {
        /**
         * 供外部类反序列化本类数组使用
         */
        @Override
        public t_stock[] newArray(int size) {
            return new t_stock[size];
        }

        /**
         * 从Parcel中读取数据
         */
        @Override
        public t_stock createFromParcel(Parcel source) {
            return new t_stock(source);
        }
    };


}
