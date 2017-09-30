package com.heaven7.java.data.mediator;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Throwables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * the mQueue property editor.
 * @param <D> the type module data.
 * @param <T> the parameter type of mQueue property.
 * Created by heaven7 on 2017/9/23.
 * @since 1.0.8
 */
public class ListPropertyEditor<D, T> {

    private final List<T> mList;
    private List<T> mTemp;

    private final Property mProperty;
    private final D mData;
    private final BaseMediator<D> mMediator;
    /**
     * create mQueue property editor.
     * @param data the module data
     * @param currentValues the current mQueue property values. can't be null(may be used by callback).
     * @param property the property to edit, can be nul
     * @param mediator the mediator of module. can be null.
     */
    public ListPropertyEditor(D data, List<T> currentValues,
                              @Nullable Property property,
                              @Nullable BaseMediator<D> mediator) {
        Throwables.checkNull(data);
        Throwables.checkNull(currentValues);
        this.mList = currentValues;
        this.mData = data;
        this.mProperty = property;
        this.mMediator = mediator;
    }

    /**
     * add a value to the property.
     * @param t the value data.
     * @return this
     */
    public ListPropertyEditor<D, T> add(T t){
        Throwables.checkNull(t);
        //dispatch if need.
        if(mList.add(t) && mMediator != null){
            ensureTempNotNull();
            mTemp.add(t);
            mMediator.dispatchAddValues(mProperty, mList, mTemp);
            mTemp.clear();
        }
        return this;
    }

    /**
     * add a value to the target index.
     * @param index the index
     * @param t the value data.
     * @return this
     */
    public ListPropertyEditor<D, T> add(int index ,T t){
        Throwables.checkNull(t);
        mList.add(index, t);
        //dispatch if need.
        if(mMediator != null){
            ensureTempNotNull();
            mTemp.add(t);
            mMediator.dispatchAddValuesWithIndex(mProperty, mList, mTemp, index);
            mTemp.clear();
        }
        return this;
    }

    /**
     * set item on target index.
     * @param index the index to set
     * @param newT the new item to set .
     * @return this
     * @since 1.1.2
     */
    public ListPropertyEditor<D, T> set(int index ,T newT){
        Throwables.checkNegativeValue(index);
        Throwables.checkNull(newT);
        if(mList.size() <= index){
            throw new IllegalArgumentException();
        }
        T old = mList.set(index, newT);
        //dispatch if need.
        if (mMediator != null) {
            mMediator.dispatchItemChanged(mProperty, old, newT, index);
        }
        return this;
    }

    /**
     * add the all value data which is indicate by target collection.
     * @param index the index
     * @param collection the collection
     * @return this                 \
     */
    public ListPropertyEditor<D, T> addAll(int index , Collection<? extends T> collection){
        Throwables.checkNull(collection);
        if(!collection.isEmpty() && mList.addAll(index, collection)) {
            //dispatch if need.
            if (mMediator != null) {
                mMediator.dispatchAddValuesWithIndex(mProperty, mList, collection, index);
            }
        }
        return this;
    }
    /**
     * add the all value data which is indicate by target collection.
     * @param collection the collection
     * @return this
     */
    public ListPropertyEditor<D, T> addAll(Collection<? extends T> collection){
        Throwables.checkNull(collection);
        if(!collection.isEmpty() && mList.addAll(collection)) {
            //dispatch if need.
            if (mMediator != null) {
                mMediator.dispatchAddValues(mProperty, mList, collection);
            }
        }
        return this;
    }

    /**
     * remove the value data from collection.
     * @param t the value data
     * @return this
     */
    public ListPropertyEditor<D, T> remove(T t){
        Throwables.checkNull(t);
        if(mList.remove(t)){
            if(mMediator != null) {
                ensureTempNotNull();
                mTemp.add(t);
                mMediator.dispatchRemoveValues(mProperty, mList, mTemp);
                mTemp.clear();
            }
        }
        return this;
    }
    /**
     * remove the index data from collection.
     * @param index the index data
     * @return this
     */
    public ListPropertyEditor<D, T> remove(int index){
        Throwables.checkNegativeValue(index);
        if(mList.size()> index){
            T t = mList.remove(index);
            if(mMediator != null && t != null) {
                ensureTempNotNull();
                mTemp.add(t);
                mMediator.dispatchRemoveValues(mProperty, mList, mTemp);
                mTemp.clear();
            }
        }
        return this;
    }
    /**
     * remove the all value data which is indicate by target collection.
     * @param collection the collection
     * @return this
     */
    public ListPropertyEditor<D, T> removeAll(Collection<? extends T> collection){
        Throwables.checkNull(collection);
        if(!collection.isEmpty() && mList.removeAll(collection)) {
            //dispatch if need.
            if(mMediator != null){
                mMediator.dispatchRemoveValues(mProperty, mList, collection);
            }
        }
        return this;
    }

    /**
     * remove the all value data.
     * @return this
     */
    public ListPropertyEditor<D, T> clearAll(){
        if(!mList.isEmpty()) {
            //dispatch if need.
            if(mMediator != null){
                ensureTempNotNull();
                mTemp.addAll(mList);
                mList.clear();
                mMediator.dispatchRemoveValues(mProperty, mList, mTemp);
                mTemp.clear();
            }else{
                mList.clear();
            }
        }
        return this;
    }

    /**
     * end the editor and return to the module type object.
     * @return the module type object.
     */
    public D end(){
        //in generate code . BaseMediator often impl the module interface.
        if(mMediator != null){
            return (D) mMediator;
        }
        return mData;
    }

    private void ensureTempNotNull(){
        if (mTemp == null) {
            mTemp = new ArrayList<T>();
        }
    }
}
