package org.visapps.warehousemodel.fragments;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.visapps.warehousemodel.R;
import org.visapps.warehousemodel.activities.CustomerActivity;
import org.visapps.warehousemodel.activities.ProductActivity;
import org.visapps.warehousemodel.adapters.CustomersAdapter;
import org.visapps.warehousemodel.models.Customer;
import org.visapps.warehousemodel.models.Product;
import org.visapps.warehousemodel.models.Result;

import static android.app.Activity.RESULT_OK;

public class CustomersFragment extends Fragment {

    private static final int REQUEST_NEW_CUSTOMER=2001;
    private static final int REQUEST_CUSTOMER_UPDATE=2002;

    private CustomersViewModel mViewModel;
    private CustomersAdapter adapter;
    private SwipeRefreshLayout refresher;

    public static CustomersFragment newInstance() {
        return new CustomersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customers_fragment, container, false);
        refresher = view.findViewById(R.id.refresher);
        refresher.setRefreshing(true);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CustomerActivity.class);
                startActivityForResult(intent,REQUEST_NEW_CUSTOMER);
            }
        });
        adapter = new CustomersAdapter(getContext());
        adapter.setCallback(new CustomersAdapter.ItemsCallback() {
            @Override
            public void onClick(Customer customer) {
                Intent intent = new Intent(getActivity(), CustomerActivity.class);
                intent.putExtra("customer",customer);
                startActivityForResult(intent,REQUEST_CUSTOMER_UPDATE);
            }

            @Override
            public void onRemove(Customer customer) {
                askForDelete(customer);
            }
        });
        RecyclerView customerslist = view.findViewById(R.id.customerslist);
        customerslist.setLayoutManager(new LinearLayoutManager(getContext()));
        customerslist.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CustomersViewModel.class);
        mViewModel.getCustomers().observe(this, customers ->{
            refresher.setRefreshing(false);
            refresher.setEnabled(false);
            if(customers !=null){
                adapter.setItems(customers);
            }
        });
        mViewModel.getCustomerEvents().observe(this, event -> {
            if(event != null){
                String message = "";
                switch(event){
                    case 0:
                        message = getString(R.string.customersaved);
                        break;
                    case 1:
                        message = getString(R.string.customerupdated);
                        break;
                    case 2:
                        message = getString(R.string.customerdeleted);
                        break;
                }
                Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_NEW_CUSTOMER && resultCode==RESULT_OK){
            Customer customer = data.getParcelableExtra("customer");
            mViewModel.storeCustomer(customer);
        }
        if(requestCode==REQUEST_CUSTOMER_UPDATE && resultCode==RESULT_OK){
            Customer customer = data.getParcelableExtra("customer");
            mViewModel.updateCustomer(customer);
        }
    }

    private void askForDelete(Customer customer){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.confirmation))
                .setMessage(getString(R.string.delete) + " " + customer.getName() + "?")
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mViewModel.deleteCustomer(customer.getId());
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true)
                .create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
