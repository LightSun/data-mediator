package com.heaven7.android.data.mediator;

import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.Property;

import java.util.List;

/**
 * the list binder callback
 * @param <T> the item module data type.
 * @author heaven7
 */
public class ListBinderCallback<T> extends Binder.SimpleBinderCallback<Object> {

    private final IItemManager<T> mCallback;

    /**
     * create list binder callback with item manager.
     * @param mCallback the item manager.
     */
    public ListBinderCallback(IItemManager<T> mCallback) {
        Throwables.checkNull(mCallback);
        this.mCallback = mCallback;
    }

    @Override
    public Object getTag() {
        return mCallback;
    }

    @Override
    public void onAddPropertyValues(Object data, Property prop, Object newValue, Object addedValue) {
        List<T> items = (List<T>) addedValue;
        mCallback.addItems(items);
    }
    @Override
    public void onAddPropertyValuesWithIndex(Object data, Property prop, Object newValue,
                                             Object addedValue, int index) {
        List<T> items = (List<T>) addedValue;
        mCallback.addItems(index, items);
    }
    @Override
    public void onRemovePropertyValues(Object data, Property prop, Object newValue, Object removeValue) {
        List<T> items = (List<T>) removeValue;
        mCallback.removeItems(items);
    }
    @Override
    public void onPropertyValueChanged(Object data, Property prop, Object oldValue, Object newValue) {
        if(newValue != null) {
            List<T> items = (List<T>) newValue;
            if(!items.isEmpty()) {
                mCallback.replaceItems(items);
            }
        }
    }

    /**
     * the item manager.
     * @param <T> the item data type
     */
    public interface IItemManager<T>{

        /**
         * called on add items. should call notify in this when use in adapter.
         * @param items the items to add.
         */
        void addItems(List<T> items);

        /**
         * called on add items. should call notify in this when use in adapter.
         * @param index the start index to add
         * @param items the items to add
         */
        void addItems(int index ,List<T> items);

        /**
         * called on remove items.should call notify in this when use in adapter.
         * @param items the items
         */
        void removeItems(List<T> items);

        /**
         * called on replace items.should call notify in this when use in adapter.
         * @param items the items.
         */
        void replaceItems(List<T> items);
    }

}
