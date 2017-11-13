package com.heaven7.data.mediator.data_binding_test.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.heaven7.core.util.ViewHelper;
import com.heaven7.data.mediator.data_binding_test.R;
import com.heaven7.data.mediator.data_binding_test.module.ImageViewBind;
import com.heaven7.data.mediator.data_binding_test.util.ResHelper;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataBinding;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.bind.BindImageBitmap;
import com.heaven7.java.data.mediator.bind.BindImageDrawable;
import com.heaven7.java.data.mediator.bind.BindImageRes;
import com.heaven7.java.data.mediator.bind.BindImageUrl;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * test bind image property
 * Created by heaven7 on 2017/11/7.
 */
public class TestImageViewBindActivity extends BaseActivity implements DataBinding.ParameterSupplier {

    @BindView(R.id.iv_url) @BindImageUrl("imageUrl")
    ImageView ivUrl;

    @BindView(R.id.iv_bitmap) @BindImageBitmap("imageBitmap")
    ImageView ivBitmap;

    @BindView(R.id.iv_drawable) @BindImageDrawable("imageDrawable")
    ImageView ivDrawable;

    @BindView(R.id.iv_res) @BindImageRes("imageRes")
    ImageView ivRes;

    protected ResHelper mResHelper = new ResHelper();
    protected Binder<ImageViewBind> mBinder;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_image_view_bind;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mResHelper.init(context);

        final ImageViewBind data = DataMediatorFactory.createData(ImageViewBind.class);
        mBinder = DataMediatorFactory.createDataBinding(this)
                .bind(data, this, null);
    }
    @OnClick(R.id.bt_url)
    public void onClickUrl(View v){
        mBinder.getDataProxy().setImageUrl(mResHelper.toggleUrl());
    }
    @OnClick(R.id.bt_bitmap)
    public void onClickBitmap(View v){
        mBinder.getDataProxy().setImageBitmap(mResHelper.toggleBitmap());
    }
    @OnClick(R.id.bt_drawable)
    public void onClickDrawable(View v){
        mBinder.getDataProxy().setImageDrawable(mResHelper.toggleDrawable());
    }
    @OnClick(R.id.bt_res)
    public void onClickRes(View v){
        mBinder.getDataProxy().setImageRes(mResHelper.toggleDrawableRes());
    }

    /**
     * {@inheritDoc}. bind imageUrl need pass extra parameter: 'ViewHelper.IImageLoader'
     * @param data the module data.
     * @param property the property
     * @return the parameter of data-binding for target data and property.
     * @see Binder#bindImageUrl(String, Object, Object)
     * @see com.heaven7.java.data.mediator.bind.BinderClass
     * @see com.heaven7.java.data.mediator.bind.BinderFactoryClass
     * @see Binder
     * @see com.heaven7.java.data.mediator.BinderFactory
     */
    @Override //bind imageUrl need pass extra parameter: 'ViewHelper.IImageLoader'
    public Object[] getParameters(Object data, String property) {
        // why here use 'ViewHelper.IImageLoader' ?
        // because the default AndroidBinder use it to load image.
        // if you want change .please override method 'bindImageUrl' and use @BinderClass/@BinderFactoryClass
        return !property.equals("imageUrl") ? null : new Object[]{ mLoader };
    }

    private final ViewHelper.IImageLoader mLoader = new ViewHelper.IImageLoader() {
        @Override
        public void load(String url, final ImageView iv) {
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
    };

}
