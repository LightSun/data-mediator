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

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.GlobalSetting;

import java.util.List;

import static com.heaven7.java.data.mediator.FieldFlags.*;

/**
 * the gson support.
 * @author heaven7
 * @since 1.0.1
 */
public class GsonSupport {

    private static final String SUFFIX_IMPL = "_Impl";

    /**
     * convert the target object to json. currently support simple object, array , list. SparseArray
     * @param t the object. must have annotation {@linkplain JsonAdapter}
     * @return the json string.
     * @see SparseArray
     */
    public static String toJson(Object t) {
        String ImplName = t.getClass().getName();
        if (!ImplName.endsWith(SUFFIX_IMPL)) {
            throw new IllegalArgumentException("target object(" + ImplName + ") can't convert to json!");
        }
        GsonBuilder builder = new GsonBuilder()
                .setVersion(GlobalSetting.getDefault().getCurrentVersion());

        if(t instanceof SparseArray){
            SparseArray sa = (SparseArray) t;
            if(sa.size() == 0){
                return "{}";
            }
            Class<?> clazz = sa.valueAt(0).getClass();
          /*  builder.registerTypeAdapter(
                    TypeToken.getParameterized(SparseArray.class, clazz).getType(),
                    new SparseArrayTypeAdapter<>(TypeHandler.getTypeAdapter(clazz)));*/
            builder.registerTypeAdapter(SparseArray.class,
                    new SparseArrayTypeAdapter<>(TypeHandler.getTypeAdapter(clazz)));
        }
        return builder.create().toJson(t);
    }

    /**
     * convert json to array.
     * @param json the json.
     * @param clazz the item class
     * @param <T> the module data type, can be interface or impl which is generate by data-mediator.
     * @return the array of target class.
     */
    public static <T> T[] fromJsonToArray(String json, Class<T> clazz){
        return (T[]) fromJson(json, clazz, COMPLEX_ARRAY);
    }

    /**
     * convert json to list.
     * @param json the json
     * @param clazz the item class.
     * @param <T> the data type. can be interface or impl which is generate by data-mediator.
     * @return the list of target class.
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz){
        return (List<T>) fromJson(json, clazz, COMPLEX_LIST);
    }
    /**
     * convert json to sparse array.
     * @param json the json
     * @param clazz the value class.
     * @param <T> the data type. can be interface or impl which is generate by data-mediator.
     * @return the list of target class.
     */
    public static <T> SparseArray<T> fromJsonToSparseArray(String json, Class<T> clazz){
        return (SparseArray<T>) fromJson(json, clazz, COMPLEX_SPARSE_ARRAY);
    }

    /**
     * convert json string to simple object.
     * @param json the json string
     * @param clazz the class.
     * @param <T> the data type , can be interface or impl which is generate by data-mediator.
     * @return a simple object
     */
    public static <T> T fromJson(String json, Class<T> clazz){
        return (T) fromJson(json, clazz, 0);
    }

    private static Object fromJson(String json, Class<?> clazz, int complexType) {
        GsonBuilder builder = new GsonBuilder()
                .setVersion(GlobalSetting.getDefault().getCurrentVersion());

        if(clazz.isInterface()){
            try {
                clazz = Class.forName(DataMediatorFactory.getImplClassName(clazz));
            } catch (ClassNotFoundException e) {
                throw new UnsupportedOperationException(e);
            }
        }
        switch (complexType){
            case COMPLEX_SPARSE_ARRAY:
                builder.registerTypeAdapter(SparseArray.class,
                        new SparseArrayTypeAdapter<>(TypeHandler.getTypeAdapter(clazz)));
                return builder.create().fromJson(json, SparseArray.class);

            case COMPLEX_ARRAY:
                return builder.create().fromJson(json, TypeToken.getArray(clazz).getType());

            case COMPLEX_LIST:
                return builder.create().fromJson(json, TypeToken.getParameterized(List.class, clazz).getType());

            case 0:
                return builder.create().fromJson(json, clazz);

            default:
                throw new UnsupportedOperationException("this complexType not support now !");
        }
    }
}
