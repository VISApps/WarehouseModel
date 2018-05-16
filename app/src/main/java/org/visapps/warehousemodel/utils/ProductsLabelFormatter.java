package org.visapps.warehousemodel.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.visapps.warehousemodel.models.Result;
import org.visapps.warehousemodel.models.ResultItem;

import java.util.List;

public class ProductsLabelFormatter implements IAxisValueFormatter {

    private List<ResultItem> items;

    public ProductsLabelFormatter(List<ResultItem> items){
        this.items = items;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return items.get((int) value).getName();
    }


}
