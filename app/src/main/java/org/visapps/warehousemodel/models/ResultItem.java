package org.visapps.warehousemodel.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ResultItem implements Parcelable{

    private String name;
    private double k;

    public ResultItem(){

    }

    public ResultItem(String name, double k) {
        this.name = name;
        this.k = k;
    }

    public ResultItem(Parcel parcel){
        this.name = parcel.readString();
        this.k = parcel.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(k);
    }

    public static Creator<ResultItem> CREATOR = new Creator<ResultItem>() {

        @Override
        public ResultItem createFromParcel(Parcel source) {
            return new ResultItem(source);
        }

        @Override
        public ResultItem[] newArray(int size) {
            return new ResultItem[size];
        }

    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }
}
