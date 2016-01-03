package com.betomaluje.android.allytest.utils;

import android.graphics.drawable.Drawable;

import java.util.HashMap;

/**
 * Created by betomaluje on 12/31/15.
 */
public class ImageCacheUtil {

    private static ImageCacheUtil instance;
    private static HashMap<String, Drawable> images;

    public static ImageCacheUtil getInstance() {
        if (instance == null) {
            instance = new ImageCacheUtil();
            images = new HashMap<>();
        }

        return instance;
    }

    public Drawable getImage(String url) {
        if (images.containsKey(url))
            return images.get(url);
        else
            return null;
    }

    public void putImage(String url, Drawable image) {
        images.put(url, image);
    }

}
