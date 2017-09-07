package com.heaven7.java.data.mediator.test;

public interface Creator<T> {
        public T createFromParcel(Parcel source);
        
        public T[] newArray(int size);
    }
