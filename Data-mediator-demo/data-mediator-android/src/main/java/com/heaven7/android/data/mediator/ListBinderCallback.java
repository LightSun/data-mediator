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

import com.heaven7.java.data.mediator.BaseListPropertyCallback;
import com.heaven7.java.data.mediator.Binder;

/**
 * <p>Use {@linkplain BaseListPropertyCallback} instead.</p>
 * the list binder callback
 * @param <T> the item module data type.
 * @author heaven7
 */
@Deprecated
public class ListBinderCallback<T> extends BaseListPropertyCallback<T> implements Binder.BinderCallback<Object>{

    /**
     * create list binder callback with item manager.
     * @param mCallback the item manager.
     */
    public ListBinderCallback(ListBinderCallback.IItemManager<T> mCallback) {
        super(mCallback);
    }

    @Override
    public Object getTag() {
        return getItemManager();
    }

    /**
     * <p>Use {@linkplain BaseListPropertyCallback.IItemManager} instead.</p>
     * the item manager.
     * @param <T> the item data type
     */
    @Deprecated
    public interface IItemManager<T> extends BaseListPropertyCallback.IItemManager<T>{
    }

}
