package com.heaven7.java.data.mediator.test;

/**
 * Created by heaven7 on 2017/9/7.
 */
public interface Parcelable {

     interface Creator<T> {
         T createFromParcel(Parcel source);

         T[] newArray(int size);
    }

}
