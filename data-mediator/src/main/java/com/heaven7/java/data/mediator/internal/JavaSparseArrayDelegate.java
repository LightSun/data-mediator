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
    public int put(int key, V value) {
        final V old = mMap.get(key);
        if(value == null){
            if(old == null){
                return STATE_NO_CHANGE;
            }else{
                mMap.put(key, null);
                return STATE_CHANGED;
            }
        }else{
            if(old == null){
                //new not null, old is null.
                mMap.put(key, value);
                return STATE_NEW;
            }else if (value.equals( old )){
                //new not null, old is not null. but equals.
                return STATE_NO_CHANGE;
            }else{
                mMap.put(key, value);
                return STATE_CHANGED;
            }
        }
    }

    @Override
    public V remove(int key) {
        return mMap.getAndRemove(key);
    }

    @Override
    public int removeByValue(V value) {
        final int index = mMap.indexOfValue(value, false);
        if(index < 0){
            return -1;
        }
        final int key = mMap.keyAt(index);
        mMap.removeAt(index);
        return key;
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
    public boolean clearTo(SparseArrayDelegate<V> out) {
        final int size = mMap.size();
        if(size == 0){
            return false;
        }
        if(out != null) {
            for (int i = size -1 ; i>=0 ; i--){
                out.put(mMap.keyAt(i), mMap.valueAt(i));
            }
        }
        mMap.clear();
        return true;
    }

    @Override
    public int indexOfValue(V value) {
        return mMap.indexOfValue(value ,false);
    }

    @Override
    public V get(int key) {
        return mMap.get(key);
    }
}
