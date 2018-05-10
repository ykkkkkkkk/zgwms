package ykk.xc.com.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 物料表
 */
public class mtl implements Parcelable {
    //    [id] [int] IDENTITY(1,1) NOT NULL,
//	[k3_fitemID] [int] NOT NULL,
//	[FShortNumber] [nvarchar](50) NULL,
//            [fnumber] [nvarchar](100) NULL,
//            [fname] [nvarchar](100) NULL,
//            [FModel] [nvarchar](100) NULL,
//            [funitID] [int] NULL,
//            [is_batch] [bit] NULL,
//            [is_sn] [bit] NOT NULL,
//	[barcode] [nvarchar](20) NULL
    private int id;
    private int k3_fitemID;
    private String FShortNumber;
    private String fnumber;
    private String fname;
    private String FModel;
    private int funitID; // 单位id
    private String funitName; // 外加的 单位名称
    private boolean is_batch;
    private boolean is_sn;
    private String barcode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getK3_fitemID() {
        return k3_fitemID;
    }

    public void setK3_fitemID(int k3_fitemID) {
        this.k3_fitemID = k3_fitemID;
    }

    public String getFShortNumber() {
        return FShortNumber;
    }

    public void setFShortNumber(String FShortNumber) {
        this.FShortNumber = FShortNumber;
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

    public String getFModel() {
        return FModel;
    }

    public void setFModel(String FModel) {
        this.FModel = FModel;
    }

    public int getFunitID() {
        return funitID;
    }

    public void setFunitID(int funitID) {
        this.funitID = funitID;
    }

    public boolean getIs_batch() {
        return is_batch;
    }

    public void setIs_batch(boolean is_batch) {
        this.is_batch = is_batch;
    }

    public boolean getIs_sn() {
        return is_sn;
    }

    public void setIs_sn(boolean is_sn) {
        this.is_sn = is_sn;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getFunitName() {
        return funitName;
    }

    public void setFunitName(String funitName) {
        this.funitName = funitName;
    }

    public mtl() {
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
     *
     * @param source
     */
    public mtl(Parcel source) {
        id = source.readInt();
        k3_fitemID = source.readInt();
        FShortNumber = source.readString();
        fnumber = source.readString();
        fname = source.readString();
        FModel = source.readString();
        funitID = source.readInt();
        int batch = source.readInt();
        is_batch = batch == 1 ? true : false;
        int sn = source.readInt();
        is_batch = sn == 1 ? true : false;
        barcode = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(k3_fitemID);
        dest.writeString(FShortNumber);
        dest.writeString(fnumber);
        dest.writeString(fname);
        dest.writeString(FModel);
        dest.writeInt(funitID);
        dest.writeInt(is_batch ? 1 : 0);
        dest.writeInt(is_sn ? 1 : 0);
        dest.writeString(barcode);
    }

    public static final Parcelable.Creator<mtl> CREATOR = new Parcelable.Creator<mtl>() {
        /**
         * 供外部类反序列化本类数组使用
         */
        @Override
        public mtl[] newArray(int size) {
            return new mtl[size];
        }

        /**
         * 从Parcel中读取数据
         */
        @Override
        public mtl createFromParcel(Parcel source) {
            return new mtl(source);
        }
    };

}
