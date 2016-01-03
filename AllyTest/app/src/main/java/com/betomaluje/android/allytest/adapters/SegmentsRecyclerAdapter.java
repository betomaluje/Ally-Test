package com.betomaluje.android.allytest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.betomaluje.android.allytest.R;
import com.betomaluje.android.allytest.models.routes.Segment;
import com.betomaluje.android.allytest.utils.ImageCacheUtil;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by betomaluje on 12/31/15.
 */
public class SegmentsRecyclerAdapter extends RecyclerView.Adapter<SegmentsRecyclerAdapter.SegmentsViewHolder> {

    private ArrayList<Segment> mSegments;
    private Context context;

    private OnSegmentClickListener onSegmentClickListener;

    public interface OnSegmentClickListener {
        void OnSegmentClicked(View v, Segment segment);
    }

    public class SegmentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView_name;//, textView_type, textView_price;
        ImageView imageView_icon;

        public SegmentsViewHolder(View itemView) {
            super(itemView);
            this.textView_name = (TextView) itemView.findViewById(R.id.textView_name);
            //this.textView_type = (TextView) itemView.findViewById(R.id.textView_type);
            //this.textView_price = (TextView) itemView.findViewById(R.id.textView_price);
            this.imageView_icon = (ImageView) itemView.findViewById(R.id.imageView_icon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();

            if (onSegmentClickListener != null)
                onSegmentClickListener.OnSegmentClicked(v, mSegments.get(position));
        }
    }

    public SegmentsRecyclerAdapter(Context context, ArrayList<Segment> routes) {
        this.context = context;
        this.mSegments = routes;
    }

    public void setOnRouteClickListener(OnSegmentClickListener onSegmentClickListener) {
        this.onSegmentClickListener = onSegmentClickListener;
    }

    @Override
    public SegmentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_segment, parent, false);
        return new SegmentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SegmentsViewHolder holder, final int listPosition) {
        Segment segment = mSegments.get(listPosition);

        holder.textView_name.setText(segment.getName());
        //holder.textView_type.setText(segment.getTravelMode());
        //holder.textView_price.setText(segment.getDescription());

        if (segment.getIconUrl() != null && !TextUtils.isEmpty(segment.getIconUrl())) {

            ImageCacheUtil imageCacheUtil = ImageCacheUtil.getInstance();

            Drawable image = imageCacheUtil.getImage(segment.getIconUrl());

            if (image != null) {
                holder.imageView_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                holder.imageView_icon.setImageDrawable(image);
            } else {
                new HttpImageRequestTask(holder.imageView_icon).execute(segment.getIconUrl());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mSegments.size();
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
