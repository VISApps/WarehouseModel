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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.visapps.warehousemodel.R;
import org.visapps.warehousemodel.activities.ProductActivity;
import org.visapps.warehousemodel.adapters.ProductsAdapter;
import org.visapps.warehousemodel.models.Product;
import org.visapps.warehousemodel.models.Result;

import static android.app.Activity.RESULT_OK;

public class ProductsFragment extends Fragment {

    private static final int REQUEST_NEW_PRODUCT=1001;
    private static final int REQUEST_PRODUCT_UPDATE=1002;

    private ProductsViewModel mViewModel;
    private ProductsAdapter adapter;
    private SwipeRefreshLayout refresher;

    public static ProductsFragment newInstance() {
        return new ProductsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.products_fragment, container, false);
        refresher = view.findViewById(R.id.refresher);
        refresher.setRefreshing(true);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                startActivityForResult(intent,REQUEST_NEW_PRODUCT);
            }
        });
        adapter = new ProductsAdapter(getContext());
        adapter.setCallback(new ProductsAdapter.ItemsCallback() {
            @Override
            public void onClick(Product product) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("product",product);
                startActivityForResult(intent,REQUEST_PRODUCT_UPDATE);
            }

            @Override
            public void onRemove(Product product) {
                askForDelete(product);
            }
        });
        RecyclerView productslist = view.findViewById(R.id.productslist);
        productslist.setLayoutManager(new LinearLayoutManager(getContext()));
        productslist.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        mViewModel.getProducts().observe(this, products ->{
            refresher.setRefreshing(false);
            refresher.setEnabled(false);
            if(products !=null){
                adapter.setItems(products);
            }
        });
        mViewModel.getProductsEvents().observe(this, event -> {
            if(event != null){
                String message = "";
                switch(event){
                    case 0:
                        message = getString(R.string.productsaved);
                        break;
                    case 1:
                        message = getString(R.string.productupdated);
                        break;
                    case 2:
                        message = getString(R.string.productdeleted);
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
        if(requestCode==REQUEST_NEW_PRODUCT && resultCode==RESULT_OK){
            Product product = data.getParcelableExtra("product");
            mViewModel.storeProduct(product);
        }
        if(requestCode==REQUEST_PRODUCT_UPDATE && resultCode==RESULT_OK){
            Product product = data.getParcelableExtra("product");
            mViewModel.updateProduct(product);
        }
    }

    private void askForDelete(Product product){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.confirmation))
                .setMessage(getString(R.string.delete) + " " + product.getName() + "?")
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mViewModel.deleteProduct(product.getId());
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true)
                .create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
