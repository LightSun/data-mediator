package com.heaven7.java.data.mediator.support.test;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.SparseArray;

import java.io.IOException;

public class Car3SparseArrayTypeAdapter extends TypeAdapter<SparseArray<Car3>> {

    private final Car3TypeAdapter mAdapter = new Car3TypeAdapter();

    @Override
    public void write(JsonWriter out, SparseArray<Car3> value) throws IOException {
        out.beginObject();
        if(value != null) {
            for (int size = value.size(), i = size - 1; i >= 0; i--) {
                out.name(value.keyAt(i) + "");
                mAdapter.write(out, value.valueAt(i));
            }
        }
        out.endObject();
    }

    @Override
    public SparseArray<Car3> read(JsonReader in) throws IOException {
        SparseArray<Car3> map = new SparseArray<>();
        in.beginObject();
        while (in.hasNext()){
            String name = in.nextName();
            map.put(Integer.parseInt(name), mAdapter.read(in));
        }
        in.endObject();
        return map;
    }
}
