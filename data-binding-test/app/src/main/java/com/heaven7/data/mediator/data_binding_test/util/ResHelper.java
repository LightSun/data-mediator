package com.heaven7.data.mediator.data_binding_test.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.heaven7.data.mediator.data_binding_test.R;

public class ResHelper {

    static final String URL_1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510078438082&di=a69c86d4cc6ab8c3f83e3d624e41e869&imgtype=0&src=http%3A%2F%2Fimg1.3lian.com%2F2015%2Fa1%2F40%2Fd%2F191.jpg";
    static final String URL_2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510078568038&di=d80672939c82b81fa52ad0075e793b22&imgtype=0&src=http%3A%2F%2Fimg17.3lian.com%2Fd%2Ffile%2F201701%2F20%2F70ac16a3c3336a3bc2fb28c147bf2049.jpg";

    int res1;
    int res2;

    Bitmap bitmap1;
    Bitmap bitmap2;

    Drawable drawable1;
    Drawable drawable2;

    int color1;
    int color2;

    boolean useUrl1;
    boolean useRes1;
    boolean useBitmap1;
    boolean useDrawable1;
    boolean useColor1;

    //=============================================
    int textColorRes1;
    int textColorRes2;
    int textSizeRes1;
    int textSizeRes2;
    int textRes1;
    int textRes2;

    boolean useTextColorRes1;
    boolean useTextSizeRes1;
    boolean useTextRes1;

    public void init(Context context) {
        final Resources res = context.getResources();
        res1 = R.mipmap.ic_launcher;
        res2 = R.mipmap.ic_launcher_round;
        drawable1 = res.getDrawable(res1);
        drawable2 = res.getDrawable(res2);
        bitmap1 = BitmapFactory.decodeResource(res, res1);
        bitmap2 = BitmapFactory.decodeResource(res, res2);
        color1 = Color.RED;
        color2 = Color.GREEN;

        textColorRes1 = android.R.color.holo_red_light;
        textColorRes2 = android.R.color.black;

        textSizeRes1 = R.dimen.dip_20;
        textSizeRes2 = R.dimen.dip_30;

        textRes1 = R.string.text_1;
        textRes2 = R.string.text_3;
        int size = res.getDimensionPixelSize(textSizeRes2);
    }

    public String toggleUrl() {
        String result = useUrl1 ? URL_2 : URL_1;
        useUrl1 = !useUrl1;
        return result;
    }

    public int toggleDrawableRes() {
        int result = useRes1 ? res2 : res1;
        useRes1 = !useRes1;
        return result;
    }

    public Bitmap toggleBitmap() {
        Bitmap result = useBitmap1 ? bitmap2 : bitmap1;
        useBitmap1 = !useBitmap1;
        return result;
    }

    public Drawable toggleDrawable() {
        Drawable result = useDrawable1 ? drawable2 : drawable1;
        useDrawable1 = !useDrawable1;
        return result;
    }
    public int toggleColor() {
        int color = useColor1 ? color2 : color1;
        useColor1 = !useColor1;
        return color;
    }

    public int toggleTextColorRes(){
        int colorRes = useTextColorRes1 ? textColorRes2 : textColorRes1;
        useTextColorRes1 = !useTextColorRes1;
        return colorRes;
    }
    public int toggleTextSizeRes(){
        int res = useTextSizeRes1 ? textSizeRes2 : textSizeRes1;
        useTextSizeRes1 = !useTextSizeRes1;
        return res;
    }
    public int toggleTextRes(){
        int res = useTextRes1 ? textRes2 : textRes1;
        useTextRes1 = !useTextRes1;
        return res;
    }
}