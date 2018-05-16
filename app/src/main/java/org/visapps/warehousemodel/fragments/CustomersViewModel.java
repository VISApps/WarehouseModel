package org.visapps.warehousemodel.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.visapps.warehousemodel.models.Customer;
import org.visapps.warehousemodel.models.Product;
import org.visapps.warehousemodel.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

public class CustomersViewModel extends ViewModel {

    private DatabaseReference customersdb;
    private MutableLiveData<List<Customer>> customersdata;
    private SingleLiveEvent<Integer> customersevents;

    public CustomersViewModel(){
        customersdb = FirebaseDatabase.getInstance().getReference("customers");
    }

    public LiveData<List<Customer>> getCustomers(){
        if (customersdata == null) {
            customersdata = new MutableLiveData<>();
            loadCustomers();
        }
        return customersdata;
    }

    public LiveData<Integer> getCustomerEvents(){
        if(customersevents == null){
            customersevents = new SingleLiveEvent<>();
        }
        return customersevents;
    }

    private void loadCustomers(){
        customersdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Customer> customers = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    customers.add(snapshot.getValue(Customer.class));
                }
                customersdata.postValue(customers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void storeCustomer(Customer customer){
        String id = customersdb.push().getKey();
        customer.setId(id);
        customersdb.child(id).setValue(customer, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                customersevents.postValue(0);
            }
        });
    }

    public void updateCustomer(Customer customer){
        customersdb.child(customer.getId()).setValue(customer, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                customersevents.postValue(1);
            }
        });
    }

    public void deleteCustomer(String id){
        customersdb.child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                customersevents.postValue(2);
            }
        });
    }
}
