package com.betomaluje.android.allytest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.betomaluje.android.allytest.R;
import com.betomaluje.android.allytest.models.routes.Segment;
import com.betomaluje.android.allytest.utils.HttpImageRequestTask;
import com.betomaluje.android.allytest.utils.ImageCacheUtil;

import java.util.ArrayList;

/**
 * Created by betomaluje on 12/31/15.
 */
public class SegmentsRecyclerAdapter extends RecyclerView.Adapter<SegmentsRecyclerAdapter.SegmentsViewHolder> {

    private ArrayList<Segment> mSegments;
    private Context context;

    public SegmentsRecyclerAdapter(Context context, ArrayList<Segment> segment) {
        this.context = context;
        this.mSegments = segment;
    }

    public class SegmentsViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_name;
        public ImageView imageView_icon;

        public View progressBar;

        public SegmentsViewHolder(View itemView) {
            super(itemView);

            this.textView_name = (TextView) itemView.findViewById(R.id.textView_name);
            this.imageView_icon = (ImageView) itemView.findViewById(R.id.imageView_icon);
            this.progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    @Override
    public SegmentsRecyclerAdapter.SegmentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_segment, parent, false);

        return new SegmentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SegmentsRecyclerAdapter.SegmentsViewHolder holder, int position) {
        Segment segment = mSegments.get(position);

        holder.textView_name.setText(segment.getName());

        if (segment.getIconUrl() != null && !TextUtils.isEmpty(segment.getIconUrl())) {

            ImageCacheUtil imageCacheUtil = ImageCacheUtil.getInstance();

            String filename = segment.getIconUrl().replace("https://d3m2tfu2xpiope.cloudfront.net/vehicles/", "").replace(".svg", ".png");

            //first we try getting the image from local saved images
            if (imageCacheUtil.getImageFromFile(holder.imageView_icon, filename)) {
                holder.imageView_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                holder.progressBar.setVisibility(View.GONE);
            } else {
                //if we haven't saved the image previously, we try getting it from our cache system
                Bitmap image = imageCacheUtil.getImage(segment.getIconUrl());

                if (image != null) {
                    holder.imageView_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    holder.imageView_icon.setImageBitmap(image);
                    holder.progressBar.setVisibility(View.GONE);
                } else {
                    new HttpImageRequestTask(holder.imageView_icon, holder.progressBar).execute(segment.getIconUrl());
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return mSegments.size();
    }

}
