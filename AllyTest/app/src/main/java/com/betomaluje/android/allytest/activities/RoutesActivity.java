package com.betomaluje.android.allytest.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;

import com.betomaluje.android.allytest.R;
import com.betomaluje.android.allytest.adapters.RoutesRecyclerAdapter;
import com.betomaluje.android.allytest.models.AllyRequest;
import com.betomaluje.android.allytest.models.routes.Route;
import com.betomaluje.android.allytest.services.AllyServiceManager;
import com.betomaluje.android.allytest.utils.bus.BusStation;
import com.squareup.otto.Produce;

import java.util.ArrayList;

/**
 * Created by betomaluje on 12/30/15.
 */
public class RoutesActivity extends AppCompatActivity {

    private RecyclerView recyclerView_route;
    private Route selectedRoute;
    private AllyRequest allyRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for transitions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition explode = new Explode();
            explode.excludeTarget(android.R.id.statusBarBackground, true);
            explode.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setEnterTransition(explode);
            getWindow().setReturnTransition(explode);

            Transition move = new Slide();
            move.excludeTarget(android.R.id.statusBarBackground, true);
            move.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setSharedElementEnterTransition(move);
        }

        setContentView(R.layout.activity_routes);

        BusStation.getBus().register(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView_route = (RecyclerView) findViewById(R.id.recyclerView_route);
        recyclerView_route.setHasFixedSize(true);

        //we don't need to modify much of the LinearLayoutManager
        recyclerView_route.setLayoutManager(new LinearLayoutManager(RoutesActivity.this));

        fillRoutes();
    }

    private void fillRoutes() {
        allyRequest = AllyServiceManager.getInstance().init(RoutesActivity.this);

        RoutesRecyclerAdapter adapter = new RoutesRecyclerAdapter(RoutesActivity.this, allyRequest.getRoutes());

        adapter.setOnRouteClickListener(new RoutesRecyclerAdapter.OnRouteClickListener() {
            @Override
            public void OnRouteClicked(View v, Route route) {

                selectedRoute = route;

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(RoutesActivity.this, v, MapsActivity.EXTRA_IMAGE);
                ActivityCompat.startActivity(RoutesActivity.this, new Intent(RoutesActivity.this, MapsActivity.class), options.toBundle());

                //now we send it to the event bus
                BusStation.postOnMain(produceSingleRoute());
                BusStation.postToSameThread(produceManyRoute());
            }
        });

        recyclerView_route.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusStation.getBus().unregister(this);
    }

    @Produce
    public Route produceSingleRoute() {
        return selectedRoute;
    }

    @Produce
    public ArrayList<Route> produceManyRoute() {
        return allyRequest.getRoutes();
    }

}
