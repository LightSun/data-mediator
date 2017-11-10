package com.heaven7.data.mediator.demo.testpackage;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by heaven7 on 2017/9/8 0008.
 */

public class HistoryData /*extends ResultData*/ implements Parcelable {

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoryData that = (HistoryData) o;

        if (age != that.age) return false;
        if (id != that.id) return false;
        if (testShort != that.testShort) return false;
        if (testByte != that.testByte) return false;
        if (testBoolean != that.testBoolean) return false;
        if (Float.compare(that.testFloat, testFloat) != 0) return false;
        if (Double.compare(that.testDouble, testDouble) != 0) return false;
        if (testChar != that.testChar) return false;
        if (testLONG != null ? !testLONG.equals(that.testLONG) : that.testLONG != null)
            return false;
        if (testDOUBLE != null ? !testDOUBLE.equals(that.testDOUBLE) : that.testDOUBLE != null)
            return false;
        if (testCharacter != null ? !testCharacter.equals(that.testCharacter) : that.testCharacter != null)
            return false;
        if (testBOOLEAN != null ? !testBOOLEAN.equals(that.testBOOLEAN) : that.testBOOLEAN != null)
            return false;
        if (testSHORT != null ? !testSHORT.equals(that.testSHORT) : that.testSHORT != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (datas != null ? !datas.equals(that.datas) : that.datas != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(testArrayResultData, that.testArrayResultData)) return false;
        if (!Arrays.equals(testArrayInt, that.testArrayInt)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(testArrayInteger, that.testArrayInteger);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = age;
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (int) testShort;
        result = 31 * result + (int) testByte;
        result = 31 * result + (testBoolean ? 1 : 0);
        result = 31 * result + (testFloat != +0.0f ? Float.floatToIntBits(testFloat) : 0);
        temp = Double.doubleToLongBits(testDouble);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) testChar;
        result = 31 * result + (testLONG != null ? testLONG.hashCode() : 0);
        result = 31 * result + (testDOUBLE != null ? testDOUBLE.hashCode() : 0);
        result = 31 * result + (testCharacter != null ? testCharacter.hashCode() : 0);
        result = 31 * result + (testBOOLEAN != null ? testBOOLEAN.hashCode() : 0);
        result = 31 * result + (testSHORT != null ? testSHORT.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (datas != null ? datas.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(testArrayResultData);
        result = 31 * result + Arrays.hashCode(testArrayInt);
        result = 31 * result + Arrays.hashCode(testArrayInteger);
        return result;
    }

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
