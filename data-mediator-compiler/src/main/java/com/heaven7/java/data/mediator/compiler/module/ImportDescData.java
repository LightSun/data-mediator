package com.heaven7.java.data.mediator.compiler.module;

import java.util.HashMap;
import java.util.List;

/**
 * @since 1.4.5
 */
public class ImportDescData {

    private final HashMap<String, String> map = new HashMap<>();

    public static ImportDescData of(List<String> names, List<String> cns){
        ImportDescData  idd = new ImportDescData();
        for(int i = 0, size = names.size() ; i < size ; i ++){
            idd.map.put(names.get(i), cns.get(i));
        }
        return idd;
    }

    public HashMap<String, String> getImportDatas(){
        return map;
    }
}
