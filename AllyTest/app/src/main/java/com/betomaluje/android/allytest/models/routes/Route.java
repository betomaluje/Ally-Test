package com.betomaluje.android.allytest.models.routes;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by betomaluje on 12/30/15.
 */
public class Route implements Parcelable {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("provider")
    @Expose
    private String provider;
    @SerializedName("segments")
    @Expose
    private ArrayList<Segment> segments = new ArrayList<Segment>();
    @SerializedName("properties")
    @Expose
    private Properties properties;
    @SerializedName("price")
    @Expose
    private Price price;

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The provider
     */
    public String getProvider() {
        return provider != null && !TextUtils.isEmpty(provider) ? provider : "";
    }

    /**
     * @param provider The provider
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * @return The segments
     */
    public ArrayList<Segment> getSegments() {
        return segments;
    }

    /**
     * @param segments The segments
     */
    public void setSegments(ArrayList<Segment> segments) {
        this.segments = segments;
    }

    /**
     * @return The properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * @param properties The properties
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * @return The price
     */
    public Price getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(Price price) {
        this.price = price;
    }


    protected Route(Parcel in) {
        type = in.readString();
        provider = in.readString();
        if (in.readByte() == 0x01) {
            segments = new ArrayList<Segment>();
            in.readList(segments, Segment.class.getClassLoader());
        } else {
            segments = null;
        }
        properties = (Properties) in.readValue(Properties.class.getClassLoader());
        price = (Price) in.readValue(Price.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(provider);
        if (segments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(segments);
        }
        dest.writeValue(properties);
        dest.writeValue(price);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };
}
