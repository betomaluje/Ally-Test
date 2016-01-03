package com.betomaluje.android.allytest.models.provider_attributes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by betomaluje on 12/30/15.
 */
public class Drivenow {

    @SerializedName("provider_icon_url")
    @Expose
    private String providerIconUrl;
    @SerializedName("disclaimer")
    @Expose
    private String disclaimer;
    @SerializedName("ios_itunes_url")
    @Expose
    private String iosItunesUrl;
    @SerializedName("ios_app_url")
    @Expose
    private String iosAppUrl;
    @SerializedName("android_package_name")
    @Expose
    private String androidPackageName;
    @SerializedName("display_name")
    @Expose
    private String displayName;

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

    /**
     * @return The iosItunesUrl
     */
    public String getIosItunesUrl() {
        return iosItunesUrl;
    }

    /**
     * @param iosItunesUrl The ios_itunes_url
     */
    public void setIosItunesUrl(String iosItunesUrl) {
        this.iosItunesUrl = iosItunesUrl;
    }

    /**
     * @return The iosAppUrl
     */
    public String getIosAppUrl() {
        return iosAppUrl;
    }

    /**
     * @param iosAppUrl The ios_app_url
     */
    public void setIosAppUrl(String iosAppUrl) {
        this.iosAppUrl = iosAppUrl;
    }

    /**
     * @return The androidPackageName
     */
    public String getAndroidPackageName() {
        return androidPackageName;
    }

    /**
     * @param androidPackageName The android_package_name
     */
    public void setAndroidPackageName(String androidPackageName) {
        this.androidPackageName = androidPackageName;
    }

    /**
     * @return The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName The display_name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
