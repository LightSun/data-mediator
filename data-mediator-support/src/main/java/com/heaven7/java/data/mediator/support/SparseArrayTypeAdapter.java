package com.heaven7.java.data.mediator.support;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.SparseArray;

import java.io.IOException;

/**
 * the sparse array type adapter.
 * @param <T> the module data type. can't be primitive type or its' box type.
 * @since 1.0.1
 */
public class SparseArrayTypeAdapter<T> extends TypeAdapter<SparseArray<T>> {

    private final TypeAdapter<T> mAdapter;

    public SparseArrayTypeAdapter(TypeAdapter<T> baseAdapter) {
        this.mAdapter = baseAdapter;
    }

    @Override
    public void write(JsonWriter out, SparseArray<T> value) throws IOException {
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
    public SparseArray<T> read(JsonReader in) throws IOException {
        SparseArray<T> map = new SparseArray<>();
        in.beginObject();
        while (in.hasNext()){
            String name = in.nextName();
            map.put(Integer.parseInt(name), mAdapter.read(in));
        }
        in.endObject();
        return map;
    }

}
