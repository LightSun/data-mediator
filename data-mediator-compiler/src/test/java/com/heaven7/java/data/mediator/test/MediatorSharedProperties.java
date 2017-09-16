package com.heaven7.java.data.mediator.test;

import com.heaven7.java.data.mediator.Property;

import java.util.HashMap;

/**
 * Created by heaven7 on 2017/9/14 0014.
 */
public class MediatorSharedProperties {

    private static final HashMap<String, Property> sCache;

    static{
        sCache = new HashMap<>();
        putToCache("java.lang.String", "name", 0);
        putToCache("xxx", "xxx", 0);
    }

    public static Property get(String typeName, String propName, int complexFlag){
        return sCache.get(generateKey(typeName, propName, complexFlag));
    }

    private static void putToCache(String typeName, String propName, int complexFlag){
        sCache.put(generateKey(typeName, propName, complexFlag), new Property(typeName, propName, complexFlag));
    }
    private static String generateKey(String typeName, String propName, int complexFlag){
        return typeName.hashCode() + "_" + propName + "_" + complexFlag;
    }

}
