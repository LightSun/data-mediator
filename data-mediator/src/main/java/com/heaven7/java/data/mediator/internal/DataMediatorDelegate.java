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
package com.heaven7.java.data.mediator.internal;

import com.heaven7.java.base.anno.CalledInternal;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.util.PlatformDependent;

/**
 * the all delegate in data-mediator frameworks.
 * Created by heaven7 on 2017/10/4.
 * @since 1.1.3
 */
public abstract class DataMediatorDelegate {

    private static DataMediatorDelegate sInstance;

    static {
        try {
            final Class<?> clazz;
            if(PlatformDependent.isAndroid()) {
                clazz = Class.forName("com.heaven7.android.data.mediator.DataMediatorDelegateImpl");
                sInstance = (DataMediatorDelegate) clazz.newInstance();
            }else{
                sInstance = new DataMediatorDelegateImpl();
            }
        } catch (Exception e) {
            //ignore
        }
    }
    protected DataMediatorDelegate(){}

    /**
     * get the delegate helper.
     * @return the data mediator delegate.
     */
    @CalledInternal
    public static DataMediatorDelegate getDefault(){
        if(sInstance == null){
            throw new IllegalStateException("init failed! ");
        }
        return sInstance;
    }

    /**
     * create binder by target data mediator.
     * @param <T> the module data type
     * @param mediator the data mediator.
     * @return the binder.
     */
    public abstract <T> Binder<T> createBinder(DataMediator<T> mediator);

    /**
     * get parcel delegate .
     * @param parcel the parcel object
     * @return the parcel delegate
     */
    public abstract ParcelDelegate getParcelDelegate(Object parcel);//called by compiler.

    /**
     * get sparse array delegate
     * @param sa the sparse array
     * @param <V> the value type
     * @return the sparse array type.
     */
    public <V> SparseArrayDelegate<V> getSparseArrayDelegate(final SparseArray<V> sa){
        return new JavaSparseArrayDelegate<V>(sa);
    }
}
