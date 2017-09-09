package com.heaven7.data.mediator.demo.testpackage;

import android.os.Parcel;
import android.os.Parcelable;

import com.heaven7.data.mediator.demo.ResultData;

import java.util.List;

/**
 * Created by heaven7 on 2017/9/8 0008.
 */

public class HistoryData implements Parcelable {

    private int age;
    private long id;
    private short testShort;
    private byte testByte;

    boolean testBoolean;
    float testFloat;
    double testDouble;
    private char testChar;

    private Long testLONG;
    private Double testDOUBLE;
    private Character testCharacter;
    private Boolean testBOOLEAN;
    private Short testSHORT;

    private String name;
    private ResultData data;

    private List<ResultData> datas;

    private ResultData[] testArrayResultData;
    private int[] testArrayInt;
    //private short[] testArrayShort; //short bugs, in parcelable plugin
    private Integer[] testArrayInteger;
   // private Character[] testArrayCHAR; // error,  in parcelable plugin

    public HistoryData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.age);
        dest.writeLong(this.id);
        dest.writeInt(this.testShort);
        dest.writeByte(this.testByte);
        dest.writeByte(this.testBoolean ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.testFloat);
        dest.writeDouble(this.testDouble);
        dest.writeInt(this.testChar);
        dest.writeValue(this.testLONG);
        dest.writeValue(this.testDOUBLE);
        dest.writeSerializable(this.testCharacter);
        dest.writeValue(this.testBOOLEAN);
        dest.writeValue(this.testSHORT);
        dest.writeString(this.name);
        dest.writeParcelable(this.data, flags);
        dest.writeTypedList(this.datas);
        dest.writeTypedArray(this.testArrayResultData, flags);
        dest.writeIntArray(this.testArrayInt);
        dest.writeArray(this.testArrayInteger);
    }

    protected HistoryData(Parcel in) {
        this.age = in.readInt();
        this.id = in.readLong();
        this.testShort = (short) in.readInt();
        this.testByte = in.readByte();
        this.testBoolean = in.readByte() != 0;
        this.testFloat = in.readFloat();
        this.testDouble = in.readDouble();
        this.testChar = (char) in.readInt();
        this.testLONG = (Long) in.readValue(Long.class.getClassLoader());
        this.testDOUBLE = (Double) in.readValue(Double.class.getClassLoader());
        this.testCharacter = (Character) in.readSerializable();
        this.testBOOLEAN = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.testSHORT = (Short) in.readValue(Short.class.getClassLoader());
        this.name = in.readString();
        this.data = in.readParcelable(ResultData.class.getClassLoader());
        this.datas = in.createTypedArrayList(ResultData.CREATOR);
        this.testArrayResultData = in.createTypedArray(ResultData.CREATOR);
        this.testArrayInt = in.createIntArray();
        this.testArrayInteger = (Integer[]) in.readArray(Integer[].class.getClassLoader());
    }

    public static final Creator<HistoryData> CREATOR = new Creator<HistoryData>() {
        @Override
        public HistoryData createFromParcel(Parcel source) {
            return new HistoryData(source);
        }

        @Override
        public HistoryData[] newArray(int size) {
            return new HistoryData[size];
        }
    };
}
