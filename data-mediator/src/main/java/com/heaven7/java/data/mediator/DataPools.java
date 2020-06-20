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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.heaven7.java.data.mediator.DataMediatorFactory.SUFFIX_IMPL;
import static com.heaven7.java.data.mediator.DataMediatorFactory.getImplClassName;
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
        final String interfaceName ;
        if (fullClassName.endsWith(SUFFIX_IMPL)) {
            interfaceName = fullClassName.substring(0, fullClassName.lastIndexOf(SUFFIX_IMPL));
        }else{
            interfaceName = fullClassName;
        }
        Entry entry = sMap.get(interfaceName);
        if(entry == null){
            sMap.put(interfaceName , new Entry(interfaceName, maxCount));
        }else{
            throw new UnsupportedOperationException("can't prepare pool more than once.");
        }
    }

    /* package */ static <T> T obtain(Class<T> clazz){
        final String name = clazz.getName();
        Entry entry = sMap.get(clazz.getName());
        final Class<?> impl;
        try {
            if(entry != null ){
                T result =  (T) entry.obtain();
                if(result != null){
                    return result;
                }
            }
            impl = Class.forName(getImplClassName(clazz));
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

        final ArrayList<Object> mQueue;
        final int maxSize;
        final String classname;
        final ReadWriteLock mLock = new ReentrantReadWriteLock();

        Entry(String className, int mMaxCount) {
            this.maxSize = mMaxCount;
            this.classname = className;
            this.mQueue = new ArrayList<>();
        }

        boolean recycle(Object data){
            if(!(data instanceof Poolable)){
                throw new RuntimeException("data must impl Poolable");
            }
            final boolean full;
            mLock.readLock().lock();
            try {
                full = mQueue.size() >= maxSize;
            }finally {
                mLock.readLock().unlock();
            }
            if(!full){
                mLock.writeLock().lock();
                try {
                    if (!mQueue.contains(data)) {
                        ((Poolable) data).clearProperties();
                        return mQueue.add(data);
                    }
                }finally {
                    mLock.writeLock().unlock();
                }
            }
            return false;
        }
        Object obtain(){
            final boolean empty;
            mLock.readLock().lock();
            try {
                empty = mQueue.size() == 0;
            }finally {
                mLock.readLock().unlock();
            }
            if(empty){
                try {
                    return sFactory.create(Class.forName(classname));
                } catch (Exception e) {
                    throw new UnsupportedOperationException("can't find impl class ("+ (classname) +")" ,e);
                }
            }
            mLock.writeLock().lock();
            try {
                return mQueue.remove(0);
            }finally {
                mLock.writeLock().unlock();
            }
        }
    }
    private interface Factory{
        Object create(Class<?> clazz) throws Exception;
    }
}
