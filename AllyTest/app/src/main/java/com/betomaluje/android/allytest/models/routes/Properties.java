package com.betomaluje.android.allytest.models.routes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by betomaluje on 12/30/15.
 */
public class Properties implements Parcelable {

    @SerializedName("companies")
    @Expose
    private List<Company> companies = new ArrayList<Company>();

    /**
     * @return The companies
     */
    public List<Company> getCompanies() {
        return companies;
    }

    /**
     * @param companies The companies
     */
    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }


    protected Properties(Parcel in) {
        if (in.readByte() == 0x01) {
            companies = new ArrayList<Company>();
            in.readList(companies, Company.class.getClassLoader());
        } else {
            companies = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (companies == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(companies);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Properties> CREATOR = new Parcelable.Creator<Properties>() {
        @Override
        public Properties createFromParcel(Parcel in) {
            return new Properties(in);
        }

        @Override
        public Properties[] newArray(int size) {
            return new Properties[size];
        }
    };
}
