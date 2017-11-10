package com.heaven7.android.data.mediator.adapter;

import com.heaven7.adapter.AdapterManager;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.data.mediator.BaseListPropertyCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * the item manager
 *
 * @author heaven7
 * @since 1.1.2
 */
public class ItemManager<T> implements BaseListPropertyCallback.IItemManager<T> {

    private final Callback mCallback;
    private final List<T> mDatas;
    private AdapterManager.IHeaderFooterManager mManager;

    public ItemManager(Callback callback, AdapterManager.IHeaderFooterManager ifm, List<T> list) {
        Throwables.checkNull(callback);
        this.mManager = ifm;
        this.mCallback = callback;
        this.mDatas = list != null ? new ArrayList<T>(list) : new ArrayList<T>();
    }

    public int getItemSize() {
        return mDatas.size();
    }

    public List<T> getItems() {
        return mDatas;
    }

    public void addItem(T item) {
        mDatas.add(item);
        mCallback.notifyItemInserted(mDatas.size() - 1);
    }

    public void removeItemAt(int index) {
        if (mDatas.remove(index) != null) {
            mCallback.notifyItemRemoved(index + getHeaderSize());
        }
    }

    public void removeItem(T t) {
        if (t != null) {
            int index = mDatas.indexOf(t);
            mDatas.remove(index);
            mCallback.notifyItemRemoved(index + getHeaderSize());
        }
    }

    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public void addItems(List<T> list) {
        final int size = mDatas.size();//1
        mDatas.addAll(list);
        mCallback.notifyItemRangeInserted(size + getHeaderSize(), list.size());
    }

    @Override
    public void addItems(int index, List<T> list) {
        mDatas.addAll(list);
        mCallback.notifyItemRangeInserted(index + getHeaderSize(), list.size());
    }

    @Override
    public void removeItems(List<T> list) {
        if (list.size() == 1) {
            int index = mDatas.indexOf(list.get(0));
            if (index >= 0) {
                mDatas.remove(index);
                mCallback.notifyItemRemoved(index + getHeaderSize());
            }
        } else {
            mDatas.removeAll(list);
            mCallback.notifyDataSetChanged();
        }
    }

    @Override
    public void replaceItems(List<T> list) {
        mDatas.clear();
        if (list != null) {
            mDatas.addAll(list);
        }
        mCallback.notifyDataSetChanged();
    }

    @Override
    public void onItemChanged(int index, T oldT, T newT) {
        mDatas.set(index, newT);
        mCallback.notifyItemChanged(index + getHeaderSize());
    }

    private int getHeaderSize() {
        return mManager.getHeaderSize();
    }

    private int getFooterSize() {
        return mManager.getFooterSize();
    }

    /**
     * the item manager callback.
     *
     * @author heaven7
     * @since 1.1.2
     */
    public interface Callback {

        void notifyItemInserted(int position);

        void notifyItemChanged(int position);

        void notifyItemRemoved(int position);

        void notifyItemMoved(int fromPosition, int toPosition);

        void notifyItemRangeChanged(int positionStart, int itemCount);

        void notifyItemRangeInserted(int positionStart, int itemCount);

        void notifyItemRangeRemoved(int positionStart, int itemCount);

        void notifyDataSetChanged();
    }
}