package com.heaven7.java.data.mediator.support.test;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class StudentTypeAdapter extends TypeAdapter<IStudent> {
    @Override
    public void write(JsonWriter out, IStudent value) throws IOException {
        GsonTest.log("write");
        out.beginObject();
        out.name("name").value("heaven7");
        out.name("id").value("26");
        out.endObject();
    }
    @Override
    public IStudent read(JsonReader in) throws IOException {
        GsonTest.log("read");
        StudentModuleImpl module = new StudentModuleImpl();
        in.beginObject();
        while (in.hasNext()){
            switch (in.nextName()){
                case "name":
                    module.setName(in.nextString());
                    break;

                case "id":
                    module.setId(in.nextString());
                    break;

                default:
                    in.skipValue();
            }
        }
        in.endObject();
        return module;
    }
}
