package com.heaven7.data.mediator.demo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v4.graphics.drawable.IconCompat;

import com.heaven7.core.util.Logger;
import com.heaven7.data.mediator.demo.MainActivity;
import com.heaven7.data.mediator.demo.R;

import java.util.List;

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
            //ok
            ShortcutManager sm = context.getSystemService(ShortcutManager.class);
            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);

            ShortcutInfo info = new ShortcutInfo.Builder(context, context.getPackageName())
                    .setIntent(intent)
                    .setShortLabel("mn_heaven7")
                    .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher_round))
                    .build();
            //PendingIntent.getIntentSender()
            sm.requestPinShortcut(info, null);
        }else{
            createShortCut(context, MainActivity.class, false);
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
     * 创建快捷方式
     *
     * @param context   上下文对象
     * @param cls       Class
     * @param duplicate 是否可以重复创建
     */
    public static void createShortCut(Context context, Class<?> cls, boolean duplicate) {
        Logger.i("ShortCutIconDelegate","createShortCut","exist = " + isShortCutExist(context));
        if (context == null || isShortCutExist(context)) {
            return;
        }
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        Intent intent = new Intent(context, cls);
        // 设置这两个属性当应用程序卸载时桌面上的快捷方式会删除
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher);
        shortcutIntent.putExtra("duplicate", duplicate);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

        context.sendBroadcast(shortcutIntent);
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

    /**
     * 判断快捷方式是否已存在
     *
     * @param context 应用上下文
     * @return 快捷方式是否已存在
     */
    private static boolean isShortCutExist(Context context) {
        String providerAuthority = null;
        try {
            providerAuthority = readProviderAuthority(context, "com.android.launcher.permission.READ_SETTINGS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (providerAuthority == null) {
            return true;
        }
        ContentResolver cr = context.getContentResolver();
        Uri contentUri = Uri.parse("content://" + providerAuthority + "/favorites?notify=true");
        Cursor cursor;
        try {
            cursor = cr.query(contentUri, null, "title=?",
                    new String[]{context.getString(R.string.app_name)}, null);
        } catch (Exception e) {
            return true;
        }
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }
    /**
     * 读取权限
     *
     * @param context    应用上下文
     * @param permission 应用权限
     * @return
     */
    private static String readProviderAuthority(Context context, String permission) {
        if (permission == null) {
            return null;
        }
        List<PackageInfo> packageInfoList = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
        if (packageInfoList == null) {
            return null;
        }
        for (PackageInfo pack : packageInfoList) {
            ProviderInfo[] providers = pack.providers;
            if (providers != null) {
                for (ProviderInfo provider : providers) {
                    if (permission.equals(provider.readPermission)) {
                        return provider.authority;
                    } else if (permission.equals(provider.writePermission)) {
                        return provider.authority;
                    }
                }
            }
        }
        return null;
    }
}
