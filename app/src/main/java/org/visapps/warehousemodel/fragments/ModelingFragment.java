package org.visapps.warehousemodel.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import org.visapps.warehousemodel.R;
import org.visapps.warehousemodel.activities.ResultActivity;
import org.visapps.warehousemodel.utils.FewCustomersException;
import org.visapps.warehousemodel.utils.FewProductsException;
import org.visapps.warehousemodel.utils.IncNumberOfDaysException;
import org.visapps.warehousemodel.utils.IncRequestsException;

import java.io.IOException;

public class ModelingFragment extends Fragment {

    private ModelingViewModel mViewModel;
    private TextInputEditText numberofdays,minrequest,maxrequest;
    private ProgressDialog progressDialog;
    private Button start;

    public static ModelingFragment newInstance() {
        return new ModelingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modeling_fragment, container, false);
        numberofdays = view.findViewById(R.id.numberofdays);
        minrequest = view.findViewById(R.id.minrequest);
        maxrequest = view.findViewById(R.id.maxrequest);
        numberofdays.setText("3");
        minrequest.setText("1");
        maxrequest.setText("10");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.modelinginprogress));
        start = view.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startModeling();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ModelingViewModel.class);
        mViewModel.getResultEvents().observe(this, result -> {
            Intent intent = new Intent(getActivity(), ResultActivity.class);
            intent.putExtra("result", result);
            startActivity(intent);
        });
        mViewModel.getProgressEvents().observe(this, progress -> {
            start.setEnabled(!progress);
            if(progress){
                if(!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }
            else{
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
        mViewModel.getExceptionEvents().observe(this, throwable -> {
            handleError(throwable);
        });
    }

    private void startModeling(){
        if(numberofdays.getText().toString().equals("") || minrequest.getText().toString().equals("") || maxrequest.getText().toString().equals("")){
            showError(getString(R.string.allfieldsarerequired));
        }
        else{
            mViewModel.startModeling(Integer.valueOf(numberofdays.getText().toString()),
                    Integer.valueOf(minrequest.getText().toString()),
                    Integer.valueOf(maxrequest.getText().toString()));
        }
    }

    private void handleError(Throwable throwable){
        String message = "";
        if(throwable instanceof FewProductsException){
            message = getString(R.string.notenoughproducts);
        }
        else if(throwable instanceof FewCustomersException){
            message = getString(R.string.notenoughcustomers);
        }
        else if(throwable instanceof IOException){
            message = getString(R.string.failedtoloaddata);
        }
        else if(throwable instanceof IncNumberOfDaysException){
            message = getString(R.string.incnumberofdays);
        }
        else if(throwable instanceof IncRequestsException){
            message = getString(R.string.increquests);
        }
        else{
            message = getString(R.string.simulationerror);
        }
        showError(message);
    }

    private void showError(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.error))
                .setMessage(message)
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
