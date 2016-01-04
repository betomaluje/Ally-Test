package com.betomaluje.android.allytest.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.betomaluje.android.allytest.R;
import com.betomaluje.android.allytest.models.routes.Segment;
import com.betomaluje.android.allytest.models.routes.Stop;

import java.util.ArrayList;

/**
 * Created by betomaluje on 1/3/16.
 */
public class StopsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the viaew under inflation and population is header or Item
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
            mStops.addAll(s.getStops());
        }
    }

    public void setOnStopClickListener(OnStopClickListener onStopClickListener) {
        this.onStopClickListener = onStopClickListener;
    }

    public class StopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int viewType;
        public TextView textView_name, textView_type, textView_price;
        public ImageView imageView_icon;

        public View progressBar;

        public StopViewHolder(View itemView) {
            super(itemView);

            this.textView_name = (TextView) itemView.findViewById(R.id.textView_name);
            this.textView_type = (TextView) itemView.findViewById(R.id.textView_type);
            this.textView_price = (TextView) itemView.findViewById(R.id.textView_price);

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

            vHolder2.textView_name.setText(stop.getName());
            //vHolder2.textView_type.setText(stop.getProperties().getCompanies().get(0).getName());
            vHolder2.textView_price.setText(stop.getDatetime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mStops.size() + 1;
    }

}
