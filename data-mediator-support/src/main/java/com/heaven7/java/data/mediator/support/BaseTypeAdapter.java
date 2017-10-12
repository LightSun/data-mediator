/**
 * Copyright 2017
 group of data-mediator
 member: heaven7(donshine723@gmail.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.heaven7.java.data.mediator.support;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.Throwables;

import java.io.IOException;
import java.util.List;

import static com.heaven7.java.data.mediator.support.SupportUtils.getValue;
import static com.heaven7.java.data.mediator.support.TypeHandler.getTypeHandler;

/**
 * the base adapter for generate gson TypeAdapter
 * @param <T> the data type
 * @author heaven7
 */
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
            out.name(prop.getRealSerializeName());

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
            if(property == null){
                in.skipValue();
            }else {
                getTypeHandler(property).read(in, property, t);
            }
        }
        in.endObject();
        return t;
    }
    protected abstract T create();

    private GsonProperty getProperty(String name){
        for (GsonProperty prop : mProps) {
            if(name.equals(prop.getRealSerializeName())){
                return prop;
            }
        }
        return null;
    }
}
