package com.heaven7.java.data.mediator;

/**
 * the bind method supplier which used for {@linkplain DataBinding}.
 * supply the method parameter types .
 * this is often used for bind self method of any view.
 * this class is unlike the {@linkplain com.heaven7.java.data.mediator.DataBinding.ParameterSupplier}.
 * <br>Created by heaven7 on 2017/11/16.
 *
 * @since 1.4.3
 */
public interface BindMethodSupplier {

    /**
     * get the bind method parameter types which are using by 'Data-Binding'.
     * @param view the view object which will be bound by 'data-binding'.
     * @param property the property of data-module
     * @param bindMethodName the bind method name of {@linkplain Binder}.
     * @return the method parameter types.
     */
    Class<?>[] getMethodParameterTypes(Object view, String property, String bindMethodName);

    /**
     * the default bind method supplier with two parameters.
     * @since 1.4.3
     */
    class DefaultBindMethodSupplier2 implements BindMethodSupplier{
        @Override
        public Class<?>[] getMethodParameterTypes(Object view, String property, String bindMethodName) {
            return new Class<?>[]{ String.class, Object.class };
        }
    }
    /**
     * the default bind method supplier with three parameters.
     * @since 1.4.3
     */
    class DefaultBindMethodSupplier3 implements BindMethodSupplier{
        @Override
        public Class<?>[] getMethodParameterTypes(Object view, String property, String bindMethodName) {
            return new Class<?>[]{ String.class, Object.class, Object.class};
        }
    }
    /**
     * the default bind method supplier with four parameters.
     * @since 1.4.3
     */
    class DefaultBindMethodSupplier4 implements BindMethodSupplier{
        @Override
        public Class<?>[] getMethodParameterTypes(Object view, String property, String bindMethodName) {
            return new Class<?>[]{ String.class, Object.class, Object.class, Object.class};
        }
    }
    /**
     * the default bind method supplier with five parameters.
     * @since 1.4.3
     */
    class DefaultBindMethodSupplier5 implements BindMethodSupplier{
        @Override
        public Class<?>[] getMethodParameterTypes(Object view, String property, String bindMethodName) {
            return new Class<?>[]{ String.class, Object.class, Object.class, Object.class, Object.class};
        }
    }
    /**
     * the default bind method supplier with six parameters.
     * @since 1.4.3
     */
    class DefaultBindMethodSupplier6 implements BindMethodSupplier{
        @Override
        public Class<?>[] getMethodParameterTypes(Object view, String property, String bindMethodName) {
            return new Class<?>[]{ String.class, Object.class, Object.class, Object.class, Object.class, Object.class};
        }
    }
}
