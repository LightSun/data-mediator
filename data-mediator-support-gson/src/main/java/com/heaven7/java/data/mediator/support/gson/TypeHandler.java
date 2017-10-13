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
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.FieldFlags;
import com.heaven7.java.data.mediator.Property;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the type handler help us read and write object for gson.
 * @author heaven7
 */
public abstract class TypeHandler {

    private static final TypeHandler PRIMITVE = new PrimitiveTypeHandler();
    private static final SparseArray<TypeHandler> sHandlers;
    private static final Map<Class<?>, TypeAdapter<?>> sTypeAdapterMap;

    static {
        sHandlers = new SparseArray<>();
        sTypeAdapterMap = new HashMap<>();
    }

    /**
     * write the value to the writer.
     * @param out the json out writer
     * @param property the gson property
     * @param value the value to write
     * @throws IOException when gson exception occurs
     */
    public abstract void write(JsonWriter out, GsonProperty property, Object value) throws IOException;

    /**
     * read the object to the target t .
     * @param in the json reader
     * @param property the gson property
     * @param t the object to set to.
     * @throws IOException when gson exception occurs
     */
    public abstract void read(JsonReader in, GsonProperty property, Object t) throws IOException;

    //==================================================================================================

    //if is java.lang.Object....

    /**
     * register the type adapter
     * @param clazz the class.
     * @param adapter the type adapter
     * @param <T> the object type.
     */
    public static <T> void registerTypeAdapter(Class<T> clazz, TypeAdapter<? super T> adapter){
        sTypeAdapterMap.put(clazz, adapter);
    }

    public static TypeHandler getTypeHandler(Property prop){
        if(prop.getComplexType() == 0){
            if(prop.getType().isPrimitive() || SupportUtils.isBoxedClass(prop.getType())){
                return PRIMITVE;
            }
        }
        return getTypeHandlerInternal(prop.getComplexType());
    }

    public static TypeAdapter getTypeAdapter(Class<?> type){
        return  sTypeAdapterMap.get(type);
    }
    private static TypeHandler getTypeHandlerInternal(int complexType) {
        TypeHandler handler = sHandlers.get(complexType);
        if(handler == null){
            switch (complexType){
                case FieldFlags.COMPLEX_LIST:
                    handler = new ListTypeHandler();
                    break;

                case FieldFlags.COMPLEX_ARRAY:
                    handler = new ArrayTypeHandler();
                    break;

                case FieldFlags.COMPLEX_SPARSE_ARRAY:
                    handler = new SparseArrayTypeHandler();
                    break;

                case 0:
                    handler = new SimpleTypeHandler();
                    break;

                default:
                    throw new UnsupportedOperationException();
            }
            sHandlers.put(complexType, handler);
        }
        return handler;
    }

    private static class SimpleTypeHandler extends TypeHandler {
        @Override
        public void write(JsonWriter out, GsonProperty property, Object value) throws IOException {
               getTypeAdapter(property.getType()).write(out, value);
        }
        @Override
        public void read(JsonReader in, GsonProperty property, Object t) throws IOException {
            Object val = getTypeAdapter(property.getType()).read(in);
            SupportUtils.setValue(property, t, val);
        }
    }

    private static class PrimitiveTypeHandler extends TypeHandler {
        @Override
        public void write(JsonWriter writer, GsonProperty property, Object value) throws IOException {
            SupportUtils.writePrimitiveOrItsBox(writer, property.getType(), value);
        }
        @Override
        public void read(JsonReader in, GsonProperty property, Object t) throws IOException {
            Object val = SupportUtils.readPrimitiveOrItsBox(in, property);
            SupportUtils.setValue(property, t, val);
        }
    }
    private static class SparseArrayTypeHandler extends TypeHandler {
        @Override
        public void write(JsonWriter out,  GsonProperty property, Object value) throws IOException {
            final Class<?> simpleType = property.getType();
            out.beginObject();
            if(value != null) {
                final SparseArray sa = (SparseArray) value;
                if(simpleType.isPrimitive() || SupportUtils.isBoxedClass(simpleType)){
                    for (int size = sa.size(), i = size - 1; i >= 0; i--) {
                        out.name(sa.keyAt(i) + "");
                        SupportUtils.writePrimitiveOrItsBox(out, simpleType, sa.valueAt(i));
                    }
                }else {
                    TypeAdapter adapter = getTypeAdapter(simpleType);
                    for (int size = sa.size(), i = size - 1; i >= 0; i--) {
                        out.name(sa.keyAt(i) + "");
                        adapter.write(out, sa.valueAt(i));
                    }
                }
            }
            out.endObject();
        }

