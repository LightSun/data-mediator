package com.heaven7.android.data.mediator.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.adapter.AdapterManager;
import com.heaven7.adapter.HeaderFooterHelper;
import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataBinding;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.PropertyInterceptor;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * the data-binding base adapter of recycler view.</br>
 * Created by heaven7 on 2017/11/9 0009.
 *
 * @see DataBinding
 * @see DataMediatorFactory#createDataBinding(Object)
 * @since 1.1.2
 */

public abstract class DataBindingRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements AdapterManager.IHeaderFooterManager, ItemManager.Callback {

    private static final String TAG = "DB_Adapter";
    private final SparseArray<Binder<T>> mBinderMap = new SparseArray<>();
    private final ItemManager<T> mItemManager;

    private DataBinding.SimpleParameterSupplier mSupplier;
    private HeaderFooterHelper mHeaderFooterHelper;

    /**
     * create data-binding adapter for target data.
     *
     * @param list the list data to show in {@linkplain RecyclerView}.
     * @see #DataBindingRecyclerAdapter(List, boolean)
     */
    public DataBindingRecyclerAdapter(List<T> list) {
        this(list, false);
    }

    /**
     * create data-binding adapter for target data.
     *
     * @param list               the list data to show in {@linkplain RecyclerView}.
     * @param mayRemoveOrAddItem true if you may want to remove/add item. false otherwise.
     *                           this is help of data-binding, if you really want to remove/add item.
     */
    public DataBindingRecyclerAdapter(List<T> list, boolean mayRemoveOrAddItem) {
        this.mItemManager = new ItemManager<T>(this, this, list);
        if (mayRemoveOrAddItem) {
            mItemManager.setAdapterDataObserver2(new InternalDataObserver());
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final HeaderFooterHelper hfHelper = this.mHeaderFooterHelper;
        if (hfHelper == null || hfHelper.isLayoutIdInRecord(viewType)) {
            DataBindingViewHolder<T> holder = onCreateViewHolderImpl(parent, viewType);
            holder.setAdapter(this);
            return holder;
        } else {
            return new RecyclerView.ViewHolder(hfHelper.findView(viewType, mItemManager.getItemSize())){};
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mHeaderFooterHelper != null) {
            if (mHeaderFooterHelper.isInHeader(position)
                    || mHeaderFooterHelper.isInFooter(position, mItemManager.getItemSize())) {
                /*
                 * let head/footer full span in StaggeredGridLayoutManager
                 */
                ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
                if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
                }
                return;
            }
            position -= mHeaderFooterHelper.getHeaderViewSize();
        }
        if (holder instanceof DataBindingViewHolder) {
            Logger.i(TAG, "onBindViewHolder", "pos = " + position);
            ((DataBindingViewHolder) holder).onBindData(position, getParameterSupplier());
        }
    }

    @Override
    public final int getItemViewType(int position) {
        if (mHeaderFooterHelper != null) {
            //in header or footer
            if (mHeaderFooterHelper.isInHeader(position) ||
                    mHeaderFooterHelper.isInFooter(position, mItemManager.getItemSize()))
                return position;

            position -= mHeaderFooterHelper.getHeaderViewSize();
        }
        int layoutId = getItemLayoutId(position, getItem(position));
        if (mHeaderFooterHelper != null) {
            mHeaderFooterHelper.recordLayoutId(layoutId);
        }
        return layoutId;
    }

    @Override
    public final int getItemCount() {
        return mHeaderFooterHelper == null ? mItemManager.getItemSize() :
                mItemManager.getItemSize() + mHeaderFooterHelper.getHeaderViewSize() +
                        mHeaderFooterHelper.getFooterViewSize();
    }

    public final T getItem(int position) {
        return mItemManager.getItem(position);
    }
    //=========================== private method =============================

    private DataBinding.SimpleParameterSupplier getParameterSupplier() {
        return mSupplier != null ? mSupplier : (mSupplier = onCreateParameterSupplier());
    }

