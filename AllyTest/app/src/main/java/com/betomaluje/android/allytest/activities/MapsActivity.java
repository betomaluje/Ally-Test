package com.betomaluje.android.allytest.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.betomaluje.android.allytest.R;
import com.betomaluje.android.allytest.adapters.FragmentViewPagerAdapter;
import com.betomaluje.android.allytest.adapters.StopsRecyclerAdapter;
import com.betomaluje.android.allytest.fragments.StopFragment;
import com.betomaluje.android.allytest.models.routes.Route;
import com.betomaluje.android.allytest.models.routes.Segment;
import com.betomaluje.android.allytest.models.routes.Stop;
import com.betomaluje.android.allytest.utils.bus.BusStation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String EXTRA_IMAGE = "com.betomaluje.android.allytest.activities.extraImage";

    private static final String STATE_STARTING_PAGE_POSITION = "state_starting_page_position";
    private static final String STATE_CURRENT_PAGE_POSITION = "state_current_page_position";

    private GoogleMap mMap;

    private ViewPager viewPager;

    private ArrayList<Route> allRoutes;
    private Route route;
    private int position;
    private int mCurrentPosition;

    //user's location
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.postponeEnterTransition(this);

        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        initAppBarLayout();

        position = 0;

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey("position")) {
            position = b.getInt("position", 0);
        }

        mCurrentPosition = position;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        BusStation.getBus().register(this);

        requestLocation();
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

    private void requestLocation() {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);

            //used as example. The power consumption is high
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    private void fillFragments(ArrayList<Route> routes) {

        ArrayList<Fragment> fragments = new ArrayList<>(routes.size());

        for (Route route : routes) {
            StopFragment f = StopFragment.newInstance(route);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Transition move = new Slide();
                move.excludeTarget(android.R.id.statusBarBackground, true);
                move.excludeTarget(android.R.id.navigationBarBackground, true);
                f.setSharedElementEnterTransition(move);
            }

            f.setOnStopClickListener(new StopsRecyclerAdapter.OnStopClickListener() {
                @Override
                public void OnStopClicked(View v, Stop stop) {
                    LatLng point = new LatLng(stop.getLat(), stop.getLng());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13f));
                }
            });

            fragments.add(f);
        }

        final FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(position);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                route = allRoutes.get(position);
                mCurrentPosition = position;
                drawOnMap();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void drawOnMap() {
        if (mMap == null || route == null)
            return;

        mMap.clear();

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

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //mMap.setOnMarkerClickListener(this);

        if (route == null)
            return;

        drawOnMap();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //supportFinishAfterTransition();
                ActivityCompat.finishAfterTransition(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishAfterTransition() {
        Intent data = new Intent();
        data.putExtra(STATE_STARTING_PAGE_POSITION, position);
        data.putExtra(STATE_CURRENT_PAGE_POSITION, mCurrentPosition);
        setResult(RESULT_OK, data);
        super.finishAfterTransition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BusStation.getBus().unregister(this);
    }

    @Override
    protected void onStart() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    /*
    Next methods are used to populate data from the Otto Event Bus.
     */
    @Subscribe
    public void onAllRoutesPassed(ArrayList<Route> routes) {
        this.allRoutes = routes;
        route = allRoutes.get(position);

        fillFragments(routes);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null && mMap != null) {
            LatLng point = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(point)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("You"));
        }

        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
