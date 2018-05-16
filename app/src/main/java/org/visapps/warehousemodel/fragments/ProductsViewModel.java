package org.visapps.warehousemodel.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.visapps.warehousemodel.models.Product;
import org.visapps.warehousemodel.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

public class ProductsViewModel extends ViewModel {

    private DatabaseReference productsdb;
    private MutableLiveData<List<Product>> productsdata;
    private SingleLiveEvent<Integer> productsevents;

    public ProductsViewModel(){
        productsdb = FirebaseDatabase.getInstance().getReference("products");
    }

    public LiveData<List<Product>> getProducts(){
        if (productsdata == null) {
            productsdata = new MutableLiveData<>();
            loadProducts();
        }
        return productsdata;
    }

    public LiveData<Integer> getProductsEvents(){
        if(productsevents == null){
            productsevents = new SingleLiveEvent<>();
        }
        return productsevents;
    }

    private void loadProducts(){
        productsdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Product> products = new ArrayList<>();
                for(DataSnapshot productsnapshot : dataSnapshot.getChildren()){
                    products.add(productsnapshot.getValue(Product.class));
                }
                productsdata.postValue(products);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void storeProduct(Product product){
        String id = productsdb.push().getKey();
        product.setId(id);
        productsdb.child(id).setValue(product, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                productsevents.postValue(0);
            }
        });
    }

    public void updateProduct(Product product){
        productsdb.child(product.getId()).setValue(product, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                productsevents.postValue(1);
            }
        });
    }

    public void deleteProduct(String id){
        productsdb.child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                productsevents.postValue(2);
            }
        });
    }
}
