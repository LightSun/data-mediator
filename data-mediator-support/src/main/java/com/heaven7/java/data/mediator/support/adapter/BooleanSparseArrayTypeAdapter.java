package com.heaven7.java.data.mediator.support.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.SparseArray;

import java.io.IOException;

public class BooleanSparseArrayTypeAdapter extends PrimitiveSparseArrayTypeAdapter<Boolean> {
    @Override
    protected void writePrimitive(JsonWriter writer, Object value) throws IOException {
        writer.value((Boolean)value);
    }

    @Override
    protected void readPrimitive(JsonReader reader, SparseArray<Boolean> map, int key) throws IOException {
        map.put(key, reader.nextBoolean());
    }
}
