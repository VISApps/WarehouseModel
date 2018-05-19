package org.visapps.warehousemodel.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Result implements Parcelable{

    private String id;
    private long days;
    private long datetime;
    private long minrequest=-1;
    private long maxrequest=-1;
    private double l;
    private List<ResultItem> items;

    public Result(){

    }

    public Result(long days, long datetime, double l, List<ResultItem> items) {
        this.days = days;
        this.datetime = datetime;
        this.l = l;
        this.items = items;
    }

    public Result(Parcel parcel){
        this.id = parcel.readString();
        this.days = parcel.readLong();
        this.datetime = parcel.readLong();
        this.minrequest = parcel.readLong();
        this.maxrequest = parcel.readLong();
        this.l = parcel.readDouble();
        this.items = new ArrayList<ResultItem>();
        this.items = parcel.readArrayList(ResultItem.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(days);
        dest.writeLong(datetime);
        dest.writeLong(minrequest);
        dest.writeLong(maxrequest);
        dest.writeDouble(l);
        dest.writeList(items);
    }

    public static Creator<Result> CREATOR = new Creator<Result>() {

        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }

    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public List<ResultItem> getItems() {
        return items;
    }

    public void setItems(List<ResultItem> items) {
        this.items = items;
    }

    public double getL() {
        return l;
    }

    public void setL(double l) {
        this.l = l;
    }

    public long getMinrequest() {
        return minrequest;
    }

    public void setMinrequest(long minrequest) {
        this.minrequest = minrequest;
    }

    public long getMaxrequest() {
        return maxrequest;
    }

    public void setMaxrequest(long maxrequest) {
        this.maxrequest = maxrequest;
    }
}
