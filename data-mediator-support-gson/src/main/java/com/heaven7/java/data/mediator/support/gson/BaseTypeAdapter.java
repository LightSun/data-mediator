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
package com.heaven7.java.data.mediator.support.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.data.mediator.GlobalSetting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public BaseTypeAdapter(){
        this(new ArrayList<GsonProperty>());
    }

    public void addGsonProperty(GsonProperty gp){
        mProps.add(gp);
    }

    @Override
    public void write(JsonWriter out, T obj) throws IOException {
        //log("BaseTypeAdapter_write");
        out.beginObject();
        for (GsonProperty prop : mProps) {
            //check version
            //current 1.5 < since 2, . no
            //current 2.1 > until 2, . no
            if(prop.getSince() > GlobalSetting.getDefault().getCurrentVersion()
                    || prop.getUntil() < GlobalSetting.getDefault().getCurrentVersion()){
                 continue;
            }
            Object val = SupportUtils.getValue(prop, obj);
            if (val == null) {
                continue;
            }
            //gson name
            out.name(prop.getRealSerializeName());

          //  log("simpleType = " + simpleType.getName());
            TypeHandler.getTypeHandler(prop).write(out, prop, val);
        }
        out.endObject();
    }

    @Override
    public T read(JsonReader in) throws IOException {
        //log("BaseTypeAdapter_read");
        in.beginObject();
        final T t = create() ;
        while (in.hasNext()) {
            GsonProperty prop = getProperty(in.nextName());
            //check null and version
            if(prop == null || prop.getSince() > GlobalSetting.getDefault().getCurrentVersion()
                    || prop.getUntil() < GlobalSetting.getDefault().getCurrentVersion()){
                in.skipValue();
            }else {
                TypeHandler.getTypeHandler(prop).read(in, prop, t);
            }
        }
        in.endObject();
        return t;
    }

    /**
     * create instance for read.
     * @return the instance
     */
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
