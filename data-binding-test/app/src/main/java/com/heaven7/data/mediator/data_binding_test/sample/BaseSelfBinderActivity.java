package com.heaven7.data.mediator.data_binding_test.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.heaven7.data.mediator.data_binding_test.module.ImageViewBind;
import com.heaven7.data.mediator.data_binding_test.util.ImageLoader;
import com.heaven7.java.data.mediator.DataBinding;
import com.heaven7.java.data.mediator.DataMediatorFactory;

/**
 * the base context of self Binder ({@linkplain com.heaven7.java.data.mediator.bind.BinderClass})
 * and self BinderFactory.({@linkplain com.heaven7.java.data.mediator.bind.BinderFactoryClass}).
 * Created by heaven7 on 2017/11/13 0013.
 */

public abstract class BaseSelfBinderActivity extends TestImageViewBindActivity implements ImageLoader {

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mResHelper.init(context);

        final ImageViewBind data = DataMediatorFactory.createData(ImageViewBind.class);
        mBinder = DataMediatorFactory.createDataBinding(this)
                .bind(data, new DataBinding.SimpleParameterSupplier() {
                    @Override
                    protected Object getImageLoader() {
                        return BaseSelfBinderActivity.this;
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
}
