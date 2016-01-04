package com.betomaluje.android.allytest.models;

import com.betomaluje.android.allytest.models.provider_attributes.ProviderAttributes;
import com.betomaluje.android.allytest.models.routes.Route;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Simple class that stores all routes and provider attributes from the json file
 * <p/>
 * Created by betomaluje on 12/30/15.
 */
public class AllyRequest {

    @SerializedName("routes")
    @Expose
    private ArrayList<Route> routes = new ArrayList<Route>();
    @SerializedName("provider_attributes")
    @Expose
    private ProviderAttributes providerAttributes;

    /**
     * @return The routes
     */
    public ArrayList<Route> getRoutes() {
        return routes;
    }

    /**
     * @param routes The routes
     */
    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    /**
     * @return The providerAttributes
     */
    public ProviderAttributes getProviderAttributes() {
        return providerAttributes;
    }

    /**
     * @param providerAttributes The provider_attributes
     */
    public void setProviderAttributes(ProviderAttributes providerAttributes) {
        this.providerAttributes = providerAttributes;
    }

}
