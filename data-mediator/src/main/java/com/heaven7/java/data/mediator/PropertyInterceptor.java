package com.heaven7.java.data.mediator;

/**
 * the property interceptor.
 * <p>sometimes, we apply properties but don't want to apply invalid value. so just use this to judge.</p>
 *
 * @since 1.0.8
 */
public abstract class PropertyInterceptor {

    public static final PropertyInterceptor NEVER = new PropertyInterceptor() {
        @Override
        public boolean shouldIntercept(Object data, Property prop, Object value) {
            return false;
        }
    };
    public static final PropertyInterceptor NULL = new PropertyInterceptor() {
        @Override
        public boolean shouldIntercept(Object data, Property prop, Object value) {
            return value == null;
        }
    };
    public static final PropertyInterceptor NULL_AND_ZERO = new PropertyInterceptor() {
        @Override
        public boolean shouldIntercept(Object data, Property prop, Object value) {
            if(value == null){
                return true;
            }
            if(prop.isPrimitive()){
                if(value instanceof Float || value instanceof Double){
                    if(Double.valueOf(value.toString()) == 0d){
                        return true;
                    }
                }
                if(value instanceof Byte || value instanceof Short ||
                        value instanceof Integer || value instanceof Long ){
                    if(Long.valueOf(value.toString()) == 0){
                        return true;
                    }
                }
                //ignore boolean, char
            }
            return false;
        }
    };

    /**
     * should intercept this apply function or not.
     *
     * @param data  the data.
     * @param prop  the property
     * @param value the property value.
     * @return true if intercept.
     */
    public abstract boolean shouldIntercept(Object data, Property prop, Object value);
}
