package ykk.xc.com.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 库位表stock_position
 */
public class stock_position implements Parcelable {
//    [ID] [int] IDENTITY(1,1) NOT NULL,
//	[area_id] [int] NOT NULL,
//	[fnumber] [nvarchar](50) NOT NULL,
//	[fname] [nvarchar](50) NOT NULL,
    private int ID;
    private int area_id;
    private String fnumber;
    private String fname;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
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
    public stock_position(Parcel source) {
        ID = source.readInt();
        area_id = source.readInt();
        fnumber = source.readString();
        fname = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeInt(area_id);
        dest.writeString(fnumber);
        dest.writeString(fname);
    }

    public static final Parcelable.Creator<stock_position> CREATOR = new Parcelable.Creator<stock_position>() {
        /**
         * 供外部类反序列化本类数组使用
         */
        @Override
        public stock_position[] newArray(int size) {
            return new stock_position[size];
        }

        /**
         * 从Parcel中读取数据
         */
        @Override
        public stock_position createFromParcel(Parcel source) {
            return new stock_position(source);
        }
    };
    

}
