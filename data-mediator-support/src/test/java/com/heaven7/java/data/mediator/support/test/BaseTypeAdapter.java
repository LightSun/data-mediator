package com.heaven7.java.data.mediator.support.test;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.data.mediator.FieldFlags;
import com.heaven7.java.data.mediator.support.GsonProperty;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static com.heaven7.java.data.mediator.support.SupportUtils.getValue;
import static com.heaven7.java.data.mediator.support.SupportUtils.writePrimitiveOrItsBox;
import static com.heaven7.java.data.mediator.support.test.GsonTest.log;

public abstract class BaseTypeAdapter<T> extends TypeAdapter<T> {

    private final List<GsonProperty> mProps;

    public BaseTypeAdapter(GsonProperty... props) {
        this.mProps = Arrays.asList(props);
    }

    @Override
    public void write(JsonWriter out, T obj) throws IOException {
        log("BaseTypeAdapter_write");
        out.beginObject();
        for (GsonProperty prop : mProps) {
            Object val = getValue(prop, obj);
            if (val == null) {
                continue;
            }
            Class<?> simpleType = prop.getType();
            //gson name
            out.name(prop.getSerializeName());

            if (prop.isPrimitive()) {
                writePrimitiveOrItsBox(out, simpleType, val);
            } else {
                switch (prop.getComplexType()) {
                    case FieldFlags.COMPLEX_LIST: {
                        out.beginArray();
                        List list = (List) val;
                        if (!list.isEmpty()) {
                            if (simpleType.isPrimitive()) {
                                for (Object item : list) {
                                    writePrimitiveOrItsBox(out, simpleType, item);
                                }
                            } else {
                                for (Object item : list) {
                                    getTypeAdapter(simpleType).write(out, item);
                                }
                            }
                        }
                        out.endArray();
                    }
                    break;

                    case FieldFlags.COMPLEX_ARRAY:
                        out.beginArray();
                        int length = Array.getLength(val);
                        if(length > 0) {
                            if (simpleType.isPrimitive()) {
                                for (int i = 0; i < length; i++) {
                                    writePrimitiveOrItsBox(out, simpleType, Array.get(val, i));
                                }
                            } else {
                                for (int i = 0; i < length; i++) {
                                    getTypeAdapter(simpleType).write(out, Array.get(val, i));
                                }
                            }
                        }
                        out.endArray();
                        break;

                    case FieldFlags.COMPLEX_SPARSE_ARRAY:
                        getSparseArrayTypeAdapter(simpleType).write(out, val);
                        break;

                    default:
                        getTypeAdapter(simpleType).write(out, val);
                        break;
                }
            }
        }
        out.endObject();
    }

    @Override
    public T read(JsonReader in) throws IOException {
        log("BaseTypeAdapter_read");
        in.beginObject();
        in.endObject();
        return null;
    }

    protected abstract TypeAdapter getTypeAdapter(Class<?> itemType);

    protected abstract TypeAdapter getSparseArrayTypeAdapter(Class<?> itemType);

}
