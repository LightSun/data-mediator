package com.heaven7.java.data.mediator.support.test;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.data.mediator.support.gson.Car3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListCar3TypeAdapter extends TypeAdapter<List<Car3>> {

    private Car3TypeAdapter mAdapter = new Car3TypeAdapter();

    @Override
    public void write(JsonWriter out, List<Car3> value) throws IOException {
        out.beginArray();
        for(Car3 car3 : value){
            mAdapter.write(out, car3);
        }
        out.endArray();
    }

    @Override
    public List<Car3> read(JsonReader in) throws IOException {
        List<Car3> list = new ArrayList<>();
        in.beginArray();
        while (in.hasNext()){
            switch (in.peek()){
                case BEGIN_OBJECT:
                   // log("BEGIN_OBJECT");
                    list.add(mAdapter.read(in));
                    break;
            }
        }
        in.endArray();
        return list;
    }
}
