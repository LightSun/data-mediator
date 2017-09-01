package com.heaven7.java.data.mediator;

import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.ExecutableElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.heaven7.java.data.mediator.MediatorUtils.hasFlag;

/**
 * type interface filler for impl
 * Created by heaven7 on 2017/9/1 0001.
 */
public abstract class TypeInterfaceFiller {

    /**
     * get the interface full name.
     * @return the interface name
     */
    protected abstract String getInterfaceName();

    /**
     * get interface flag.
     * @return the inter face flag.
     */
    protected abstract int getInterfaceFlag();

    /**
     * fill the {@link FieldData} to the map. which is used to generate code for interface.
     * @param fd the fieldData
     * @param map the map.
     */
    public void fill(FieldData fd, HashMap<String, List<FieldData>> map){
        final String interfaceName = getInterfaceName();
        if(hasFlag(fd.getFlags(), getInterfaceFlag())){
            List<FieldData> data = map.get(interfaceName);
            if(data == null){
                data = new ArrayList<>();
                map.put(interfaceName, data);
            }
            data.add(fd);
        }
    }

    /**
     * add method statement for builder with list of FieldData which has this filler's flags.
     * @param curPkg  current package name of generate java file.
     * @param curClassName  current simple class name of generate java file.
     * @param parentInterfaceName  the parent of current class which will generate java file.
     * @param ee the execute element.
     * @param builder the method builder.
     * @param list the FieldData list
     */
    public abstract void buildMethodStatement(String curPkg, String parentInterfaceName, String  curClassName,
                                              ExecutableElement  ee, MethodSpec.Builder builder, List<FieldData> list);
}