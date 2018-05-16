package org.visapps.warehousemodel.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private String id;
    private String name;
    private String currentquantity;
    private String minimalquantity;
    private String orderquantity;

    public Product(){

    }

    public Product(String name, String currentquantity, String minimalquantity, String orderquantity) {
        this.name = name;
        this.currentquantity = currentquantity;
        this.minimalquantity = minimalquantity;
        this.orderquantity = orderquantity;
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

    public String getCurrentquantity() {
        return currentquantity;
    }

    public void setCurrentquantity(String currentquantity) {
        this.currentquantity = currentquantity;
    }

    public String getMinimalquantity() {
        return minimalquantity;
    }

    public void setMinimalquantity(String minimalquantity) {
        this.minimalquantity = minimalquantity;
    }

    public String getOrderquantity() {
        return orderquantity;
    }

    public void setOrderquantity(String orderquantity) {
        this.orderquantity = orderquantity;
    }

    public Product(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        this.id = data[0];
        this.name = data[1];
        this.currentquantity = data[2];
        this.minimalquantity = data[3];
        this.orderquantity = data[4];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.id,
                this.name,
                this.currentquantity,
                this.minimalquantity,
                this.orderquantity
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };




}
