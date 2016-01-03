package com.betomaluje.android.allytest.models.provider_attributes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by betomaluje on 12/30/15.
 */
public class Google {

    @SerializedName("provider_icon_url")
    @Expose
    private String providerIconUrl;
    @SerializedName("disclaimer")
    @Expose
    private String disclaimer;

    /**
     * @return The providerIconUrl
     */
    public String getProviderIconUrl() {
        return providerIconUrl;
    }

    /**
     * @param providerIconUrl The provider_icon_url
     */
    public void setProviderIconUrl(String providerIconUrl) {
        this.providerIconUrl = providerIconUrl;
    }

    /**
     * @return The disclaimer
     */
    public String getDisclaimer() {
        return disclaimer;
    }

    /**
     * @param disclaimer The disclaimer
     */
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

}
