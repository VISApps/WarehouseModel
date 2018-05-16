package org.visapps.warehousemodel.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.visapps.warehousemodel.R;
import org.visapps.warehousemodel.models.Result;
import org.visapps.warehousemodel.models.ResultItem;
import org.visapps.warehousemodel.utils.ProductsLabelFormatter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private PieChart totalchart;
    private BarChart productschart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        totalchart = findViewById(R.id.totalchart);
        productschart = findViewById(R.id.productschart);
        TextView days = findViewById(R.id.days);
        Result result = null;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            result = extras.getParcelable("result");
        }
        if(result != null){
            getSupportActionBar().setTitle(convertTime(result.getDatetime()));
            days.setText(getString(R.string.days) + ": " + String.valueOf(result.getDays()));
            initBar(result.getItems());
            initPie(result.getL());
        }
        else{
            finish();
        }
    }

    private void initBar(List<ResultItem> items){
        List<BarEntry> entries = new ArrayList<>();
        float x = 0f;
        for(ResultItem item : items){
            entries.add(new BarEntry(x,(float) item.getK()));
            x = x + 1f;
        }
        BarDataSet set = new BarDataSet(entries, getString(R.string.products));
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData data = new BarData(set);
        productschart.setData(data);
        productschart.setFitBars(true);
        productschart.getXAxis().setValueFormatter(new ProductsLabelFormatter(items));
        productschart.getXAxis().setGranularity(1f);
        productschart.getXAxis().setGranularityEnabled(true);
        productschart.getDescription().setText(getString(R.string.percentageoffailedordersbyproducts));
        productschart.invalidate();
    }

    private void initPie(double l){
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) l, getString(R.string.notdone)));
        entries.add(new PieEntry((float) (100 - l), getString(R.string.done)));
        PieDataSet set = new PieDataSet(entries, getString(R.string.orders));
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(set);
        totalchart.setData(data);
        totalchart.getDescription().setText(getString(R.string.percentageoffailedorders));
        totalchart.invalidate();
    }

    private String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(date);
    }

}
