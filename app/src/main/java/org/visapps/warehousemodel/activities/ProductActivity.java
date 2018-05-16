package org.visapps.warehousemodel.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.visapps.warehousemodel.R;
import org.visapps.warehousemodel.models.Product;

public class ProductActivity extends AppCompatActivity {

    private TextInputLayout name_layout, currentquantity_layout, minimalquantity_layout, orderquantity_layout;
    private TextInputEditText name,currentquantity,minimalquantity,orderquantity;
    private String id=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        name_layout = findViewById(R.id.name_layout);
        currentquantity_layout = findViewById(R.id.currentquantity_layout);
        minimalquantity_layout = findViewById(R.id.minimalquantity_layout);
        orderquantity_layout = findViewById(R.id.orderquantity_layout);
        name = findViewById(R.id.name);
        currentquantity = findViewById(R.id.currentquantity);
        minimalquantity = findViewById(R.id.minimalquantity);
        orderquantity = findViewById(R.id.orderquantity);
        Product product = null;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            product = extras.getParcelable("product");
        }
        if(product != null){
            id = product.getId();
            name.setText(product.getName());
            currentquantity.setText(product.getCurrentquantity());
            minimalquantity.setText(product.getMinimalquantity());
            orderquantity.setText(product.getOrderquantity());
        }
        else{
            getSupportActionBar().setTitle(getString(R.string.addproduct));
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
            saveProduct();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveProduct(){
        if(name.getText().toString().equals("")
                || currentquantity.getText().toString().equals("")
                || minimalquantity.getText().toString().equals("")
                || orderquantity.getText().toString().equals("")){
            showError();
        }
        else{
            Product product = new Product(name.getText().toString(),currentquantity.getText().toString(),minimalquantity.getText().toString(), orderquantity.getText().toString());
            if(id !=null){
                product.setId(id);
            }
            Intent intent = new Intent();
            intent.putExtra("product", product);
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
