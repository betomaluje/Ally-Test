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
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.betomaluje.android.allytest.R;
import com.betomaluje.android.allytest.adapters.StopsRecyclerAdapter;
import com.betomaluje.android.allytest.models.routes.Route;
import com.betomaluje.android.allytest.models.routes.Segment;
import com.betomaluje.android.allytest.models.routes.Stop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final String EXTRA_IMAGE = "com.betomaluje.android.allytest.activities.extraImage";

    private GoogleMap mMap;
    private RecyclerView recyclerView_stops;
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

        Bundle b = getIntent().getExtras();

        if (b != null) {
            route = b.getParcelable("route");

            initAppBarLayout();

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            recyclerView_stops = (RecyclerView) findViewById(R.id.recyclerView_stops);
            recyclerView_stops.setHasFixedSize(true);

            //recyclerView_stops.setLayoutManager(new LinearLayoutManager(MapsActivity.this));

            ViewCompat.setTransitionName(recyclerView_stops, EXTRA_IMAGE);

            //we add a divider (instead of creating it on the XML)
            //RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
            //recyclerView_stops.addItemDecoration(itemDecoration);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                recyclerView_stops.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        Log.e("SCROLL", "scroll 1: " + scrollY);
                    }
                });
            } else {
                recyclerView_stops.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        Log.e("SCROLL", "scroll 2: " + dy);
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }
                });
            }

            fillSegments();
        } else {
            Toast.makeText(MapsActivity.this, getString(R.string.error_no_content), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

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

    private void fillSegments() {
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
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        if (route == null)
            return;

        //we need to add each segment to the map

        int length = route.getSegments().size();
        for (int i = 0; i < length; i++) {
            Segment segment = route.getSegments().get(i);

            String markerTitle = "";
            for (Stop stop : segment.getStops()) {
                LatLng point = new LatLng(stop.getLat(), stop.getLng());

                markerTitle = stop.getName() != null && !TextUtils.isEmpty(stop.getName()) ? stop.getName() : "";

                mMap.addMarker(new MarkerOptions().position(point).title(markerTitle));

                //we check if it's our starting point
                if (i == 0) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13f));
                }
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
    public boolean onMarkerClick(Marker marker) {
        //marker.getPosition();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
