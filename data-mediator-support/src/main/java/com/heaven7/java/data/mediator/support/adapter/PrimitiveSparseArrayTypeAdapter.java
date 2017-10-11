package com.heaven7.java.data.mediator.support.adapter;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.SparseArray;

import java.io.IOException;

/**
 * primitive sparse array type adapter
 * @param <T> the primitive type
 */
public abstract class PrimitiveSparseArrayTypeAdapter<T> extends TypeAdapter<SparseArray<T>> {

    @Override
    public void write(JsonWriter out, SparseArray<T> map) throws IOException {
        out.beginObject();
        if(map != null) {
            try {
                for (int size = map.size(), i = size - 1; i >= 0; i--) {
                    out.name(map.keyAt(i) + "");
                    writePrimitive(out, map.valueAt(i));
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        out.endObject();
    }

    @Override
    public SparseArray<T> read(JsonReader in) throws IOException {
        SparseArray<T> map = new SparseArray<>();
        in.beginObject();
        try {
            while (in.hasNext()) {
                int key = Integer.parseInt(in.nextName());
                readPrimitive(in, map, key);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        in.endObject();
        return map;
    }
    protected abstract void writePrimitive(JsonWriter writer, Object value) throws IOException;
    protected abstract void readPrimitive(JsonReader reader, SparseArray<T> map, int key)  throws IOException;
}
