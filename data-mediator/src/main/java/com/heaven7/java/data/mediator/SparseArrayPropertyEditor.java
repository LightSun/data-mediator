package com.heaven7.java.data.mediator;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.base.util.Throwables;

/**
 * the sparse array editor
 * Created by heaven7 on 2017/10/3.
 * @since 1.1.3
 */
/*public*/ final class SparseArrayPropertyEditor<D, V> {

    private final SparseArray<V> mMap;
    private final Property mProperty;
    private final D mData;
    private final BaseMediator<D> mMediator;

    /**
     * create list property editor.
     * @param data the module data
     * @param map the current map of property value..
     * @param property the property to edit, can be nul
     * @param mediator the mediator of module. can be null.
     */
    public SparseArrayPropertyEditor(D data, SparseArray<V> map,
                              @Nullable Property property,
                              @Nullable BaseMediator<D> mediator) {
        Throwables.checkNull(data);
        Throwables.checkNull(map);
        this.mMap = map;
        this.mData = data;
        this.mProperty = property;
        this.mMediator = mediator;
    }

    public SparseArrayPropertyEditor<D, V> put(int key, V value){
         mMap.put(key, value);
         return this;
    }
}
