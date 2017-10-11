package com.heaven7.java.data.mediator.support.test;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StringArrayAdapter extends TypeAdapter<List<String>>{

    @Override
    public void write(JsonWriter out, List<String> value) throws IOException {
        out.beginArray();
        for(String str : value){
            out.value(str);
        }
        out.endArray();
    }

    @Override
    public List<String> read(JsonReader in) throws IOException {
        return readStringArray(in);
    }

    public List<String> readStringArray(JsonReader reader) throws IOException {
        List<String> colors = new ArrayList<String>();
        reader.beginArray();
        while (reader.hasNext()) {
            colors.add(reader.nextString());
        }
        reader.endArray();
        return colors;
    }
}
