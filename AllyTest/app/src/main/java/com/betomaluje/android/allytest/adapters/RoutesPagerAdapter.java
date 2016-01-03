package com.betomaluje.android.allytest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.betomaluje.android.allytest.R;
import com.betomaluje.android.allytest.models.routes.Route;
import com.betomaluje.android.allytest.models.routes.Segment;
import com.betomaluje.android.allytest.utils.ImageCacheUtil;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by betomaluje on 1/2/16.
 */
public class RoutesPagerAdapter extends PagerAdapter {

    private Route route;
    private ArrayList<Segment> mSegments;
    // This holds all the currently displayable views, in order from left to right.
    private SparseArray<View> views = new SparseArray<View>();

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private RoutesRecyclerAdapter.OnRouteClickListener onRouteClickListener;

    public RoutesPagerAdapter(Context context, Route route) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.route = route;
        this.mSegments = route.getSegments();
    }

    public void setOnRouteClickListener(RoutesRecyclerAdapter.OnRouteClickListener onRouteClickListener) {
        this.onRouteClickListener = onRouteClickListener;
    }

    @Override
    public int getCount() {
        return mSegments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;

        if (views.get(position) == null) {
            view = mLayoutInflater.inflate(R.layout.item_segment, container, false);
            views.put(position, view);
        } else {
            view = views.get(position);
        }

        ImageView imageView_icon = (ImageView) view.findViewById(R.id.imageView_icon);
        TextView textView_name = (TextView) view.findViewById(R.id.textView_name);

        Segment segment = mSegments.get(position);

        textView_name.setText(segment.getName());

        if (segment.getIconUrl() != null && !TextUtils.isEmpty(segment.getIconUrl())) {

            ImageCacheUtil imageCacheUtil = ImageCacheUtil.getInstance();

            Drawable image = imageCacheUtil.getImage(segment.getIconUrl());

            if (image != null) {
                imageView_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                imageView_icon.setImageDrawable(image);
            } else {
                new HttpImageRequestTask(imageView_icon).execute(segment.getIconUrl());
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRouteClickListener != null)
                    onRouteClickListener.OnRouteClicked(v, route);
            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView(views.get(position));
        views.remove(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private class HttpImageRequestTask extends AsyncTask<String, Void, Drawable> {

        private ImageView mImageView;

        public HttpImageRequestTask(ImageView mImageView) {
            this.mImageView = mImageView;
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
}
