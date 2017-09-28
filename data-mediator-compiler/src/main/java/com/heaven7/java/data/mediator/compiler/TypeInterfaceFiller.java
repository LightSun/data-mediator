package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.ExecutableElement;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.heaven7.java.data.mediator.compiler.Util.hasFlag;

/**
 * type interface fillers for impl
 * often used to fill  fields. constructors, methods for impl class.
 * not for interface.
 * Created by heaven7 on 2017/9/1 0001.
 */
public abstract class TypeInterfaceFiller {

    private WeakReference<ProcessorPrinter> mWeakPrinter;

    public void setLogPrinter(ProcessorPrinter pp) {
        mWeakPrinter = new WeakReference<ProcessorPrinter>(pp);
    }

    public ProcessorPrinter getLogPrinter() {
        return mWeakPrinter != null ? mWeakPrinter.get() : null;
    }

    protected void note(String method, Object... objs) {
        final ProcessorPrinter printer = getLogPrinter();
        if (printer != null) {
            printer.note(getClass().getSimpleName(), method, objs);
        }
    }

    public void reset(){

    }

    /**
     * get the interface full name.
     *
     * @return the interface name
     */
    public abstract String getInterfaceName();

    /**
     * get interface flag. or 0 if not use/focued
     *
     * @return the inter face flag.
     */
    public abstract int getInterfaceFlag();

    /**
     * fill the {@link FieldData} to the map. which is used to generate code for interface.
     *
     * @param fd  the fieldData
     * @param map the map.
     * @return true if fill success.
     */
    public boolean fill(FieldData fd, Map<String, List<FieldData>> map) {
        final String interfaceName = getInterfaceName();
        // note("fd.flags = " + fd.getFlags() + " ,interface flag = " + getInterfaceFlag());
        if (hasFlag(fd.getFlags(), getInterfaceFlag())) {
            //    note("has flag: " + interfaceName);
            List<FieldData> data = map.get(interfaceName);
            if (data == null) {
                data = new ArrayList<>();
                map.put(interfaceName, data);
            }
            data.add(fd);
            return true;
        }
        return false;
    }

    /**
     * add method statement for builder with list of FieldData which has this fillers's flags.
     * @param curPkg              current package name of generate java file.
     * @param parentInterfaceName the parent of current class which will generate java file.
     * @param curClassName        current simple class name of generate java file.
     * @param ee                  the execute element. often is the method of interface.
     * @param builder             the method builder.
     * @param list                the FieldData list
     * @param hasSuperClass       true if have super class.
     * @param superInterfaceFlagsForParent the super interface flags for parent.
     */
    public abstract void buildMethodStatement(String curPkg, String parentInterfaceName, String curClassName,
                                              ExecutableElement ee, MethodSpec.Builder builder,
                       List<FieldData> list, boolean hasSuperClass, int superInterfaceFlagsForParent);

    /**
     *  create field builder. if you want. eg: android.os.Parcelable
     * @param pkgName the package name.
     * @param interName the simple interface name.
     * @param classname the simple class name of generate java file
     * @param datas the field datas.
     * @param superInterfaceFlagsForParent the super interface flags
     * @return the field data array.
     */
    public FieldSpec.Builder[] createFieldBuilder(String pkgName, String interName,
                                                  String classname, List<FieldData> datas, int superInterfaceFlagsForParent) {
        return null;
    }

    /**
     * create special constructor  builder.
     * @param pkgName the package name.
     * @param interName the interface name
     * @param classname the classname
     * @param datas the field datas.
     * @param hasSuperClass true if have super class.
     * @param superFlagsForParent super interface flags for parent.
     * @return the constructor builders.
     */
    public MethodSpec.Builder[] createConstructBuilder(String pkgName, String interName, String classname,
                       List<FieldData> datas, boolean hasSuperClass, int superFlagsForParent) {
        return null;
    }

    /**
     * build super methods for proxy. (override method from super.)
     * @param builder the method builder.
     * @param ee the method element
     * @param cn_interface the class name of interface. may copy need.
     */
    public abstract void buildProxyMethod(MethodSpec.Builder builder, ExecutableElement ee, ClassName cn_interface);
}
