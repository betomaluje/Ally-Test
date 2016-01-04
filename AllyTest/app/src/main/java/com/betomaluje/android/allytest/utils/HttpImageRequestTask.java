package com.betomaluje.android.allytest.utils;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by betomaluje on 1/2/16.
 */
public class HttpImageRequestTask extends AsyncTask<String, Void, Drawable> {

    private ImageView mImageView;
    private View progressBar;

    public HttpImageRequestTask(ImageView mImageView, View progressBar) {
        this.mImageView = mImageView;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Drawable doInBackground(String... params) {
        try {
            final URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            SVG svg = SVG.getFromInputStream(inputStream);

            Drawable drawable = new PictureDrawable(svg.renderToPicture());

            ImageCacheUtil.getInstance().putImage(params[0], drawable);

            return drawable;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        // Update the view
        updateImageView(drawable);
        this.progressBar.setVisibility(View.GONE);
    }

    @SuppressLint("NewApi")
    private void updateImageView(Drawable drawable) {
        if (drawable != null) {
            // Try using your library and adding this layer type before switching your SVG parsing
            mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mImageView.setImageDrawable(drawable);
        }
    }
}