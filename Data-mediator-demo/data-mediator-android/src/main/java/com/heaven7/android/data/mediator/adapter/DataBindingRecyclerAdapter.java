package com.heaven7.android.data.mediator.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.adapter.AdapterManager;
import com.heaven7.adapter.HeaderFooterHelper;
import com.heaven7.core.util.Logger;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataBinding;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.PropertyInterceptor;

import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

/**
 * the data-binding adapter of recycler view.
 * Created by heaven7 on 2017/11/9 0009.
 *
 * @see DataBinding
 * @see DataMediatorFactory#createDataBinding(Object)
 * @since 1.1.2
 */

public abstract class DataBindingRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements AdapterManager.IHeaderFooterManager, ItemManager.Callback {

    private final ItemManager<T> mItemManager;
    private final TreeMap<T, Binder<T>> mBinderMap = new TreeMap<>(new Comparator<T>() {
        @Override
        public int compare(T o1, T o2) {
            return o1.equals(o2)? 0 : -1;
        }
    });

    private DataBinding.SimpleParameterSupplier mSupplier;
    private HeaderFooterHelper mHeaderFooterHelper;


    public DataBindingRecyclerAdapter(List<T> list) {
        this.mItemManager = new ItemManager<T>(this, this, list);
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                Logger.w("DataBindingViewHolder", "onItemRangeRemoved", "itemCount = " + itemCount);
                final int headerSize = getHeaderSize();
                /*for (int i = itemCount - 1; i >= 0; i--) {
                    Binder<T> binder = removeBinder(positionStart + i  - headerSize);
                    if(binder != null){
                        binder.unbindAll();
                    }
                }*/
            }
        });
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final HeaderFooterHelper hfHelper = this.mHeaderFooterHelper;
        if (hfHelper == null || hfHelper.isLayoutIdInRecord(viewType)) {
            DataBindingViewHolder<T> holder = onCreateViewHolderImpl(parent, viewType);
            holder.setAdapter(this);
            return holder;
        } else {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    viewType, parent, false)) {
            };
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
            Logger.i("DataBindingRecyclerAdapter","onBindViewHolder","position = " + position);
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
     * get the binder by target real position.(exclude header and footer)
     * @param position the position
     * @return the binder
     */
    private Binder<T> getBinder(int position) {//TODO header
        T item = getItem(position);
        Logger.i("DataBindingRecyclerAdapter","getBinder",
                "position = " + position + ", item = " + item);
        return mBinderMap.get(item);
    }
    private Binder<T> removeBinder(int position) {
        return mBinderMap.remove(getItem(position));
    }

    // ========================== end private method ======================

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof DataBindingViewHolder) {
            Logger.i("DataBindingRecyclerAdapter", "onViewDetachedFromWindow", "pos = "
                    + holder.getAdapterPosition());
            ((DataBindingViewHolder) holder).onDetachItem();
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof DataBindingViewHolder) {
            ((DataBindingViewHolder) holder).onAttachItem();
            Logger.i("DataBindingRecyclerAdapter", "onViewAttachedToWindow", "pos = "
                    + holder.getAdapterPosition());
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
    protected abstract @LayoutRes
    int getItemLayoutId(int position, T t);

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
            Logger.i("DataBindingViewHolder","getDataMediator","pos = " + pos);
            return adapter.getBinder(pos).getDataMediator();
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
         * called on attach the item of current position.
         */
        public void onAttachItem() {

        }

        /**
         * called on detach the item of current position.
         */
        public void onDetachItem() {

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
        public void onBindData(int position, @Nullable DataBinding.SimpleParameterSupplier supplier) {
            DataBindingRecyclerAdapter<T> adapter = getAdapter();
            if (adapter == null) {
                return;
            }
            T module = adapter.getItem(position);
            Binder<T> binder = adapter.mBinderMap.get(module);
            if (binder != null) {
                binder.unbindAll();
            }
            adapter.mBinderMap.put(module, mDataBinding.bindAndApply(module,
                    0, supplier, getPropertyInterceptor()));
        }

        /**
         * get the property interceptor which is used to data-binding.
         *
         * @return the property interceptor.
         * @see PropertyInterceptor
         * @see DataBinding#bind(Object, int, DataBinding.ParameterSupplier, PropertyInterceptor)
         */
        protected @Nullable
        PropertyInterceptor getPropertyInterceptor() {
            return PropertyInterceptor.NULL;
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
