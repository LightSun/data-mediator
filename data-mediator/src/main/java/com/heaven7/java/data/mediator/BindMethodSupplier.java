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

/**
 * the bind method supplier which used for {@linkplain DataBinding}.
 * supply the method parameter types .
 * this is often used for bind self method of any view.
 * this class is unlike the {@linkplain com.heaven7.java.data.mediator.DataBinding.ParameterSupplier}.
 * Here is demo to use annotation.
 * <code><pre>
 *     {@literal @}BindMethodSupplierClass(BindMethodSupplier.DefaultBindMethodSupplier2.class)
        public class MockActivity extends AppCompatActivity {
        }
 * </pre></code>
 * <br>Created by heaven7 on 2017/11/16.
 *
 * @since 1.4.3
 */
public interface BindMethodSupplier {

    /** bind method type const: indicate default two parameters  */
    Class<?>[] TYPES2 =  { String.class, Object.class };
    /** bind method  type const: indicate default three parameters  */
    Class<?>[] TYPES3 =  { String.class, Object.class, Object.class };
    /** bind method type const: indicate default four parameters  */
    Class<?>[] TYPES4 =  { String.class, Object.class, Object.class, Object.class };
    /** bind method type const: indicate default five parameters  */
    Class<?>[] TYPES5 =  { String.class, Object.class, Object.class, Object.class, Object.class };
    /** bind method type const: indicate default six parameters  */
    Class<?>[] TYPES6 =  { String.class, Object.class, Object.class, Object.class, Object.class, Object.class };

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
            return TYPES2;
        }
    }
    /**
     * the default bind method supplier with three parameters.
     * @since 1.4.3
     */
    class DefaultBindMethodSupplier3 implements BindMethodSupplier{
        @Override
        public Class<?>[] getMethodParameterTypes(Object view, String property, String bindMethodName) {
            return TYPES3;
        }
    }
    /**
     * the default bind method supplier with four parameters.
     * @since 1.4.3
     */
    class DefaultBindMethodSupplier4 implements BindMethodSupplier{
        @Override
        public Class<?>[] getMethodParameterTypes(Object view, String property, String bindMethodName) {
            return TYPES4;
        }
    }
    /**
     * the default bind method supplier with five parameters.
     * @since 1.4.3
     */
    class DefaultBindMethodSupplier5 implements BindMethodSupplier{
        @Override
        public Class<?>[] getMethodParameterTypes(Object view, String property, String bindMethodName) {
            return TYPES5;
        }
    }
    /**
     * the default bind method supplier with six parameters.
     * @since 1.4.3
     */
    class DefaultBindMethodSupplier6 implements BindMethodSupplier{
        @Override
        public Class<?>[] getMethodParameterTypes(Object view, String property, String bindMethodName) {
            return TYPES6;
        }
    }
}
