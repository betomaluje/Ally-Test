package com.betomaluje.android.allytest.models.provider_attributes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by betomaluje on 12/30/15.
 */
public class ProviderAttributes {

    @SerializedName("vbb")
    @Expose
    private Vbb vbb;
    @SerializedName("drivenow")
    @Expose
    private Drivenow drivenow;
    @SerializedName("car2go")
    @Expose
    private Car2go car2go;
    @SerializedName("google")
    @Expose
    private Google google;
    @SerializedName("nextbike")
    @Expose
    private Nextbike nextbike;
    @SerializedName("callabike")
    @Expose
    private Callabike callabike;

    /**
     * @return The vbb
     */
    public Vbb getVbb() {
        return vbb;
    }

    /**
     * @param vbb The vbb
     */
    public void setVbb(Vbb vbb) {
        this.vbb = vbb;
    }

    /**
     * @return The drivenow
     */
    public Drivenow getDrivenow() {
        return drivenow;
    }

    /**
     * @param drivenow The drivenow
     */
    public void setDrivenow(Drivenow drivenow) {
        this.drivenow = drivenow;
    }

    /**
     * @return The car2go
     */
    public Car2go getCar2go() {
        return car2go;
    }

    /**
     * @param car2go The car2go
     */
    public void setCar2go(Car2go car2go) {
        this.car2go = car2go;
    }

    /**
     * @return The google
     */
    public Google getGoogle() {
        return google;
    }

    /**
     * @param google The google
     */
    public void setGoogle(Google google) {
        this.google = google;
    }

    /**
     * @return The nextbike
     */
    public Nextbike getNextbike() {
        return nextbike;
    }

    /**
     * @param nextbike The nextbike
     */
    public void setNextbike(Nextbike nextbike) {
        this.nextbike = nextbike;
    }

    /**
     * @return The callabike
     */
    public Callabike getCallabike() {
        return callabike;
    }

    /**
     * @param callabike The callabike
     */
    public void setCallabike(Callabike callabike) {
        this.callabike = callabike;
    }

}
