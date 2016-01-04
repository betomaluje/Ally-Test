package com.betomaluje.android.allytest.utils.bus;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by betomaluje on 1/4/16.
 */
public class BusStation {

    public static Bus bus = new Bus();

    private static final Handler mainThread = new Handler(Looper.getMainLooper());

    public static Bus getBus() {
        return bus;
    }

    public static void postOnMain(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            getBus().post(event);
        } else {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    getBus().post(event);
                }
            });
        }
    }

    public static void postToSameThread(final Object event) {
        getBus().post(event);
    }
}
