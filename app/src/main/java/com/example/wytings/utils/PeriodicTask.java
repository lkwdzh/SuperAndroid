package com.example.wytings.utils;

import android.content.Context;
import android.os.Handler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rex on 2016/1/12.
 */
public class PeriodicTask {

    private ScheduledExecutorService executorService;
    private long initialDelay;
    private long delay;
    private Handler innerHandler;
    private PeriodicTaskCallback callback;

    public PeriodicTask(Context context, long initialDelaySeconds, long delaySeconds) {
        this.initialDelay = initialDelaySeconds;
        this.delay = delaySeconds;
        innerHandler = new Handler(context.getMainLooper());
    }

    public void setPeriodicTaskCallback(PeriodicTaskCallback callback) {
        this.callback = callback;
    }


    public void startTask() {
        if (executorService == null) {
            executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    innerHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onStart();
                        }
                    });
                    try {
                        callback.innerOnceValidCall();
                        final Object object = callback.onCall();
                        innerHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onEnd(object);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, initialDelay, delay, TimeUnit.SECONDS);
        }
    }

    public void stopTask() {
        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
    }

    public static abstract class PeriodicTaskCallback {
        private boolean onceValidFlag = false;

        public void onStart() {
        }

        public boolean onOnceValidCall() throws Exception {
            return true;
        }

        final void innerOnceValidCall() {
            if (onceValidFlag) {
                return;
            }
            try {
                onceValidFlag = onOnceValidCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public abstract Object onCall() throws Exception;

        public abstract void onEnd(Object object);
    }
}
