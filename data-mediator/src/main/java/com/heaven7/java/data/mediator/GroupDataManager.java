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
    private SparseArray<? extends MediatorDelegate<T>> mSparseArr;
    private List<? extends MediatorDelegate<T>> mList;

    private final DataMediatorCallback<T> mCallback = new DataMediatorCallback<T>() {
        @Override
        public void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue) {
            GroupDataManager.this.onPropertyValueChanged(data, prop, oldValue, newValue);
        }
    };

    private GroupDataManager() {
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
        return of(Arrays.asList(array), gps);
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
        GroupDataManager<T> gdm = new GroupDataManager<>();
        gdm.mList = delegateList;
        for(MediatorDelegate<T> md : delegateList){
             md.getDataMediator().addDataMediatorCallback(gdm.mCallback);
        }
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
        GroupDataManager<T> gdm = new GroupDataManager<>();
        gdm.mSparseArr = sa;
        int size = sa.size();
        for (int i = size - 1; i >=0 ; i --){
            MediatorDelegate<T> delegate = sa.get(sa.keyAt(i));
            if(delegate != null){
                delegate.getDataMediator().addDataMediatorCallback(gdm.mCallback);
            }
        }
        gdm.prepare(gps);
        return gdm;
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
                doMutex(target, data);
                return true;
            }
        }else{
            if(value == target.value){
                doMutex(target, data);
                return true;
            }
        }
        return false;
    }

    private void doMutex(GroupProperty target, T data) {
         if(!Predicates.isEmpty(mList)){
             for(MediatorDelegate<T> md : mList){
                 if(md.getDataMediator().getData() == data){
                     continue;
                 }
                 setMutexValue(target, md);
             }
         }else if(mSparseArr != null){
             int size = mSparseArr.size();
             for (int i = size - 1; i >=0 ; i --){
                 MediatorDelegate<T> delegate = mSparseArr.get(mSparseArr.keyAt(i));
                 if(delegate.getDataMediator().getData() == data){
                     continue;
                 }
                 setMutexValue(target, delegate);
             }
         }
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
}
