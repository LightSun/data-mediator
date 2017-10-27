/**
 * Copyright 2017
 * group of data-mediator
 * member: heaven7(donshine723@gmail.com)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heaven7.java.data.mediator.internal;

import com.heaven7.java.data.mediator.Property;

import java.util.HashMap;

/**
 * this class help we share properties.
 * @author heaven7
 * @since 1.2.1
 */
public final class SharedProperties {
    private static final HashMap<String, Property> sCache;

    static {
        sCache = new HashMap<>();
        /* remove in 1.3.0
        putToCache("boolean", "selected", 0);
        try {
            for (int i = 1; i < 100; i++) {
                Class.forName("com.heaven7.java.data.mediator.internal.SharedProperties" + "_" + i);
            }
        } catch (Exception e) {
            //ignore
        }*/
    }

    /**
     *  get the property from target parameters.
     * @param typeName the type name
     * @param propName the property name
     * @param complexFlag the complex flag
     * @return an instance of {@linkplain Property}.
     */
    public static Property get(String typeName, String propName, int complexFlag) {
        /*
         * problem: previously, i generate some class (SharedProperties_N) to auto register property.
         *         it has a bug. may generate some file.
         *         ( only on java project main/test module not multi module , android project its' ok)
         * so just lazy load .
         * since 1.3.0.
         */
        final String key = generateKey(typeName, propName, complexFlag);
        Property property = sCache.get(key);
        if(property == null){
            property = new Property(typeName, propName, complexFlag);
            sCache.put(key, property);
        }
        return property;
    }

    /**
     * put the parameter as {@linkplain Property} to put to the cache,
     * @param typeName the type name
     * @param propName the property name
     * @param complexFlag the complex flag
     */
    public static void putToCache(String typeName, String propName, int complexFlag) {
        sCache.put(generateKey(typeName, propName, complexFlag),
                new Property(typeName, propName, complexFlag));
    }

    private static String generateKey(String typeName, String propName, int complexFlag) {
        return typeName.hashCode() + "_" + propName + "_" + complexFlag;
    }
}
