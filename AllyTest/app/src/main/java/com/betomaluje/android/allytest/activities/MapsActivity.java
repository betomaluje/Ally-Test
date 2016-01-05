package com.betomaluje.android.allytest.activities;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.betomaluje.android.allytest.R;
import com.betomaluje.android.allytest.adapters.SegmentsRecyclerAdapter;
import com.betomaluje.android.allytest.adapters.StopsRecyclerAdapter;
import com.betomaluje.android.allytest.models.routes.Route;
import com.betomaluje.android.allytest.models.routes.Segment;
import com.betomaluje.android.allytest.models.routes.Stop;
import com.betomaluje.android.allytest.utils.bus.BusStation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_IMAGE = "com.betomaluje.android.allytest.activities.extraImage";

    private GoogleMap mMap;
    private RecyclerView recyclerView_stops, recyclerView_route;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        initAppBarLayout();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        recyclerView_stops = (RecyclerView) findViewById(R.id.recyclerView_stops);
        recyclerView_stops.setHasFixedSize(true);

        recyclerView_route = (RecyclerView) findViewById(R.id.recyclerView_route);

        ViewCompat.setTransitionName(findViewById(R.id.cardView), EXTRA_IMAGE);

        BusStation.getBus().register(this);
    }

    /**
     * This method adjusts the AppBarLayout's height to 70% of the screen's height.
     * It also disables the dragging behaviour to let the user navigate through the map.
     */
    private void initAppBarLayout() {
        //first we disable scroll of AppBarLayout in CoordinatorLayout
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();

        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();

        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }
        });
        params.setBehavior(behavior);

        //now we resize it
        int measuredheight;
        Point size = new Point();
        WindowManager w = getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.getDefaultDisplay().getSize(size);
            measuredheight = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            measuredheight = d.getHeight();
        }

        params.height = (int) (measuredheight * 0.7f);
    }

    /**
     * This method populates the RecyclerView with a header (all segments of a route) and all the stops of the entire route
     */
    private void fillSegments() {
        SegmentsRecyclerAdapter adapterHeader = new SegmentsRecyclerAdapter(MapsActivity.this, route.getSegments());
        LinearLayoutManager layoutManager = new LinearLayoutManager(MapsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_route.setLayoutManager(layoutManager);
        recyclerView_route.setAdapter(adapterHeader);

        StopsRecyclerAdapter adapter = new StopsRecyclerAdapter(MapsActivity.this, route.getSegments());
        recyclerView_stops.setLayoutManager(new LinearLayoutManager(MapsActivity.this));

        adapter.setOnStopClickListener(new StopsRecyclerAdapter.OnStopClickListener() {
            @Override
            public void OnStopClicked(View v, Stop stop) {
                LatLng point = new LatLng(stop.getLat(), stop.getLng());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13f));
            }
        });
        recyclerView_stops.setAdapter(adapter);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //mMap.setOnMarkerClickListener(this);

        if (route == null)
            return;

        //we need to add each segment to the map

        int length = route.getSegments().size();

        //now we add the polyline and a marker each time the segment changes
        for (int i = 0; i < length; i++) {
            Segment segment = route.getSegments().get(i);

            //we only put the first marker of each stop on the map
            Stop stop = segment.getStops().get(0);
            LatLng point = new LatLng(stop.getLat(), stop.getLng());
            mMap.addMarker(new MarkerOptions().position(point).title(stop.getStopName()));

            //we check if it's our starting point
            if (i == 0) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13f));
            }

            if (segment.getPolyline() != null && !TextUtils.isEmpty(segment.getPolyline())) {
                List<LatLng> line = PolyUtil.decode(segment.getPolyline());
                mMap.addPolyline(new PolylineOptions()
                        .addAll(line)
                        .color(Color.parseColor(segment.getColor())));
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //not finish because it didn't restore the animation
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BusStation.getBus().unregister(this);
    }

    /*
    Next two methods are used to populate data from the Otto Event Bus.
     */
    @Subscribe
    public void onAllRoutesPassed(ArrayList<Route> routes) {

    }

    @Subscribe
    public void onSelectedRoutePassed(Route route) {
        this.route = route;

        fillSegments();
    }

}
