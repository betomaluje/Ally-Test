package com.betomaluje.android.allytest.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.betomaluje.android.allytest.R;
import com.betomaluje.android.allytest.models.routes.Route;

import java.util.ArrayList;

/**
 * Created by betomaluje on 12/31/15.
 */
public class RoutesRecyclerAdapter extends RecyclerView.Adapter<RoutesRecyclerAdapter.RouteViewHolder> {

    private Context mContext;
    private ArrayList<Route> mRoutes;
    private OnRouteClickListener onRouteClickListener;

    public interface OnRouteClickListener {
        void OnRouteClicked(View v, Route route);
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewPager viewpager;

        public RouteViewHolder(View itemView) {
            super(itemView);
            this.viewpager = (ViewPager) itemView.findViewById(R.id.viewpager);

            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();

            if (onRouteClickListener != null)
                onRouteClickListener.OnRouteClicked(v, mRoutes.get(position));
        }
    }

    public RoutesRecyclerAdapter(Context context, ArrayList<Route> routes) {
        this.mContext = context;
        this.mRoutes = routes;
    }

    public void setOnRouteClickListener(OnRouteClickListener onRouteClickListener) {
        this.onRouteClickListener = onRouteClickListener;
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_route, parent, false);
        RouteViewHolder cardViewHolder = new RouteViewHolder(view);

        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(final RouteViewHolder holder, final int listPosition) {
        Route route = mRoutes.get(listPosition);

        RoutesPagerAdapter adapter = new RoutesPagerAdapter(mContext, route);
        adapter.setOnRouteClickListener(onRouteClickListener);
        holder.viewpager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

}
