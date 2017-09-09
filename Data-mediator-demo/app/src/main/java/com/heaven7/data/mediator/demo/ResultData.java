package com.heaven7.data.mediator.demo;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public class ResultData implements Parcelable {

    private List<Integer> intergers ;
    private List<Character> characters ;
    private List<Intent> intents ;

    private String[] testStrs ;
    private List<String> strs ;

    public ResultData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.intergers);
        dest.writeList(this.characters);
        dest.writeTypedList(this.intents);
        dest.writeStringArray(this.testStrs);
        dest.writeStringList(this.strs);
    }

    protected ResultData(Parcel in) {
        this.intergers = new ArrayList<Integer>();
        in.readList(this.intergers, Integer.class.getClassLoader());
        this.characters = new ArrayList<Character>();
        in.readList(this.characters, Character.class.getClassLoader());
        this.intents = in.createTypedArrayList(Intent.CREATOR);
        this.testStrs = in.createStringArray();
        this.strs = in.createStringArrayList();
    }

    public static final Creator<ResultData> CREATOR = new Creator<ResultData>() {
        @Override
        public ResultData createFromParcel(Parcel source) {
            return new ResultData(source);
        }

        @Override
        public ResultData[] newArray(int size) {
            return new ResultData[size];
        }
    };
}
