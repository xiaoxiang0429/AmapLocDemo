package com.tz.amaplocdemo.util;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.tz.amaplocdemo.base.TApplication;


/**
 * Created by Administrator on 2017/10/27.
 *
 * @author YY
 */

public final class ToastUtil {
    private static Toast toast;

    private ToastUtil() {
    }

    /**
     * sHandler维护的是主线程的消息队列
     */
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    /**
     * 无论如何, Toast都必须在主线程中被调用
     *
     * @param msg     toast文字
     */
    public static void showToastShort(final String msg) {
        //判断是否是主线程
        if (Looper.myLooper() == Looper.getMainLooper()) {
            //当前线程是主线程
            toastShowShort(msg);
        } else {
            // handler.sendMessage(msg); 把一个消息发送到消息队列
            // 这个消息队列属于主线程还是子线程，取决于handler维护的是哪一个线程的消息队列

            //将Runnable丢到主线程的消息队列
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    toastShowShort(msg);
                }
            });
        }
    }

    /**
     * toast的文字是资源id
     *
     * @param resId   资源id
     */
    public static void showToastShort(int resId) {
        showToastShort(TApplication.getInstance().getApplicationContext().getString(resId));
    }

    public static void showToastLong(final String msg) {
        //判断是否是主线程
        if (Looper.myLooper() == Looper.getMainLooper()) {
            //当前线程是主线程
            toastShowLong(msg);
        } else {
            // handler.sendMessage(msg); 把一个消息发送到消息队列
            // 这个消息队列属于主线程还是子线程，取决于handler维护的是哪一个线程的消息队列

            //将Runnable丢到主线程的消息队列
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    toastShowLong(msg);
                }
            });
        }
    }

    public static void showToastLong(int resId) {
        showToastLong(TApplication.getInstance().getApplicationContext().getString(resId));
    }

    /**
     * toastShowShort
     * @param msg msg
     */
    private static void toastShowShort(String msg) {
        if (toast == null) {
            toast = Toast.makeText(TApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * toastShowLong
     * @param msg msg
     */
    private static void toastShowLong(String msg) {
        if (toast == null) {
            toast = Toast.makeText(TApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

}
