package com.betomaluje.android.allytest.adapters;

import android.content.Context;
import android.graphics.Color;
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
 * Created by betomaluje on 1/3/16.
 */
public class StopsRecyclerAdapter extends RecyclerView.Adapter<StopsRecyclerAdapter.StopViewHolder> {

    private ArrayList<Stop> mStops;
    private Context context;

    private OnStopClickListener onStopClickListener;

    public interface OnStopClickListener {
        void OnStopClicked(View v, Stop stop);
    }

    public StopsRecyclerAdapter(Context context, ArrayList<Segment> segment) {
        this.context = context;

        this.mStops = new ArrayList<>();

        for (Segment s : segment) {
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
            int position = getLayoutPosition();

            if (onStopClickListener != null)
                onStopClickListener.OnStopClicked(v, mStops.get(position));
        }
    }

    @Override
    public StopsRecyclerAdapter.StopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_stops, parent, false);
        return new StopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StopsRecyclerAdapter.StopViewHolder holder, int position) {
        Stop stop = mStops.get(position);

        holder.view_color.setBackgroundColor(Color.parseColor(stop.getColor()));
        holder.textView_name.setText(stop.getName());

    }

    @Override
    public int getItemCount() {
        return mStops.size();
    }

}
