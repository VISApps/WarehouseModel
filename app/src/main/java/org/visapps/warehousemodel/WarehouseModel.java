package org.visapps.warehousemodel;

import android.app.Application;

import java.util.Random;

public class WarehouseModel extends Application{

    public static WarehouseModel instance;
    private Random random;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        random = new Random(System.currentTimeMillis());
    }

    public static WarehouseModel getInstance(){
        return instance;
    }

    public Random getRandom(){
        return random;
    }
}