        @Override
        public void read(JsonReader in, GsonProperty property, Object t) throws IOException {
            final Class<?> simpleType = property.getType();
            final SparseArray sa = new SparseArray();

            in.beginObject();
            if (simpleType.isPrimitive() || SupportUtils.isBoxedClass(simpleType)) {
                while (in.hasNext()) {
                    int key = Integer.parseInt(in.nextName());
                    sa.put(key, SupportUtils.readPrimitiveOrItsBox(in, property));
                }
            }else{
                TypeAdapter adapter = getTypeAdapter(simpleType);
                while (in.hasNext()) {
                    int key = Integer.parseInt(in.nextName());
                    Object val = adapter.read(in);
                    if (val != null) {
                        sa.put(key, val);
                    }
                }
            }
            SupportUtils.setValue(property, t, sa);
            in.endObject();
        }
    }

    private static class ArrayTypeHandler extends TypeHandler {
        @Override
        public void write(JsonWriter out, GsonProperty property, Object value) throws IOException {
            final Class<?> simpleType = property.getType();
            out.beginArray();
            int length = Array.getLength(value);
            if(length > 0) {
                if (simpleType.isPrimitive() || SupportUtils.isBoxedClass(simpleType)) {
                    for (int i = 0; i < length; i++) {
                        SupportUtils.writePrimitiveOrItsBox(out, simpleType, Array.get(value, i));
                    }
                } else {
                    TypeAdapter adapter = getTypeAdapter(simpleType);
                    for (int i = 0; i < length; i++) {
                        adapter.write(out, Array.get(value, i));
                    }
                }
            }
            out.endArray();
        }

        @Override
        public void read(JsonReader in, GsonProperty property, Object t) throws IOException {
            final Class<?> simpleType = property.getType();

            List list = new ArrayList();
            in.beginArray();
            if (simpleType.isPrimitive() || SupportUtils.isBoxedClass(simpleType)) {
                while (in.hasNext()){
                    list.add(SupportUtils.readPrimitiveOrItsBox(in, property));
                }
            }else{
                TypeAdapter adapter = getTypeAdapter(simpleType);
                while (in.hasNext()){
                    list.add(adapter.read(in));
                }
            }
            //copy to array
            Object array = Array.newInstance(simpleType, list.size());
            for(int i = 0, size = list.size() ; i < size ; i ++){
                Array.set(array, i, list.get(i));
            }
            SupportUtils.setValue(property, t, array);
            in.endArray();
        }
    }

    private static class ListTypeHandler extends TypeHandler {
        @Override
        public void write(JsonWriter out,  GsonProperty property, Object value) throws IOException {
            final Class<?> simpleType = property.getType();
            out.beginArray();
            List list = (List) value;
            if (!list.isEmpty()) {
                if (simpleType.isPrimitive()|| SupportUtils.isBoxedClass(simpleType)) {
                    for (Object item : list) {
                        SupportUtils.writePrimitiveOrItsBox(out, simpleType, item);
                    }
                } else {
                    TypeAdapter adapter = getTypeAdapter(simpleType);
                    for (Object item : list) {
                        adapter.write(out, item);
                    }
                }
            }
            out.endArray();
        }

        @Override
        public void read(JsonReader in, GsonProperty property, Object t) throws IOException {
            final Class<?> simpleType = property.getType();

            List list = new ArrayList();
            in.beginArray();
            if (simpleType.isPrimitive() || SupportUtils.isBoxedClass(simpleType)) {
                while (in.hasNext()){
                    list.add(SupportUtils.readPrimitiveOrItsBox(in, property));
                }
            }else{
                TypeAdapter adapter = getTypeAdapter(simpleType);
                while (in.hasNext()){
                    list.add(adapter.read(in));
                }
            }
            SupportUtils.setValue(property, t, list);
            in.endArray();
        }
    }
}
