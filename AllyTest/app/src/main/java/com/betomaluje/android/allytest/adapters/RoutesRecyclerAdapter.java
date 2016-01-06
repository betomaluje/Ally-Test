package com.betomaluje.android.allytest.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.betomaluje.android.allytest.R;
import com.betomaluje.android.allytest.models.routes.Route;

import java.util.ArrayList;

/**
 * Normal RecyclerView Adapter. It has another RecyclerView and shows Horizontally each segment
 * <p/>
 * Created by betomaluje on 12/31/15.
 */
public class RoutesRecyclerAdapter extends RecyclerView.Adapter<RoutesRecyclerAdapter.RouteViewHolder> {

    private Context mContext;

    private ArrayList<Route> mRoutes;
    private OnRouteClickListener onRouteClickListener;

    public interface OnRouteClickListener {
        void OnRouteClicked(View v, Route route, int position);
    }

    public RoutesRecyclerAdapter(Context context, ArrayList<Route> routes) {
        this.mContext = context;
        this.mRoutes = routes;
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerView recyclerView_route;

        public RouteViewHolder(View itemView) {
            super(itemView);
            this.recyclerView_route = (RecyclerView) itemView.findViewById(R.id.recyclerView_route);

            itemView.findViewById(R.id.view_dummy).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();

            if (onRouteClickListener != null)
                onRouteClickListener.OnRouteClicked(v, mRoutes.get(position), position);
        }

    }

    public void setOnRouteClickListener(OnRouteClickListener onRouteClickListener) {
        this.onRouteClickListener = onRouteClickListener;
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_route, parent, false);

        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RouteViewHolder holder, final int listPosition) {
        Route route = mRoutes.get(listPosition);

        SegmentsRecyclerAdapter adapter = new SegmentsRecyclerAdapter(mContext, route.getSegments());
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView_route.setLayoutManager(layoutManager);
        holder.recyclerView_route.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

}
