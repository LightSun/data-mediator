/**
 * Copyright 2017
 group of data-mediator
 member: heaven7(donshine723@gmail.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.heaven7.java.data.mediator;

import java.util.Arrays;
import java.util.List;

/**
 * the property interceptor.
 * <p>sometimes, we apply properties but don't want to apply invalid value. so just use this to judge.</p>
 *
 * @since 1.0.8
 */
public abstract class PropertyInterceptor {

    /**
     * a interceptor that never intercept.
     */
    public static final PropertyInterceptor NEVER = new PropertyInterceptor() {
        @Override
        public boolean shouldIntercept(Object data, Property prop, Object value) {
            return false;
        }
    };
    /**
     * a interceptor when value is null. it will intercept.
     */
    public static final PropertyInterceptor NULL = new PropertyInterceptor() {
        @Override
        public boolean shouldIntercept(Object data, Property prop, Object value) {
            return value == null;
        }
    };
    /**
     * a interceptor when value is null or 0. it will intercept.
     */
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
     * create filter property interceptor.
     * @param accepts the property to accept. can't be null
     * @return an instance of PropertyInterceptor.
     * @since 1.1.2
     */
   public static PropertyInterceptor createFilter(Property... accepts){
       return new FilterPropertyInterceptor(accepts);
   }

    /**
     * should intercept this apply function or not.
     *
     * @param data  the data.
     * @param prop  the property
     * @param value the property value.
     * @return true if intercept.
     */
    public abstract boolean shouldIntercept(Object data, Property prop, Object value);

    /**
     * @since 1.1.2
     */
    private static class FilterPropertyInterceptor extends PropertyInterceptor{

        private final List<Property> accepts;

        public FilterPropertyInterceptor(Property[] accepts) {
            this.accepts = Arrays.asList(accepts);
        }

        @Override
        public boolean shouldIntercept(Object data, Property prop, Object value) {
            return !accepts.contains(prop);
        }
    }
}
