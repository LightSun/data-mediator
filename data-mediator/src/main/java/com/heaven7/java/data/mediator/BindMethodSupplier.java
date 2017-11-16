package com.heaven7.java.data.mediator;

import com.heaven7.java.base.util.SparseArray;

/**
 * the bind method supplier which used for {@linkplain DataBinding}.
 * supply the method name with it's parameter type .
 * this is often used for bind self method of any view.
 * this class is unlike the {@linkplain com.heaven7.java.data.mediator.DataBinding.ParameterSupplier}.
 * <br>Created by heaven7 on 2017/11/16.
 *
 * @since 1.4.3
 */
public abstract class BindMethodSupplier {

    private static final SparseArray<BindMethodSupplier> sSupplierMap;

    static {
        sSupplierMap = new SparseArray<>();
    }

    /**
     * get the bind method supplier for target class type.
     * the internal use share mode to share {@linkplain BindMethodSupplier}.
     * @param clazz the BindMethodSupplier class.
     * @return an instance of {@linkplain BindMethodSupplier}.
     */
    public static BindMethodSupplier get(Class<? extends BindMethodSupplier> clazz){
        BindMethodSupplier supplier = sSupplierMap.get(clazz.hashCode());
        if(supplier == null){
            try {
                supplier = clazz.newInstance();
                sSupplierMap.put(clazz.hashCode(), supplier);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return supplier;
    }

    /**
     * get the method name of {@linkplain Binder}.
     *
     * @return the method name of {@linkplain Binder}.
     */
    public abstract String getMethodName();

    /**
     * get the method parameter types. default is String and object types.
     *
     * @return the parameter types of {@linkplain Binder}.
     * @see Binder
     */
    public Class<?>[] getMethodParameterTypes() {
        return new Class<?>[]{ String.class, Object.class };
    }
}
