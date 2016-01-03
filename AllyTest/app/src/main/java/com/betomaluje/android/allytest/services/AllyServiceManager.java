package com.betomaluje.android.allytest.services;

import android.content.Context;

import com.betomaluje.android.allytest.models.AllyRequest;
import com.betomaluje.android.allytest.models.provider_attributes.ProviderAttributes;
import com.betomaluje.android.allytest.models.routes.Route;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by betomaluje on 12/30/15.
 */
public class AllyServiceManager {

    private static AllyServiceManager instance;

    private AllyRequest allyRequest;

    public static AllyServiceManager getInstance() {
        if (instance == null)
            instance = new AllyServiceManager();

        return instance;
    }

    public AllyRequest init(Context context) {
        if (allyRequest == null)
            allyRequest = getRequest(context);

        return allyRequest;
    }

    private AllyRequest getRequest(Context context) {
        try {
            InputStream is = context.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            return gson.fromJson(json, AllyRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Route> getRoutes() {
        return allyRequest.getRoutes();
    }

    public ProviderAttributes getProviderAttributes() {
        return allyRequest.getProviderAttributes();
    }
}
