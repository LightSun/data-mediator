package com.heaven7.java.data.mediator.support.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.SparseArray;

import java.io.IOException;


public class FloatSparseArrayTypeAdapter extends PrimitiveSparseArrayTypeAdapter<Float> {
    @Override
    protected void writePrimitive(JsonWriter writer, Object value) throws IOException {
        writer.value(((Double) value).floatValue());
    }
    @Override
    protected void readPrimitive(JsonReader reader, SparseArray<Float> map, int key) throws IOException {
        map.put(key, (float) reader.nextDouble());
    }
}
