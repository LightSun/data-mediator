package com.heaven7.data.mediator.data_binding_test.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.heaven7.android.data.mediator.AndroidBinder;
import com.heaven7.android.data.mediator.SimpleBinderCallback2;
import com.heaven7.core.util.Toaster;
import com.heaven7.data.mediator.data_binding_test.module.ImageViewBind;
import com.heaven7.data.mediator.data_binding_test.util.ImageLoader;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataBinding;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.PropertyInterceptor;
import com.heaven7.java.data.mediator.bind.BinderClass;

/**
 * test self Binder. this demo will show how to override binder method for data-binding.
 * Created by heaven7 on 2017/11/13 0013.
 */
@BinderClass(TestSelfBinderActivity.MyBinder.class)
public class TestSelfBinderActivity extends TestImageViewBindActivity implements ImageLoader{

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mResHelper.init(context);

        final ImageViewBind data = DataMediatorFactory.createData(ImageViewBind.class);
        mBinder = DataMediatorFactory.createDataBinding(this)
                .bind(data, new DataBinding.SimpleParameterSupplier() {
                    @Override
                    protected Object getImageLoader() {
                        return TestSelfBinderActivity.this;
                    }
                }, null);
    }

    @Override
    public void loadImage(String url,final ImageView iv) {
        Glide.with(iv.getContext())
                .load(url)
                .into(new SimpleTarget<Drawable>(200, 200) {
                    @Override
                    public void onResourceReady(Drawable resource,
                                                Transition<? super Drawable> transition) {
                        iv.setImageDrawable(resource);
                    }
                });

    }

    public static class MyBinder<T> extends AndroidBinder<T>{

        /**
         * create binder for target data mediator. this constructor must be public .
         *
         * @param mMediator the target data mediator.
         */
        public MyBinder(DataMediator<T> mMediator) {
            super(mMediator);
        }

        @Override
        public Binder<T> bindImageUrl(String property, Object imageView, Object imageLoader) {
            if(!(imageView instanceof ImageView)){
                throw new IllegalArgumentException("the view must be ImageView");
            }
            if(imageLoader instanceof ImageLoader){
                ImageView iv = (ImageView) imageView;
                Toaster.show(iv.getContext(), "start using self MyBinder with image loader.");
                return bind(property, new ImageUrlBinderCallback<T>(iv, getPropertyInterceptor(),
                        (ImageLoader)imageLoader));
            }
            return super.bindImageUrl(property, imageView, imageLoader);
        }
    }

    private static class ImageUrlBinderCallback<T> extends SimpleBinderCallback2<T> {

        final ImageLoader mLoader;
        public ImageUrlBinderCallback(ImageView tv, PropertyInterceptor interceptor,
                                      ImageLoader loader) {
            super(tv, interceptor);
            this.mLoader = loader;
        }
        @Override
        protected void apply(Property prop, View view, Object newValue) {
            mLoader.loadImage((String) newValue, (ImageView) view);
        }
    }
}
