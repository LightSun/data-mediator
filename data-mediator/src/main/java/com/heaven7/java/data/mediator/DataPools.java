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

import com.heaven7.java.base.anno.VisibleForTest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import static com.heaven7.java.data.mediator.DataMediatorFactory.SUFFIX_IMPL;
import static com.heaven7.java.data.mediator.DataMediatorFactory.SUFFIX_INTERFACE;
/**
 * this class just called by framework internal.(include compiler.)
 * Created by heaven7 on 2017/9/27 0027.
 * @since 1.1.0
 */
public final class DataPools {

    /**
     * indicate the data can be put to pool or not.
     */
    public interface Poolable{
        /**
         * clear all properties for data without callback.
         * auto called before put to pool.
         */
        void clearProperties();

        /**
         * recycle this object. after call this you should not call any method for data .
         */
        void recycle();
    }

    /**
     * recycle the module data.
     * @param moduleData the module data.
     */
    public static void recycle(Object moduleData){
        final String name = moduleData.getClass().getName();
        if(!name.endsWith(SUFFIX_IMPL)){
            throw new IllegalArgumentException();
        }
        final String interfaceName = name.substring(0, name.lastIndexOf(SUFFIX_IMPL));
        if(!interfaceName.endsWith(SUFFIX_INTERFACE)){
            throw new IllegalArgumentException("only support data-mediator module interface.");
        }
        Entry entry = sMap.get(interfaceName);
        if(entry != null){
            entry.recycle(moduleData);
        }
    }

    /**
     * prepare the pool for target class. this method can only call once. or else throw UnsupportedOperationException.
     * @param fullClassName the impl class name which is generate by data-mediator-compiler.
     * @param maxCount the max count of the type pool.
     */
    public static void preparePool(String fullClassName, int maxCount){
        final String interfaceName;
        if(fullClassName.endsWith(SUFFIX_INTERFACE)){
            interfaceName = fullClassName;
        }else {
            if (!fullClassName.endsWith(SUFFIX_IMPL)) {
                throw new IllegalArgumentException();
            }
            interfaceName = fullClassName.substring(0, fullClassName.lastIndexOf(SUFFIX_IMPL));
            if(!interfaceName.endsWith(SUFFIX_INTERFACE)){
                throw new IllegalArgumentException("only support data-mediator module interface.");
            }
        }
        Entry entry = sMap.get(interfaceName);
        if(entry == null){
            sMap.put(interfaceName , new Entry(maxCount));
        }else{
            throw new UnsupportedOperationException("can't prepare pool more than once.");
        }
    }

    /* package */ static <T> T obtain(Class<T> clazz){
        final String name = clazz.getName();
        if(!name.endsWith(SUFFIX_INTERFACE)){
            throw new IllegalArgumentException();
        }
        Entry entry = sMap.get(name);
        final Class<?> impl;
        try {
            impl = Class.forName(name + SUFFIX_IMPL);
            if(entry != null ){
                T result =  (T) entry.obtain(impl);
                if(result != null){
                    return result;
                }
            }
            return (T) sFactory.create(impl);
        }catch (Exception e){
            throw new UnsupportedOperationException("can't find impl class ("+ (name + SUFFIX_IMPL) +")" ,e);
        }
    }

    /*@VisibleForTest
    public static int size(Class<?> interClazz){
        return sMap.get(interClazz.getName()).mQueue.size();
    }*/

    @VisibleForTest
    private static final Map<String, Entry> sMap = new HashMap<>();

    private static final Factory sFactory = new Factory() {
        @Override
        public Object create(Class<?> clazz) throws Exception {
            return clazz.newInstance();
        }
    };
    private static class Entry{

        final ArrayBlockingQueue<Object> mQueue;

        Entry(int mMaxCount) {
            mQueue = new ArrayBlockingQueue<>(mMaxCount);
        }

        boolean recycle(Object data){
            if(!(data instanceof Poolable)){
                throw new RuntimeException("data must impl Poolable");
            }
            if (!mQueue.contains(data)) {
                ((Poolable) data).clearProperties();
                return mQueue.offer(data);
            }
            return false;
        }
        Object obtain(Class<?> clazz) throws Exception {
            return mQueue.poll();
        }
    }
    private interface Factory{
        Object create(Class<?> clazz) throws Exception;
    }
}
