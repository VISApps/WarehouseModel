package org.visapps.warehousemodel.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.visapps.warehousemodel.R;
import org.visapps.warehousemodel.models.Customer;
import org.visapps.warehousemodel.models.Product;

public class CustomerActivity extends AppCompatActivity {

    private TextInputLayout name_layout;
    private TextInputEditText name;
    private String id=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        name_layout = findViewById(R.id.name_layout);
        name = findViewById(R.id.name);
        Customer customer = null;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            customer = extras.getParcelable("customer");
        }
        if(customer != null){
            id = customer.getId();
            name.setText(customer.getName());
        }
        else{
            getSupportActionBar().setTitle(getString(R.string.addcustomer));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveCustomer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCustomer(){
        if(name.getText().toString().equals("")){
            showError();
        }
        else{
            Customer customer = new Customer(name.getText().toString());
            if(id !=null){
                customer.setId(id);
            }
            Intent intent = new Intent();
            intent.putExtra("customer", customer);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void showError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.error))
                .setMessage(getString(R.string.allfieldsarerequired))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true)
                .create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
