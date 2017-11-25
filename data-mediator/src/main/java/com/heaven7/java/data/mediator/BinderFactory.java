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
 * the binder factory which is used to create binder.<br>
 * Created by heaven7 on 2017/11/5.
 * @author heaven7
 * @since 1.4.0
 */
public interface BinderFactory {

    /**
     * create binder for target object and data mediator.
     * @param <T> the module data type
     * @param target the target object which use 'data-binding'.
     * @param dm the data mediator.
     * @return the binder which is used for data-binding. return null means use default binder.
     */
    <T> Binder<T> createBinder(Object target, DataMediator<T>  dm);
}
