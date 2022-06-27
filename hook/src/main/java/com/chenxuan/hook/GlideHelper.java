package com.chenxuan.hook;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bumptech.glide.request.RequestListener;

import java.util.ArrayList;
import java.util.List;

public class GlideHelper {
    private List<RequestListener<Drawable>> requestListeners;

    private void hook() {
        if (requestListeners == null) requestListeners = new ArrayList<>();
        GlideUtil.addListener(requestListeners);
    }

    public void handler() {
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        long currentTimeMills = end - start;
        Log.d("chenxuan----->", "Method name total time: " + currentTimeMills + "ms");
    }
}