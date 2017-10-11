package com.heaven7.java.data.mediator.support.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.SparseArray;

import java.io.IOException;

public class DoubleSparseArrayTypeAdapter extends PrimitiveSparseArrayTypeAdapter<Double> {
    @Override
    protected void writePrimitive(JsonWriter writer, Object value) throws IOException {
        writer.value(((Double) value).doubleValue());
    }

    @Override
    protected void readPrimitive(JsonReader reader, SparseArray<Double> map, int key) throws IOException {
        map.put(key, reader.nextDouble());
    }
}
