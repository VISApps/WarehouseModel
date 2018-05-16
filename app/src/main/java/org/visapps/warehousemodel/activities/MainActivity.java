package org.visapps.warehousemodel.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.visapps.warehousemodel.R;
import org.visapps.warehousemodel.fragments.CustomersFragment;
import org.visapps.warehousemodel.fragments.HistoryFragment;
import org.visapps.warehousemodel.fragments.ModelingFragment;
import org.visapps.warehousemodel.fragments.ProductsFragment;
import org.visapps.warehousemodel.utils.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    private static final int FRAGMENT_MODELING = 0;
    private static final int FRAGMENT_PRODUCTS = 1;
    private static final int FRAGMENT_CUSTOMERS = 2;
    private static final int FRAGMENT_HISTORY = 3;

    private int selectedfragment = FRAGMENT_MODELING;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_modeling:
                    if(selectedfragment != FRAGMENT_MODELING){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentscontainer, new ModelingFragment()).commit();
                        getSupportActionBar().setTitle(getString(R.string.title_modeling));
                        selectedfragment = FRAGMENT_MODELING;
                    }
                    return true;
                case R.id.navigation_products:
                    if(selectedfragment != FRAGMENT_PRODUCTS){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentscontainer, new ProductsFragment()).commit();
                        getSupportActionBar().setTitle(getString(R.string.title_products));
                        selectedfragment = FRAGMENT_PRODUCTS;
                    }
                    return true;
                case R.id.navigation_customers:
                    if(selectedfragment != FRAGMENT_CUSTOMERS){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentscontainer, new CustomersFragment()).commit();
                        getSupportActionBar().setTitle(getString(R.string.title_customers));
                        selectedfragment = FRAGMENT_CUSTOMERS;
                    }
                    return true;
                case R.id.navigation_history:
                    if(selectedfragment != FRAGMENT_HISTORY){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentscontainer, new HistoryFragment()).commit();
                        getSupportActionBar().setTitle(getString(R.string.title_history));
                        selectedfragment = FRAGMENT_HISTORY;
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState != null) {
            selectedfragment = savedInstanceState.getInt("selectedfragment");
        }
        switch(selectedfragment){
            case FRAGMENT_MODELING:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentscontainer, new ModelingFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.title_modeling));
                break;
            case FRAGMENT_PRODUCTS:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentscontainer, new ProductsFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.title_products));
                break;
            case FRAGMENT_CUSTOMERS:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentscontainer, new CustomersFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.title_customers));
                break;
            case FRAGMENT_HISTORY:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentscontainer, new HistoryFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.title_history));
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedfragment", selectedfragment);
    }

}