    /**
     * get the binder by target real position.(include header and footer)
     *
     * @param position the position, include header and footer
     * @return the binder
     */
    private Binder<T> getBinder(int position) {
        return mBinderMap.get(position - getHeaderSize());
    }
    // ========================== end private method ======================

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof DataBindingViewHolder) {
            ((DataBindingViewHolder) holder).onDetachItem(
                    holder.getLayoutPosition() - getHeaderSize());
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof DataBindingViewHolder) {
            ((DataBindingViewHolder) holder).onAttachItem(
                    holder.getAdapterPosition() - getHeaderSize());
        }
    }

    //=================== start header footer view ======================= //
    @Override
    public void addHeaderView(View v) {
        if (mHeaderFooterHelper == null)
            mHeaderFooterHelper = new HeaderFooterHelper();
        int headerSize = getHeaderSize();
        mHeaderFooterHelper.addHeaderView(v);
        notifyItemInserted(headerSize);
    }

    @Override
    public void removeHeaderView(View v) {
        if (mHeaderFooterHelper != null) {
            int index = mHeaderFooterHelper.removeHeaderView(v);
            if (index != -1) {
                notifyItemRemoved(index);
            }
        }
    }

    @Override
    public void addFooterView(View v) {
        if (mHeaderFooterHelper == null)
            mHeaderFooterHelper = new HeaderFooterHelper();
        int itemCount = getItemCount();
        mHeaderFooterHelper.addFooterView(v);
        notifyItemInserted(itemCount);
    }

    @Override
    public void removeFooterView(View v) {
        if (mHeaderFooterHelper != null) {
            int index = mHeaderFooterHelper.removeFooterView(v);
            if (index != -1) {
                notifyItemRemoved(index + getHeaderSize() + mItemManager.getItemSize());
            }
        }
    }

    @Override
    public int getHeaderSize() {
        return mHeaderFooterHelper == null ? 0 : mHeaderFooterHelper.getHeaderViewSize();
    }

    @Override
    public int getFooterSize() {
        return mHeaderFooterHelper == null ? 0 : mHeaderFooterHelper.getFooterViewSize();
    }

    @Override
    public boolean isHeader(int position) {
        return mHeaderFooterHelper != null && mHeaderFooterHelper.isInHeader(position);
    }

    @Override
    public boolean isFooter(int position) {
        return mHeaderFooterHelper != null && mHeaderFooterHelper.isInFooter(
                position, mItemManager.getItemSize());
    }

    // =================== end header footer view ======================= //

    /**
     * get the item manager of adapter.which implements {@linkplain com.heaven7.java.data.mediator.BaseListPropertyCallback.IItemManager}.
     *
     * @return the item manager.
     */
    public final ItemManager<T> getItemManager() {
        return mItemManager;
    }

    /**
     * called when we want to create parameter supplier for {@linkplain DataBinding}.
     * rg: when we want to  bind image url to {@linkplain android.widget.ImageView}.
     * the internal implements use {@linkplain Binder#bindImageUrl(String, Object, Object)} to bind image.
     * so we need additional parameter named 'the image loader'. if you not use self {@link Binder}.
     * the default is {@linkplain com.heaven7.android.data.mediator.AndroidBinder}.
     * <p>How to define self Binder? extends {@linkplain Binder}. optional use
     * self {@linkplain com.heaven7.java.data.mediator.BinderFactory}, The double can be defined by
     * annotation '@BinderClass' and @BinderFactory.</p>
     *
     * @return the parameter supplier.
     * @see DataBinding#bind(Object, int, DataBinding.ParameterSupplier, PropertyInterceptor)
     */
    protected DataBinding.SimpleParameterSupplier onCreateParameterSupplier() {
        return null;
    }

    /**
     * get the item layout id for target position and data.
     *
     * @param position the position
     * @param t        the data
     * @return the layout id.
     */
    protected abstract @LayoutRes int getItemLayoutId(int position, T t);

    /**
     * called on create view holder. header and footer will not be called in this.
     *
     * @param parent   the parent view
     * @param layoutId the item layout id.
     * @return the data-binding ViewHolder.
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     * @see DataBindingViewHolder
     */
    protected abstract DataBindingViewHolder<T> onCreateViewHolderImpl(ViewGroup parent, int layoutId);

    private class InternalDataObserver implements AdapterDataObserver2<T> {
        @Override
        public void onItemRemoved(int index, T t) {
            Binder<T> binder = mBinderMap.getAndRemove(index);
            if (binder != null) {
                binder.unbindAll();
            }
            //decrease position/index of binder. iterate from lower.
            final int size = mBinderMap.size();
            for (int i = 0; i < size; i++) {
                final int key = mBinderMap.keyAt(i);
                if (key <= index) {
                    continue;
                }
                final Binder<T> val = mBinderMap.valueAt(i);
                mBinderMap.put(key - 1, val);
               /* Logger.d(TAG, "onItemRemoved",
                        String.format("pos from %d to %d", key, key - 1));*/
            }
        }

        @Override
        public void onResetItems(List<T> items) {
            for (int size = mBinderMap.size(), i = size - 1; i >= 0; i--) {
                final Binder<T> binder = mBinderMap.getAndRemove(mBinderMap.keyAt(i));
                if (binder != null) {
                    binder.unbindAll();
                }
            }
        }

        @Override
        public void onAddItem(int index, T item) {
            processAddForBinder(index, 1);
        }

        @Override
        public void onAddItems(int index, List<T> list) {
            processAddForBinder(index, list.size());
        }

        void processAddForBinder(int index ,int addSize){
            //iterate from larger
            for (int size = mBinderMap.size() , i = size - 1; i >= 0; i--) {
                final int key = mBinderMap.keyAt(i);
                if (key < index) {
                    continue;
                }
                final Binder<T> val = mBinderMap.valueAt(i);
                //move from key -> key +1
                mBinderMap.put(key + addSize, val);
               /* Logger.d(TAG, "processAddForBinder",
                        String.format("pos from %d to %d", key, key + addSize));*/
            }
            //for add . the old index of binder already exist. need remove.
            mBinderMap.remove(index);
        }
    }


    /**
     * the data-binding view holder, which is used by {@linkplain DataBinding}.
     *
     * @param <T> the data module if item.
     * @author heaven7
     * @since 1.1.2
     */
    public abstract static class DataBindingViewHolder<T> extends RecyclerView.ViewHolder {

        private final DataBinding<DataBindingViewHolder<T>> mDataBinding;
        private WeakReference<DataBindingRecyclerAdapter<T>> mWeakAdapter;

        /**
         * create data-binding view holder for target item view.
         *
         * @param itemView the item view
         */
        public DataBindingViewHolder(View itemView) {
            super(itemView);
            onPreCreateDataBinding(itemView);
            mDataBinding = DataMediatorFactory.createDataBinding(this);
        }

        //=================== start public or protected methods ================

        /**
         * get the data mediator for current position.
         *
         * @return the data mediator.
         */
        public final DataMediator<T> getDataMediator() {
            DataBindingRecyclerAdapter<T> adapter = getAdapter();
            if (adapter == null) {
                return null;
            }
            int pos = getAdapterPosition();
          /*  Logger.i(TAG,"getDataMediator","pos = "
                    + pos + " , layoutPos = " + getLayoutPosition() + " ,oldPos = " + getOldPosition());*/
            return adapter.getBinder(pos).getDataMediator();
        }

        /**
         * get the data proxy of current position.
         *
         * @return the proxy data
         */
        public final T getDataProxy() {
            final DataMediator<T> dm = getDataMediator();
            return dm != null ? dm.getDataProxy() : null;
        }
        /**
         * get the data of current position.
         *
         * @return the proxy data
         */
        public final T getData() {
            final DataMediator<T> dm = getDataMediator();
            return dm != null ? dm.getData() : null;
        }

        /**
         * get the data-binding adapter.
         *
         * @return the data-binding adapter.
         * @see DataBindingRecyclerAdapter
         */
        public final DataBindingRecyclerAdapter<T> getAdapter() {
            return mWeakAdapter != null ? mWeakAdapter.get() : null;
        }

        /**
         * called on bind item data.
         *
         * @param position the position (reject headers and footers)
         * @param supplier the supplier, which is used to data-binding. this is from
         *                 {@linkplain DataBindingRecyclerAdapter#onCreateParameterSupplier()}.
         * @see DataBinding
         * @see DataBinding#bind(Object, int, DataBinding.ParameterSupplier, PropertyInterceptor)
         */
        @CallSuper
        protected void onBindData(int position, @Nullable DataBinding.SimpleParameterSupplier supplier) {
            DataBindingRecyclerAdapter<T> adapter = getAdapter();
            if (adapter == null) {
                return;
            }
            Binder<T> binder = adapter.mBinderMap.get(position);
            if (binder != null) {
                binder.unbindAll();
            }
            adapter.mBinderMap.put(position, mDataBinding.bindAndApply(
                    adapter.getItem(position), 0, supplier, getPropertyInterceptor()));
        }

        /**
         * get the property interceptor which is used to data-binding.
         *
         * @return the property interceptor. can be null.
         * @see PropertyInterceptor
         * @see DataBinding#bind(Object, int, DataBinding.ParameterSupplier, PropertyInterceptor)
         */
        protected @Nullable PropertyInterceptor getPropertyInterceptor() {
            return PropertyInterceptor.NULL;
        }

        /**
         * called on attach the item of target position.
         * @param position the position of adapter .exclude header and footer
         */
        protected void onAttachItem(int position) {

        }

        /**
         * called on detach the item of target position.
         * @param position  the position of adapter .exclude header and footer
         */
        protected void onDetachItem(int position) {

        }

        /**
         * called on pre create data-binding. this often used by 'ButterKnife', like :
         * <pre>
         *     ButterKnife.bind(this, itemView);
         * </pre>
         *
         * @param itemView the item view of holder
         */
        protected void onPreCreateDataBinding(View itemView) {

        }
        //==================== end public/protected methods ================


        //================= START private/default method ==================
        void setAdapter(DataBindingRecyclerAdapter<T> adapter) {
            this.mWeakAdapter = new WeakReference<>(adapter);
        }
    }
}
