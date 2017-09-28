package com.heaven7.java.data.mediator.compiler.insert;

import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.replacer.TargetClassInfo;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by heaven7 on 2017/9/28 0028.
 */
public final class InsertManager {

    private static final ArrayList<IInterfaceInsertDelegate> sInserts;

    static {
        sInserts = new ArrayList<>(4);
        sInserts.add(new PoolableInsertDelegate());
    }

    public static void setClassInfo(TargetClassInfo info){
        for (IInterfaceInsertDelegate delegate : sInserts){
            delegate.setClassInfo(info);
        }
    }

    /**
     * add static code
     * @param typeBuilder the class builder
     * @param param the extra param
     */
    public static void addStaticCode(TypeSpec.Builder typeBuilder, Object param){
        for (IInterfaceInsertDelegate delegate : sInserts){
            delegate.addStaticCode(typeBuilder, param);
        }
    }

    /**
     * add constructor code
     * @param typeBuilder the class builder
     * @param fields the all fields use in constructor
     */
    public static void addConstructor(TypeSpec.Builder typeBuilder, Collection<FieldData> fields){
        for (IInterfaceInsertDelegate delegate : sInserts){
            delegate.addConstructor(typeBuilder, fields);
        }
    }

    /**
     * add super interface
     * @param typeBuilder the type builder
     */
    public static void addSuperInterface(TypeSpec.Builder typeBuilder){
        for (IInterfaceInsertDelegate delegate : sInserts){
            delegate.addSuperInterface(typeBuilder);
        }
    }
    /**
     * override method for proxy class
     * @param typeBuilder the type builder
     * @param fields the fields
     */
    public static void overrideMethodsForProxy(TypeSpec.Builder typeBuilder, Collection<FieldData> fields){
        for (IInterfaceInsertDelegate delegate : sInserts){
            delegate.overrideMethodsForProxy(typeBuilder, fields);
        }
    }
    /**
     * override method for impl class
     * @param typeBuilder the type builder
     * @param fields the fields
     */
    public static void overrideMethodsForImpl(TypeSpec.Builder typeBuilder, Collection<FieldData> fields){
        for (IInterfaceInsertDelegate delegate : sInserts){
            delegate.overrideMethodsForImpl(typeBuilder, fields);
        }
    }

}
