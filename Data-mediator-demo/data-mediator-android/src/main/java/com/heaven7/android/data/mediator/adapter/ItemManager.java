package com.heaven7.android.data.mediator.adapter;

import com.heaven7.adapter.AdapterManager;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.data.mediator.BaseListPropertyCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * the item manager
 *
 * @author heaven7
 * @since 1.1.2
 */
public class ItemManager<T> implements BaseListPropertyCallback.IItemManager<T> {

    /**
     * the  threshold percent of remove.
     */
    private static final float PERCENT_THRESHOLD = 0.3f;
    private static final Comparator<Integer> INT_INVERSE =  new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return ItemManager.compare(o2, o1);
        }
    };

    private final Callback mCallback;
    private final List<T> mDatas;
    private final AdapterManager.IHeaderFooterManager mManager;
    private AdapterDataObserver2<T> mObserver;

    /*public*/ ItemManager(Callback callback, AdapterManager.IHeaderFooterManager ifm, List<T> list) {
        Throwables.checkNull(callback);
        this.mManager = ifm;
        this.mCallback = callback;
        this.mDatas = list != null ? new ArrayList<T>(list) : new ArrayList<T>();
    }

    /**
     * set adapter data remove observer.
     * @param observer the adapter dara removed observer.
     */
    void setAdapterDataObserver2(AdapterDataObserver2<T> observer) {
        this.mObserver = observer;
    }

    /**
     * get the items size.
     * @return the items size.
     */
    public int getItemSize() {
        return mDatas.size();
    }

    /**
     * get the items
     * @return the items
     */
    public List<T> getItems() {
        return mDatas;
    }

    /**
     * add a item
     * @param index the index to add. exclude header and footer.
     * @param item the item to add
     */
    public void addItem(int index, T item) {
        mDatas.add(index, item);
        if(mObserver != null){
            mObserver.onAddItem(index, item);
        }
        mCallback.notifyItemInserted(index + getHeaderSize());
    }

    /**
     * add a item
     * @param item the item to add
     */
    public void addItem(T item) {
        mDatas.add(item);
        final int index = mDatas.size() - 1;
        if(mObserver != null){
            mObserver.onAddItem(index, item);
        }
        mCallback.notifyItemInserted(index + getHeaderSize());
    }

    /**
     * remove the target index of item
     * @param index the index to remove,  exclude header and footer.
     */
    public void removeItemAt(int index) {
        T t;
        if ((t = mDatas.remove(index)) != null) {
            if (mObserver != null) {
                mObserver.onItemRemoved(index, t);
            }
            mCallback.notifyItemRemoved(index + getHeaderSize());
        }
    }

    /**
     * remove item.
     * @param t the item
     */
    public void removeItem(T t) {
        if (t != null) {
            int index = mDatas.indexOf(t);
            if (index < 0) {
                return;
            }
            mDatas.remove(index);
            if (mObserver != null) {
                mObserver.onItemRemoved(index, t);
            }
            mCallback.notifyItemRemoved(index + getHeaderSize());
        }
    }

    /**
     * get the item for target position
     * @param position the position,  exclude header and footer.
     * @return the item
     */
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public void addItems(List<T> list) {
        final int size = mDatas.size();//1
        mDatas.addAll(list);
        if(mObserver != null){
            mObserver.onAddItems(size, list);
        }
        mCallback.notifyItemRangeInserted(size + getHeaderSize(), list.size());
    }

    /**
     * {@inheritDoc }
     * @param index the index.  exclude header and footer.
     * @param list the list data to add.
     */
    @Override
    public void addItems(int index, List<T> list) {
        mDatas.addAll(index, list);
        if(mObserver != null){
            mObserver.onAddItems(index, list);
        }
        mCallback.notifyItemRangeInserted(index + getHeaderSize(), list.size());
    }

    @Override
    public void removeItems(List<T> list) {
        final int changeSize = list.size();
        final int currSize = mDatas.size();
        final int threshold = (int) (PERCENT_THRESHOLD * currSize);
        if(currSize <= 3 || changeSize > threshold){
            mDatas.removeAll(list);
            if(mObserver != null){
                mObserver.onResetItems(list);
            }
            mCallback.notifyDataSetChanged();
        }else{
            //collect indexes
            final List<Integer> indexList = new ArrayList<>();
            for (T t : list) {
                int index = mDatas.indexOf(t);
                if (index >= 0) {
                    indexList.add(index);
                }
            }
            //sort inverse
            Collections.sort(indexList, INT_INVERSE);
            //remove from higher to lower
            for(int index : indexList){
                T t = mDatas.remove(index);
                if (mObserver != null) {
                    mObserver.onItemRemoved(index, t);
                }
                mCallback.notifyItemRemoved(index + getHeaderSize());
            }
        }
    }

    @Override
    public void replaceItems(List<T> list) {
        mDatas.clear();
        if (list != null) {
            mDatas.addAll(list);
        }
        if(mObserver != null){
            mObserver.onResetItems(list);
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
    private static int compare(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    /**
     * the item manager callback same as the {@linkplain android.support.v7.widget.RecyclerView.Adapter}.
     *
     * @author heaven7
     * @since 1.1.2
     * @see android.support.v7.widget.RecyclerView.Adapter
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
