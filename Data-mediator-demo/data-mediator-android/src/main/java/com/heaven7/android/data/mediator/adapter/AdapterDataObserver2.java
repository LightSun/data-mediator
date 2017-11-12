package com.heaven7.android.data.mediator.adapter;

import java.util.List;

/**
 * adapter data observer. which will help we handle 'data-binding' in adapter.
 *
 * @param <T> the item type
 * @author heaven7
 * @since 1.1.2
 */
interface AdapterDataObserver2<T> {

    /**
     * called on remove item
     * @param index the index of item
     * @param t the item data
     */
    void onItemRemoved(int index, T t);

    /**
     * called on reset items.
     * @param items the items which is reset previous items
     */
    void onResetItems(List<T> items);

    /**
     * called on add item
     * @param index the index
     * @param item the item to add
     */
    void onAddItem(int index, T item);

    /**
     * called on add items
     * @param index the index
     * @param list the items to add.
     */
    void onAddItems(int index, List<T> list);
}
