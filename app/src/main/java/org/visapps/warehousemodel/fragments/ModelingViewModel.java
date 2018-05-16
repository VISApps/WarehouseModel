package org.visapps.warehousemodel.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.visapps.warehousemodel.models.Customer;
import org.visapps.warehousemodel.models.Modeling;
import org.visapps.warehousemodel.models.Product;
import org.visapps.warehousemodel.models.Result;
import org.visapps.warehousemodel.utils.SingleLiveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ModelingViewModel extends ViewModel {

    private DatabaseReference productsdb;
    private DatabaseReference customersdb;
    private DatabaseReference resultsdb;
    private CompositeDisposable disposables;

    private SingleLiveEvent<Result> resultevents;
    private MutableLiveData<Boolean> progressevents;
    private MutableLiveData<Throwable> exceptionevents;

    public ModelingViewModel(){
        super();
        productsdb = FirebaseDatabase.getInstance().getReference("products");
        customersdb = FirebaseDatabase.getInstance().getReference("customers");
        resultsdb = FirebaseDatabase.getInstance().getReference("results");
        disposables = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public LiveData<Result> getResultEvents(){
        if(resultevents == null){
            resultevents = new SingleLiveEvent<>();
        }
        return resultevents;
    }

    public LiveData<Boolean> getProgressEvents(){
        if(progressevents == null){
            progressevents = new MutableLiveData<>();
        }
        return progressevents;
    }

    public LiveData<Throwable> getExceptionEvents(){
        if(exceptionevents == null){
            exceptionevents = new MutableLiveData<>();
        }
        return exceptionevents;
    }

    public void startModeling(int days){
        progressevents.postValue(true);
        disposables.add(Single
                .zip(loadProducts(),loadCustomers(), (products,customers) -> {
                    Modeling modeling = new Modeling();
                    modeling.init(products,customers,days);
                    return modeling.getResult();
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    uploadResult(res);
                    progressevents.postValue(false);
                    resultevents.postValue(res);
                }, throwable -> {
                    progressevents.postValue(false);
                    exceptionevents.postValue(throwable);
                }));
    }

    private Single<List<Product>> loadProducts(){
        return Single.create(emitter -> {
            productsdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Product> products = new ArrayList<>();
                    for(DataSnapshot productsnapshot : dataSnapshot.getChildren()){
                        products.add(productsnapshot.getValue(Product.class));
                    }
                    emitter.onSuccess(products);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    emitter.onError(new IOException());
                }
            });
        });
    }

    private Single<List<Customer>> loadCustomers(){
        return Single.create(emitter -> {
            customersdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Customer> customers = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        customers.add(snapshot.getValue(Customer.class));
                    }
                    emitter.onSuccess(customers);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    emitter.onError(new IOException());
                }
            });
        });
    }

    private void uploadResult(Result result){
        String id = resultsdb.push().getKey();
        result.setId(id);
        resultsdb.child(id).setValue(result);
    }
}
