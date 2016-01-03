package com.betomaluje.android.allytest.models.routes;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by betomaluje on 12/30/15.
 */
public class Segment implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("num_stops")
    @Expose
    private int numStops;
    @SerializedName("stops")
    @Expose
    private List<Stop> stops = new ArrayList<Stop>();
    @SerializedName("travel_mode")
    @Expose
    private String travelMode;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;
    @SerializedName("polyline")
    @Expose
    private String polyline;

    /**
     * @return The name
     */
    public String getName() {
        return name != null && !TextUtils.isEmpty(name) ? name : "";
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The numStops
     */
    public int getNumStops() {
        return numStops;
    }

    /**
     * @param numStops The num_stops
     */
    public void setNumStops(int numStops) {
        this.numStops = numStops;
    }

    /**
     * @return The stops
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     * @param stops The stops
     */
    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    /**
     * @return The travelMode
     */
    public String getTravelMode() {
        return travelMode != null && !TextUtils.isEmpty(travelMode) ? travelMode : "";
    }

    /**
     * @param travelMode The travel_mode
     */
    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description != null && !TextUtils.isEmpty(description) ? description : "";
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color The color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return The iconUrl
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * @param iconUrl The icon_url
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     * @return The polyline
     */
    public String getPolyline() {
        return polyline;
    }

    /**
     * @param polyline The polyline
     */
    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    protected Segment(Parcel in) {
        name = in.readString();
        numStops = in.readInt();
        if (in.readByte() == 0x01) {
            stops = new ArrayList<Stop>();
            in.readList(stops, Stop.class.getClassLoader());
        } else {
            stops = null;
        }
        travelMode = in.readString();
        description = in.readString();
        color = in.readString();
        iconUrl = in.readString();
        polyline = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(numStops);
        if (stops == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(stops);
        }
        dest.writeString(travelMode);
        dest.writeString(description);
        dest.writeString(color);
        dest.writeString(iconUrl);
        dest.writeString(polyline);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Segment> CREATOR = new Parcelable.Creator<Segment>() {
        @Override
        public Segment createFromParcel(Parcel in) {
            return new Segment(in);
        }

        @Override
        public Segment[] newArray(int size) {
            return new Segment[size];
        }
    };
}
