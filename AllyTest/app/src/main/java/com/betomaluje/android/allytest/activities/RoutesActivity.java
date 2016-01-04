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
import android.transition.Slide;
import android.view.View;

import com.betomaluje.android.allytest.R;
import com.betomaluje.android.allytest.adapters.RoutesRecyclerAdapter;
import com.betomaluje.android.allytest.models.AllyRequest;
import com.betomaluje.android.allytest.models.routes.Route;
import com.betomaluje.android.allytest.services.AllyServiceManager;
import com.betomaluje.android.allytest.utils.bus.BusStation;
import com.betomaluje.android.allytest.views.DividerItemDecoration;
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
        setContentView(R.layout.activity_routes);

        BusStation.getBus().register(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView_route = (RecyclerView) findViewById(R.id.recyclerView_route);
        recyclerView_route.setHasFixedSize(true);

        //we add a divider (instead of creating it on the XML)
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView_route.addItemDecoration(itemDecoration);

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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide transition = new Slide();
                    transition.excludeTarget(android.R.id.statusBarBackground, true);
                    getWindow().setEnterTransition(transition);
                    getWindow().setReturnTransition(transition);
                }

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(RoutesActivity.this, v, MapsActivity.EXTRA_IMAGE);
                ActivityCompat.startActivity(RoutesActivity.this, new Intent(RoutesActivity.this, MapsActivity.class), options.toBundle());

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
