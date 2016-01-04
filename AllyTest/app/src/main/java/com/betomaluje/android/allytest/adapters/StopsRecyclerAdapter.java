package com.betomaluje.android.allytest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.betomaluje.android.allytest.R;
import com.betomaluje.android.allytest.models.routes.Segment;
import com.betomaluje.android.allytest.models.routes.Stop;

import java.util.ArrayList;

/**
 * Adapter that can handle a header view (a Horizontal RecyclerView) and a list of views.
 * I chose to use a RecyclerView because the route can have multiple segments.
 * <p/>
 * Created by betomaluje on 1/3/16.
 */
public class StopsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Declaring Variable to Understand which View is being worked on
    private static final int TYPE_HEADER = 0;
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private ArrayList<Segment> mSegments;
    private ArrayList<Stop> mStops;
    private Context context;

    private OnStopClickListener onStopClickListener;

    public interface OnStopClickListener {
        void OnStopClicked(View v, Stop stop);
    }

    public StopsRecyclerAdapter(Context context, ArrayList<Segment> segment) {
        this.context = context;
        this.mSegments = segment;

        this.mStops = new ArrayList<>();

        for (Segment s : this.mSegments) {
            for (Stop stop : s.getStops()) {
                stop.setColor(s.getColor());
            }
            mStops.addAll(s.getStops());
        }
    }

    public void setOnStopClickListener(OnStopClickListener onStopClickListener) {
        this.onStopClickListener = onStopClickListener;
    }

    public class StopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View view_color;
        public TextView textView_name;

        public StopViewHolder(View itemView) {
            super(itemView);

            this.view_color = itemView.findViewById(R.id.view_color);
            this.textView_name = (TextView) itemView.findViewById(R.id.textView_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition() - 1;

            if (onStopClickListener != null)
                onStopClickListener.OnStopClicked(v, mStops.get(position));
        }
    }

    public class SegmentsViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView_route;

        public SegmentsViewHolder(View itemView) {
            super(itemView);

            this.recyclerView_route = (RecyclerView) itemView.findViewById(R.id.recyclerView_route);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == TYPE_ITEM) {
            //item
            view = LayoutInflater.from(context).inflate(R.layout.item_stops, parent, false);
            return new StopViewHolder(view);
        } else {
            //header
            view = LayoutInflater.from(context).inflate(R.layout.item_stop_header, parent, false);
            return new SegmentsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_HEADER) {

            SegmentsViewHolder vHolder1 = (SegmentsViewHolder) holder;

            SegmentsRecyclerAdapter adapter = new SegmentsRecyclerAdapter(context, mSegments);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            vHolder1.recyclerView_route.setLayoutManager(layoutManager);
            vHolder1.recyclerView_route.setAdapter(adapter);
        } else {
            StopViewHolder vHolder2 = (StopViewHolder) holder;

            Stop stop = mStops.get(position - 1);

            vHolder2.view_color.setBackgroundColor(Color.parseColor(stop.getColor()));
            vHolder2.textView_name.setText(stop.getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    /**
     * We need to add one because we have a header
     *
     * @return number of rows in the adapter
     */
    @Override
    public int getItemCount() {
        return mStops.size() + 1;
    }

}
