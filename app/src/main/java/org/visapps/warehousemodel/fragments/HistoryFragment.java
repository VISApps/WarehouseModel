package org.visapps.warehousemodel.fragments;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.visapps.warehousemodel.R;
import org.visapps.warehousemodel.activities.ResultActivity;
import org.visapps.warehousemodel.adapters.ResultsAdapter;
import org.visapps.warehousemodel.models.Result;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryFragment extends Fragment {

    private HistoryViewModel mViewModel;
    private ResultsAdapter adapter;
    private SwipeRefreshLayout refresher;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        refresher = view.findViewById(R.id.refresher);
        refresher.setRefreshing(true);
        adapter = new ResultsAdapter(getContext());
        adapter.setCallback(new ResultsAdapter.ItemsCallback() {
            @Override
            public void onClick(Result result) {
                Intent intent = new Intent(getActivity(), ResultActivity.class);
                intent.putExtra("result", result);
                startActivity(intent);
            }

            @Override
            public void onRemove(Result result) {
                askForDelete(result);
            }
        });
        RecyclerView resultslist = view.findViewById(R.id.resultslist);
        resultslist.setLayoutManager(new LinearLayoutManager(getContext()));
        resultslist.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        mViewModel.getResultsData().observe(this, results -> {
            refresher.setRefreshing(false);
            refresher.setEnabled(false);
            if(results !=null){
                adapter.setItems(results);
            }
        });
        mViewModel.getDeleteEvents().observe(this, delete -> {
            if(delete){
                Toast toast = Toast.makeText(getContext(), getString(R.string.resultdeleted), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void askForDelete(Result result){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.confirmation))
                .setMessage(getString(R.string.delete) + " " + convertTime(result.getDatetime()) + "?")
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mViewModel.deleteResult(result.getId());
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true)
                .create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(date);
    }

}
