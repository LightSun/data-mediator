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
package com.heaven7.android.data.mediator;

import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.IBinderSupplier;
/**
 * the binder supplier. this call is called by reflect. must not change package name and class name.
 * Created by heaven7 on 2017/9/24.
 */
public class BinderSupplierImpl<T> implements IBinderSupplier<T> {
    @Override
    public Binder<T> create(DataMediator<T> mediator) {
        return new AndroidBinder<T>(mediator);
    }
}
