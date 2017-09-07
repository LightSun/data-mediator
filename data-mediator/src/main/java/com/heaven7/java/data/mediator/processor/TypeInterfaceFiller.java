package com.heaven7.java.data.mediator.processor;

import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.ExecutableElement;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.heaven7.java.data.mediator.MediatorUtils.hasFlag;

/**
 * type interface filler for impl
 * Created by heaven7 on 2017/9/1 0001.
 */
public abstract class TypeInterfaceFiller {

    private WeakReference<ProcessorPrinter> mWeakPrinter;

    public void setLogPrinter(ProcessorPrinter pp){
        mWeakPrinter = new WeakReference<ProcessorPrinter>(pp);
    }
    public ProcessorPrinter getLogPrinter(){
        return mWeakPrinter != null ? mWeakPrinter.get() : null;
    }

    protected void note(Object...objs){
        final ProcessorPrinter printer = getLogPrinter();
        if(printer != null){
            printer.note(getClass().getSimpleName() + " >>> ", objs);
        }
    }
    /**
     * get the interface full name.
     * @return the interface name
     */
    public abstract String getInterfaceName();

    /**
     * get interface flag.
     * @return the inter face flag.
     */
    public abstract int getInterfaceFlag();

    /**
     * fill the {@link FieldData} to the map. which is used to generate code for interface.
     * @param fd the fieldData
     * @param map the map.
     * @return true if fill success.
     */
    public boolean fill(FieldData fd, Map<String, List<FieldData>> map){
        final String interfaceName = getInterfaceName();
       // note("fd.flags = " + fd.getFlags() + " ,interface flag = " + getInterfaceFlag());
        if(hasFlag(fd.getFlags(), getInterfaceFlag())){
        //    note("has flag: " + interfaceName);
            List<FieldData> data = map.get(interfaceName);
            if(data == null){
                data = new ArrayList<>();
                map.put(interfaceName, data);
            }
            data.add(fd);
            return true;
        }
        return false;
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
