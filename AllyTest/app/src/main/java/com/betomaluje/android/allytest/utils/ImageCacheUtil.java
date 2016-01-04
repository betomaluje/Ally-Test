package com.betomaluje.android.allytest.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.PictureDrawable;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by betomaluje on 12/31/15.
 */
public class ImageCacheUtil {

    private static ImageCacheUtil instance;
    private static HashMap<String, Bitmap> images;

    public static ImageCacheUtil getInstance() {
        if (instance == null) {
            instance = new ImageCacheUtil();
            images = new HashMap<>();
        }

        return instance;
    }

    public Bitmap getImage(String url) {
        if (images.containsKey(url))
            return images.get(url);
        else
            return null;
    }

    public void putImage(String url, PictureDrawable image) {
        if (!images.containsKey(url)) {
            Bitmap bm = Bitmap.createBitmap(image.getIntrinsicWidth(), image.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            canvas.drawPicture(image.getPicture());

            images.put(url, bm);
        }
    }

    public void saveImageToFile(PictureDrawable pd, String filename) {
        Bitmap bm = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawPicture(pd.getPicture());

        //now we save it to storage
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + ".ally/images");

        boolean doSave = true;
        if (!dir.exists()) {
            doSave = dir.mkdirs();
        }

        if (doSave) {
            saveBitmapToFile(dir, filename, bm, Bitmap.CompressFormat.PNG, 80);
        }
    }

    /**
     * Bitmap.CompressFormat can be PNG,JPEG or WEBP.
     * <p/>
     * quality goes from 1 to 100. (Percentage).
     * <p/>
     * dir you can get from many places like Environment.getExternalStorageDirectory() or mContext.getFilesDir()
     * depending on where you want to save the image.
     */
    private boolean saveBitmapToFile(File dir, String fileName, Bitmap bm,
                                     Bitmap.CompressFormat format, int quality) {

        File imageFile = new File(dir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);

            bm.compress(format, quality, fos);

            fos.close();

            return true;
        } catch (IOException e) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            e.printStackTrace();
        }
        return false;
    }

    public boolean getImageFromFile(ImageView imageView, String filename) {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + ".ally/images");

        File imgFile = new File(dir, filename);

        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            imageView.setImageBitmap(myBitmap);

            return true;
        }

        return false;
    }

}
