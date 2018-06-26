package com.heaven7.java.data.mediator;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.internal.ReflectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the group data manager. like mutex value with multi 'data-mediator'.
 *
 * @param <T> the data type.
 * @author heaven7
 * @since 1.4.5
 */
//@GroupProp(type = MUTEX, prop ="state", value=XXX, mutex=Oppo_val,flags = true)
public final class GroupDataManager<T>{

    public static final int TYPE_MUTEX = 1;

    // (key, value) = (type, GroupProperty)
    private final SparseArray<List<GroupProperty>> mPropMap = new SparseArray<>();
    private final State mState;

    private final DataMediatorCallback<T> mCallback = new DataMediatorCallback<T>() {
        @Override
        public void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue) {
            GroupDataManager.this.onPropertyValueChanged(data, prop, oldValue, newValue);
        }
    };
    @SuppressWarnings("unchecked")
    private GroupDataManager(Object group) {
        if(group instanceof List) {
            mState = new ListState((List<? extends MediatorDelegate<T>>) group);
        }else if(group instanceof SparseArray){
            mState = new SparseArrayState((SparseArray<? extends MediatorDelegate<T>>) group);
        }else if(group.getClass().isArray()){
            mState = new ArrayState((MediatorDelegate<T>[]) group);
        }else{
            throw new UnsupportedOperationException("unknown group type");
        }
    }
    /**
     * attach a group of 'mediator-delegate' to GroupDataManager.
     * @param array the group of 'mediator-delegate'
     * @param gps the group properties.
     * @param <T> the data type
     * @param <D> the mediator delegate type
     * @return the GroupDataManager.
     */
    public static <T, D extends MediatorDelegate<T>> GroupDataManager<T> of(D[] array, List<GroupProperty> gps) {
        GroupDataManager<T> gdm = new GroupDataManager<>(array);
        gdm.prepare(gps);
        return gdm;
    }
    /**
     * attach a group of 'mediator-delegate' to GroupDataManager.
     * @param delegateList the group of 'mediator-delegate'
     * @param gps the group properties.
     * @param <T> the data type
     * @param <D> the mediator delegate type
     * @return the GroupDataManager.
     */
    public static  <T, D extends MediatorDelegate<T>> GroupDataManager<T> of(List<D> delegateList, List<GroupProperty> gps) {
        GroupDataManager<T> gdm = new GroupDataManager<>(delegateList);
        gdm.prepare(gps);
        return gdm;
    }

    /**
     * attach a group of 'mediator-delegate' to GroupDataManager.
     * @param sa the group of 'mediator-delegate'
     * @param gps the group properties.
     * @param <T> the data type
     * @param <D> the mediator delegate type
     * @return the GroupDataManager.
     */
    public static  <T, D extends MediatorDelegate<T>> GroupDataManager<T> of(SparseArray<D> sa, List<GroupProperty> gps) {
        GroupDataManager<T> gdm = new GroupDataManager<>(sa);
        gdm.prepare(gps);
        return gdm;
    }

    public void attach(){
        mState.attach();
    }

    public void detach(){
        mState.detach();
    }

    private void prepare(List<GroupProperty> gps) {
         for(GroupProperty gp : gps){
             List<GroupProperty> list = mPropMap.get(gp.type);
             if(list == null){
                 list = new ArrayList<>();
                 mPropMap.put(gp.type, list);
             }
             list.add(gp);
         }
    }

    private void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue) {
        //currently only support int and long.
        if(prop.getType() != int.class && prop.getType() != long.class){
            return;
        }
        List<GroupProperty> list = mPropMap.get(TYPE_MUTEX);
        //process mutex
        if(!Predicates.isEmpty(list)){
            long value = Long.valueOf(newValue.toString());
            for(GroupProperty gp : list){
                if(gp.prop == prop && mutex(gp, data, value)){
                    break;
                }
            }
        }
    }

    //return true if mutex ok.
    private boolean mutex(GroupProperty target, T data, long value){
        if(target.asFlags){
            if((value & target.value) == target.value){
                //contains
                mState.doMutex(target, data);
                return true;
            }
        }else{
            if(value == target.value){
                mState.doMutex(target, data);
                return true;
            }
        }
        return false;
    }

    private void setMutexValue(GroupProperty target, MediatorDelegate<T> md) {
        DataMediator<T> mediator = md.getDataMediator();
        //before mutex we should just remove this callback for reduce unnecessary callback.
        mediator.removeDataMediatorCallback(mCallback);

        T proxy = mediator.getDataProxy();
        Object value = ReflectUtils.getValue(target.prop, proxy);
        setMutexValue0(target, proxy, value);
        //restore
        mediator.addDataMediatorCallback(mCallback);
    }

    private static void setMutexValue0(GroupProperty gp, Object obj, Object preVal){
        if(gp.prop.getType() == int.class){
            int newVal = gp.asFlags ? (Integer) preVal & ~(int)gp.value : (int)gp.oppositeValue;
            ReflectUtils.setValue(gp.prop, obj, newVal);
        }else if(gp.prop.getType() == long.class){
            long newVal = gp.asFlags ? (Long) preVal & ~gp.value : gp.oppositeValue;
            ReflectUtils.setValue(gp.prop, obj, newVal);
        }else{
            throw new UnsupportedOperationException();
        }
    }

    /**
     * the group property
     * @author heaven7
     */
    public static class GroupProperty {
        Property prop;
        /** the group type */
        int type;
        /** the focus value */
        long value;
        /** the opposite value of focus value. this is used by 'Mutex' */
        long oppositeValue;
        /** focused as flags or not */
        boolean asFlags;

        public Property getProperty() {
            return prop;
        }
        public void setProperty(Property prop) {
            this.prop = prop;
        }

        public int getType() {
            return type;
        }
        public void setType(int type) {
            this.type = type;
        }

        public long getValue() {
            return value;
        }
        public void setValue(long value) {
            this.value = value;
        }

        public boolean isAsFlags() {
            return asFlags;
        }
        public void setAsFlags(boolean asFlags) {
            this.asFlags = asFlags;
        }

        public long getOppositeValue() {
            return oppositeValue;
        }
        public void setOppositeValue(long oppositeValue) {
            this.oppositeValue = oppositeValue;
        }
    }

    public interface MediatorDelegate<T> {
        DataMediator<T> getDataMediator();
    }
    private interface State{
        void attach();
        void detach();
        void doMutex(GroupProperty target, Object data);
    }
    private class ArrayState implements State{

        private final MediatorDelegate<T>[] array;

        ArrayState(MediatorDelegate<T>[] array) {
            this.array = array;
        }

        @Override
        public void attach() {
            for (MediatorDelegate<T> delegate : array){
                delegate.getDataMediator().addDataMediatorCallback(mCallback);
            }
        }
        @Override
        public void detach() {
            for (MediatorDelegate<T> delegate : array){
                delegate.getDataMediator().removeDataMediatorCallback(mCallback);
            }
        }
        @Override
        public void doMutex(GroupProperty target, Object data) {
            for(MediatorDelegate<T> md : array){
                if(md.getDataMediator().getData() == data){
                    continue;
                }
                setMutexValue(target, md);
            }
        }
    }

    private class ListState implements State{
        private final List<? extends MediatorDelegate<T>> mList;
        ListState(List<? extends MediatorDelegate<T>> mList) {
            this.mList = mList;
        }
        @Override
        public void attach() {
            for (MediatorDelegate<T> delegate : mList){
                delegate.getDataMediator().addDataMediatorCallback(mCallback);
            }
        }
        @Override
        public void detach() {
            for (MediatorDelegate<T> delegate : mList){
                delegate.getDataMediator().removeDataMediatorCallback(mCallback);
            }
        }
        @Override
        public void doMutex(GroupProperty target, Object data) {
            for(MediatorDelegate<T> md : mList){
                if(md.getDataMediator().getData() == data){
                    continue;
                }
                setMutexValue(target, md);
            }
        }
    }

    private class SparseArrayState implements State{

        private final SparseArray<? extends MediatorDelegate<T>> mSa;

        SparseArrayState(SparseArray<? extends MediatorDelegate<T>> mSa) {
            this.mSa = mSa;
        }
        @Override
        public void attach() {
            int size = mSa.size();
            for (int i = size - 1; i >=0 ; i --){
                MediatorDelegate<T> delegate = mSa.get(mSa.keyAt(i));
                if(delegate != null){
                    delegate.getDataMediator().addDataMediatorCallback(mCallback);
                }
            }
        }
        @Override
        public void detach() {
            int size = mSa.size();
            for (int i = size - 1; i >=0 ; i --){
                MediatorDelegate<T> delegate = mSa.get(mSa.keyAt(i));
                if(delegate != null){
                    delegate.getDataMediator().removeDataMediatorCallback(mCallback);
                }
            }
        }
        @Override
        public void doMutex(GroupProperty target, Object data) {
            int size = mSa.size();
            for (int i = size - 1; i >=0 ; i --){
                MediatorDelegate<T> delegate = mSa.get(mSa.keyAt(i));
                if(delegate != null) {
                    if (delegate.getDataMediator().getData() == data) {
                        continue;
                    }
                    setMutexValue(target, delegate);
                }
            }
        }
    }
}
