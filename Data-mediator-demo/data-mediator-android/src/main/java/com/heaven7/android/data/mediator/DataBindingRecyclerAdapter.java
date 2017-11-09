package com.heaven7.android.data.mediator;

import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataBinding;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.PropertyInterceptor;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * the data-binding adapter of recycler view.
 * Created by heaven7 on 2017/11/9 0009.
 * @since 1.1.2
 */

public abstract class DataBindingRecyclerAdapter<T> extends RecyclerView.Adapter<
        DataBindingRecyclerAdapter.DataBindingViewHolder<T>> {

    private final List<T> mDatas;
    private DataBinding.SimpleParameterSupplier mSupplier;

    public DataBindingRecyclerAdapter(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public void onBindViewHolder(DataBindingViewHolder<T> viewHolder, int position) {
        viewHolder.onBindData(position, getParameterSupplier());
    }

    @Override
    public final DataBindingViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        DataBindingViewHolder<T> holder = onCreateViewHolderImpl(parent, viewType);
        holder.mWeakAdapter = new WeakReference<>(this);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemLayoutId(position, getItem(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public final T getItem(int position){
        return mDatas.get(position);
    }

    protected abstract  @LayoutRes int getItemLayoutId(int position, T t);

    protected DataBinding.SimpleParameterSupplier onCreateParameterSupplier() {
        return null;
    }

    public abstract DataBindingViewHolder<T> onCreateViewHolderImpl(ViewGroup parent, int viewType);

    //=========================== private method =============================

    private DataBinding.SimpleParameterSupplier getParameterSupplier(){
        return mSupplier != null ? mSupplier : (mSupplier = onCreateParameterSupplier());
    }

    //TestDatabindingAdapter$InnerViewHolder_$DataBinding
    public abstract static class DataBindingViewHolder<T> extends RecyclerView.ViewHolder{

        private final SparseArray<Binder<T>> mBinderMap = new SparseArray<>();
        private final DataBinding<DataBindingViewHolder<T>> mDataBinding;
        private WeakReference<DataBindingRecyclerAdapter<T>> mWeakAdapter;

        public DataBindingViewHolder(View itemView) {
            super(itemView);
            onPreCreateDataBinding(itemView);
            mDataBinding = DataMediatorFactory.createDataBinding(this);
        }

        public final DataMediator<T> getDataMediator(){
            return mBinderMap.get(getAdapterPosition()).getDataMediator();
        }

        public final DataBindingRecyclerAdapter<T> getAdapter(){
            return mWeakAdapter != null ? mWeakAdapter.get() : null;
        }

        @CallSuper
        public void onBindData(int position, @Nullable DataBinding.SimpleParameterSupplier supplier){
            DataBindingRecyclerAdapter<T> adapter = getAdapter();
            if(adapter == null){
                return;
            }
            Binder<T> binder = mBinderMap.getAndRemove(position);
            if(binder != null){
                binder.unbindAll();
            }
            mBinderMap.put(position, mDataBinding.bind(adapter.getItem(position),
                    0, supplier, getPropertyInterceptor()));
        }

        public void unbind(){
            Binder<T> binder = mBinderMap.getAndRemove(getAdapterPosition());
            if(binder != null){
                binder.unbindAll();
            }
        }

        protected void onPreCreateDataBinding(View itemView){

        }

        public @Nullable PropertyInterceptor getPropertyInterceptor(){
            return null;
        }

    }
}
