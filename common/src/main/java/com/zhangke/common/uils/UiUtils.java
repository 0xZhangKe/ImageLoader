package com.zhangke.common.uils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
/**
 * Created by ZhangKe on 2017/9/24.
 */

public class UiUtils {
    private static Toast toast;

    public static void showToast(final Context context, final String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 设置ListView高度充满
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 设置透明Dialog大小，默认为屏幕宽度的0.8，需要在Dialog.show之后调用
     *
     * @param dialog
     * @param context
     */
    public static void setDialogDefaultSize(Dialog dialog, Context context) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高
        lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
    }

    /**
     * 设置状态栏颜色（沉浸式），兼容到android4.4
     *
     * @param activity
     * @param color
     */
    public static void setWindowColorRect(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= 14) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置根布局的参数
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(activity, color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 设置Window Title颜色<br/>
     *
     * @param activity activity
     */
    public static void setWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
//            setWindowColorRect(activity, activity.getResources().getColor(Color.TRANSPARENT));
        }
    }

    /**
     * 生成一个和状态栏大小相同的矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    private static View createStatusView(Activity activity, int color) {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(activity.getResources().getColor(color));
        return statusView;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * <p>
     * （DisplayMetrics类中属性density）
     *
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 检测是否有拍照权限
     *
     * @param context
     * @return
     */
    public static boolean checkCameraPermission(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.CAMERA", context.getPackageName()));
        if (!permission) {
            showPermissionDialog(context, "拍照权限已被禁止，请到设置中打开。");
        }
        return permission;
    }

    /**
     * 检测是否有打电话权限
     *
     * @param context
     * @return
     */
    public static boolean checkCallPhonePermission(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.CALL_PHONE", context.getPackageName()));
        if (!permission) {
            showPermissionDialog(context, "拨打电话权限已被禁止，请到设置中打开。");
        }
        return permission;
    }

    /**
     * 检测是否有定位权限
     *
     * @param context
     * @return
     */
    public static boolean checkLocationPermission(Context context, DialogInterface.OnClickListener onClickListener) {
        PackageManager pm = context.getPackageManager();
//        boolean netPermission = (PackageManager.PERMISSION_GRANTED ==
//                pm.checkPermission("android.permission.ACCESS_COARSE_LOCATION", "com.liren.oa"));
        boolean GPSPermission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", context.getPackageName()));
        if (!GPSPermission) {
            showPermissionDialog(context, "定位权限已被禁止，请到设置中打开。", onClickListener);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检测是否有定位权限
     *
     * @param context
     * @return
     */
    public static boolean checkLocationPermission(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean GPSPermission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", context.getPackageName()));
        if (!GPSPermission) {
            UiUtils.showToast(context, "定位权限已被禁止，请到设置中打开。");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检测是否有读取存储的权限
     *
     * @param context
     * @return
     */
    public static boolean checkStorangePermission(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean netPermission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", context.getPackageName()));
        if (!netPermission) {
            showPermissionDialog(context, "读取存储权限已被禁止，请到设置中打开。");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检测是否有发送短信的权限
     *
     * @param context
     * @return
     */
    public static boolean checkSMSPermission(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean netPermission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.SEND_SMS", context.getPackageName()));
        if (!netPermission) {
            showPermissionDialog(context, "发送短信权限已被禁止，请到设置中打开。");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检测是否有查看WIFI信息的权限
     *
     * @param context
     * @return
     */
    public static boolean checkWIFIPermission(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean netPermission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.ACCESS_WIFI_STATE", context.getPackageName()));
        if (!netPermission) {
            showPermissionDialog(context, "获取WIFI信息权限已被禁止，请到设置中打开。");
            return false;
        } else {
            return true;
        }
    }

    private static void showPermissionDialog(Context context,
                                             String message) {
        showPermissionDialog(context, message, null);
    }

    private static void showPermissionDialog(Context context,
                                             String message,
                                             DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("注意");
        builder.setMessage(message);
        builder.setPositiveButton("确定", onClickListener);
        builder.create().show();
    }
}
