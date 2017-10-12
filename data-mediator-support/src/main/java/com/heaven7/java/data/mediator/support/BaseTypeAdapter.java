package com.heaven7.java.data.mediator.support;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.Throwables;

import java.io.IOException;
import java.util.List;

import static com.heaven7.java.data.mediator.support.SupportUtils.getValue;
import static com.heaven7.java.data.mediator.support.TypeHandler.getTypeHandler;

public abstract class BaseTypeAdapter<T> extends TypeAdapter<T> {

    private final List<GsonProperty> mProps;

    public BaseTypeAdapter(List<GsonProperty> props) {
        Throwables.checkNull(props);
        this.mProps = props;
    }

    @Override
    public void write(JsonWriter out, T obj) throws IOException {
        //log("BaseTypeAdapter_write");
        out.beginObject();
        for (GsonProperty prop : mProps) {
            Object val = getValue(prop, obj);
            if (val == null) {
                continue;
            }
            //gson name
            out.name(prop.getSerializeName());

          //  log("simpleType = " + simpleType.getName());
            getTypeHandler(prop).write(out, prop, val);
        }
        out.endObject();
    }

    @Override
    public T read(JsonReader in) throws IOException {
        //log("BaseTypeAdapter_read");
        in.beginObject();
        final T t = create() ;
        while (in.hasNext()) {
            GsonProperty property = getProperty(in.nextName());
            getTypeHandler(property).read(in, property, t);
        }
        in.endObject();
        return t;
    }
    protected abstract T create();

    private GsonProperty getProperty(String name){
        for (GsonProperty prop : mProps) {
            if(name.equals(prop.getSerializeName())){
                return prop;
            }
        }
        throw new IllegalStateException("the json key can't find mapping . name = " + name);
    }
}
