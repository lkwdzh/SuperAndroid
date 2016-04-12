package com.example.wytings.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Rex on 2016/3/31.
 * https://github.com/wytings
 */
public class DataLoader {

    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private static Handler internalHandler = new Handler(Looper.getMainLooper());

    public static void loadData(final DataLoaderCallback callback) {
        executorService.submit(new Runnable() {
            Object result = null;

            @Override
            public void run() {
                internalHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onStart();
                        internalHandler.removeCallbacks(this);
                    }
                });
                try {
                    result = callback.onCall();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                internalHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onEnd(result);
                        internalHandler.removeCallbacks(this);
                    }
                });
            }
        });
    }

    public static void shutdown() {
        try {
            internalHandler.removeCallbacksAndMessages(null);
            executorService.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static abstract class DataLoaderCallback {
        public void onStart() {
        }

        public abstract Object onCall() throws Exception;

        public abstract void onEnd(Object object);
    }

}
