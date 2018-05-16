package org.visapps.warehousemodel.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable{

    private String id;
    private String name;

    public Customer(){

    }

    public Customer(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer(Parcel in){
        String[] data = new String[2];
        in.readStringArray(data);
        this.id = data[0];
        this.name = data[1];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.id,
                this.name,
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
}
