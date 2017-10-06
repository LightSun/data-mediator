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

import com.heaven7.java.base.util.SparseArray;

/**
 * the sparse array on java .
 * @param <V> the value type.
 * @author heaven7
 */
/*public*/ class JavaSparseArrayDelegate<V> implements SparseArrayDelegate<V> {

    private final SparseArray<V> mMap;

    public JavaSparseArrayDelegate(SparseArray<V> mMap) {
        this.mMap = mMap;
    }

    @Override
    public V put(int key, V value) {
        final V old = mMap.get(key);
        mMap.put(key, value);
        return old;
    }

    @Override
    public V remove(int key) {
        return mMap.getAndRemove(key);
    }

    @Override
    public int size() {
        return mMap.size();
    }

    @Override
    public int keyAt(int index) {
        return mMap.keyAt(index);
    }

    @Override
    public V valueAt(int index) {
        return mMap.valueAt(index);
    }

    @Override
    public void removeAt(int index) {
        mMap.removeAt(index);
    }

    @Override
    public void setValueAt(int index, V value) {
        mMap.setValueAt(index, value);
    }

    @Override
    public void clear() {
        mMap.clear();
    }

    @Override
    public int indexOfValue(V value) {
        return mMap.indexOfValue(value ,false);
    }
}
