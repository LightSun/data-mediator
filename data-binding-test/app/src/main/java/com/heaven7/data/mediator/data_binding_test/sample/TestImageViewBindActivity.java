package com.heaven7.data.mediator.data_binding_test.sample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private ResHelper mResHelper = new ResHelper();
    private Binder<ImageViewBind> mBinder;

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
        mBinder.getDataProxy().setImageRes(mResHelper.toggleRes());
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

    public static class ResHelper{

       static final String URL_1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510078438082&di=a69c86d4cc6ab8c3f83e3d624e41e869&imgtype=0&src=http%3A%2F%2Fimg1.3lian.com%2F2015%2Fa1%2F40%2Fd%2F191.jpg";
       static final String URL_2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510078568038&di=d80672939c82b81fa52ad0075e793b22&imgtype=0&src=http%3A%2F%2Fimg17.3lian.com%2Fd%2Ffile%2F201701%2F20%2F70ac16a3c3336a3bc2fb28c147bf2049.jpg";

       int res1;
       int res2;

       Bitmap bitmap1;
       Bitmap bitmap2;

       Drawable drawable1;
       Drawable drawable2;

       boolean useUrl1;
       boolean useRes1;
       boolean useBitmap1;
       boolean useDrawable1;

       void init(Context context){
           final Resources res = context.getResources();
           res1 = R.mipmap.ic_launcher;
           res2 = R.mipmap.ic_launcher_round;
           drawable1 = res.getDrawable(res1);
           drawable2 = res.getDrawable(res2);
           bitmap1 = BitmapFactory.decodeResource(res, res1);
           bitmap2 = BitmapFactory.decodeResource(res, res2);
        }
        String toggleUrl(){
            String result = useUrl1 ? URL_2 : URL_1;
            useUrl1 = !useUrl1;
            return result;
        }
        int toggleRes(){
            int result = useRes1 ? res2 : res1;
            useRes1 = !useRes1;
            return result;
        }
        Bitmap toggleBitmap(){
            Bitmap result = useBitmap1 ? bitmap2 : bitmap1;
            useBitmap1 = !useBitmap1;
            return result;
        }
        Drawable toggleDrawable(){
            Drawable result = useDrawable1 ? drawable2 : drawable1;
            useDrawable1 = !useDrawable1;
            return result;
        }
    }
}
