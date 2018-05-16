package org.visapps.warehousemodel.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.visapps.warehousemodel.models.Product;
import org.visapps.warehousemodel.models.Result;
import org.visapps.warehousemodel.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HistoryViewModel extends ViewModel {

    private DatabaseReference resultsdb;
    private MutableLiveData<List<Result>> resultsdata;
    private SingleLiveEvent<Boolean> deleteevents;

    public HistoryViewModel(){
        super();
        resultsdb = FirebaseDatabase.getInstance().getReference("results");
    }

    public LiveData<List<Result>> getResultsData(){
        if(resultsdata == null){
            resultsdata = new MutableLiveData<>();
            loadResults();
        }
        return resultsdata;
    }

    public LiveData<Boolean> getDeleteEvents(){
        if(deleteevents == null){
            deleteevents = new SingleLiveEvent<>();
        }
        return deleteevents;
    }

    private void loadResults(){
        Query resultsquery = resultsdb.orderByKey();
        resultsquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Result> results = new ArrayList<>();
                for(DataSnapshot productsnapshot : dataSnapshot.getChildren()){
                    results.add(productsnapshot.getValue(Result.class));
                }
                Collections.reverse(results);
                resultsdata.postValue(results);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteResult(String id){
        resultsdb.child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                deleteevents.postValue(true);
            }
        });
    }


}
