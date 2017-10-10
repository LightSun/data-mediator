package com.heaven7.data.mediator.demo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v4.graphics.drawable.IconCompat;

import com.heaven7.core.util.Logger;
import com.heaven7.data.mediator.demo.MainActivity;
import com.heaven7.data.mediator.demo.R;

/**
 * Created by heaven7 on 2017/10/10 0010.
 */

public abstract class ShortCutIconDelegate {

    public static void addShortcut2(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.addCategory("android.intent.category.DEFAULT");

        ShortcutInfoCompat infoCompat = new ShortcutInfoCompat.Builder(context, context.getPackageName())
                .setIntent(intent)
                .setShortLabel("mn_heaven7")
                .setIcon(IconCompat.createWithResource(context, R.mipmap.ic_launcher_round))
                .build();
        boolean result = ShortcutManagerCompat.requestPinShortcut(context,
                infoCompat, null
        );
        Logger.i("ShortCutIconDelegate","addShortcut2","result = "+ result);
        /*Intent resulIntent = ShortcutManagerCompat.createShortcutResultIntent(context, infoCompat);
        Activity activity = (Activity) context;
        activity.setResult(Activity.RESULT_OK, resulIntent);
        activity.finish();*/
    }

    public static void addShortcut(Context context){

        Logger.i("ShortCutIconDelegate","addShortcut","v = " + Build.VERSION.SDK_INT);
        if(Build.VERSION.SDK_INT >= 25) {
            ShortcutManager sm = context.getSystemService(ShortcutManager.class);
            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addCategory("android.intent.category.DEFAULT");

            ShortcutInfo info = new ShortcutInfo.Builder(context, context.getPackageName())
                    .setIntent(intent)
                    .setShortLabel("mn_heaven7")
                    .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher_round))
                    .build();
            //PendingIntent.getIntentSender()
            sm.requestPinShortcut(info, null);
        }else{
            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addCategory("android.intent.category.DEFAULT");
            ShortCutIconDelegate.addShortcut(context,
                    context.getPackageName(),
                    R.mipmap.ic_launcher_round,
                    intent, false);
        }
    }


    public static void addShortcut2(Context context, String shortcutName, int iconRes,
                                   Intent actionIntent, boolean allowRepeat){
        Intent shortcutintent = new Intent(Intent.ACTION_CREATE_SHORTCUT);
        //是否允许重复创建
        shortcutintent.putExtra("duplicate",allowRepeat);
        //快捷方式的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        //设置快捷方式图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), iconRes);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //设置快捷方式动作
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent);
        //向系统发送广播
        context.sendBroadcast(shortcutintent);
    }

    /**
     * 创建快捷方式1 :
     * //第一种设置快捷方式图片
     Parcelable icon = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), iconRes);
     shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
     */
    public static void addShortcut(Context context, String shortcutName, int iconRes,
                                   Intent actionIntent, boolean allowRepeat){
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //是否允许重复创建
        shortcutintent.putExtra("duplicate",allowRepeat);
        //快捷方式的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        //设置快捷方式图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), iconRes);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //设置快捷方式动作
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent);
        //向系统发送广播
        context.sendBroadcast(shortcutintent);
    }
    /**
     * 创建快捷方式2:
     shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
     */
    public static void addShortcut(Context context, String shotcutName, Bitmap bitmap, Intent actionIntent, boolean allowRepeat){
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //是否允许重复创建
        shortcutintent.putExtra("duplicate",allowRepeat);
        //快捷方式的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shotcutName);
        //设置快捷方式图片
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
        //设置快捷方式动作
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent);
        //向系统发送广播
        context.sendBroadcast(shortcutintent);

    }
    /**
     * 删除快捷键
     *
     */
    public void deleteShortcut(Context context, String name, int iconRes,Intent actionIntent,boolean allowRepeat){
        Intent shortcutintent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        //是否循环删除
        shortcutintent.putExtra("duplicate",allowRepeat);
        //快捷方式的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        //设置快捷方式动作
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent);
        //向系统发送广播
        context.sendBroadcast(shortcutintent);
    }

}
