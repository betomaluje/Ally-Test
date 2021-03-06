package com.betomaluje.android.allytest.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.betomaluje.android.allytest.R;
import com.betomaluje.android.allytest.activities.MapsActivity;
import com.betomaluje.android.allytest.adapters.SegmentsRecyclerAdapter;
import com.betomaluje.android.allytest.adapters.StopsRecyclerAdapter;
import com.betomaluje.android.allytest.models.routes.Route;

/**
 * Created by betomaluje on 1/6/16.
 */
public class StopFragment extends Fragment {

    private Context context;

    private Route route;
    private int position;
    private int mStartingPosition;

    private View cardView;
    private RecyclerView recyclerView_stops, recyclerView_route;
    private StopsRecyclerAdapter.OnStopClickListener onStopClickListener;

    public static StopFragment newInstance(Route route, int position, int mStartingPosition) {
        StopFragment fragment = new StopFragment();
        Bundle b = new Bundle();
        b.putParcelable("route", route);
        b.putInt("position", position);
        b.putInt("mStartingPosition", mStartingPosition);
        fragment.setArguments(b);

        return fragment;
    }

    public void setOnStopClickListener(StopsRecyclerAdapter.OnStopClickListener onStopClickListener) {
        this.onStopClickListener = onStopClickListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        Bundle b = getArguments();

        if (b != null) {
            if (b.containsKey("route"))
                this.route = b.getParcelable("route");

            if (b.containsKey("position"))
                this.position = b.getInt("position", 0);

            if (b.containsKey("mStartingPosition"))
                this.mStartingPosition = b.getInt("mStartingPosition", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        recyclerView_stops = (RecyclerView) view.findViewById(R.id.recyclerView_stops);
        recyclerView_stops.setHasFixedSize(true);

        recyclerView_route = (RecyclerView) view.findViewById(R.id.recyclerView_route);
        cardView = view.findViewById(R.id.cardView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setTransitionName(cardView, MapsActivity.EXTRA_IMAGE + position);
        }

        fillSegmentsAndStops();

        startPostponedEnterTransition();
    }

    private void startPostponedEnterTransition() {
        if (position == mStartingPosition) {
            cardView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    cardView.getViewTreeObserver().removeOnPreDrawListener(this);
                    ActivityCompat.startPostponedEnterTransition(getActivity());
                    return true;
                }
            });
        }
    }

    /**
     * This method populates the RecyclerView with a header (all segments of a route) and all the stops of the entire route
     */
    private void fillSegmentsAndStops() {
        SegmentsRecyclerAdapter adapterHeader = new SegmentsRecyclerAdapter(context, route.getSegments());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_route.setLayoutManager(layoutManager);
        recyclerView_route.setAdapter(adapterHeader);

        StopsRecyclerAdapter adapter = new StopsRecyclerAdapter(context, route.getSegments());
        recyclerView_stops.setLayoutManager(new LinearLayoutManager(context));

        if (onStopClickListener != null)
            adapter.setOnStopClickListener(onStopClickListener);

        recyclerView_stops.setAdapter(adapter);
    }

    /**
     * Returns the shared element that should be transitioned back to the previous Activity,
     * or null if the view is not visible on the screen.
     */
    @Nullable
    public View getCardView() {
        if (isViewInBounds(getActivity().getWindow().getDecorView(), cardView)) {
            return cardView;
        }
        return null;
    }

    /**
     * Returns true if {@param view} is contained within {@param container}'s bounds.
     */
    private static boolean isViewInBounds(@NonNull View container, @NonNull View view) {
        Rect containerBounds = new Rect();
        container.getHitRect(containerBounds);
        return view.getLocalVisibleRect(containerBounds);
    }
}
