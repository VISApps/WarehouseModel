package org.visapps.warehousemodel.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.visapps.warehousemodel.R;
import org.visapps.warehousemodel.models.Product;
import org.visapps.warehousemodel.models.Result;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder>{

    public interface ItemsCallback{
        void onClick(Result result);
        void onRemove(Result result);
    }

    private List<Result> items;
    private Context context;
    private ItemsCallback callback;


    public void setCallback(ItemsCallback callback) {
        this.callback = callback;
    }

    public ResultsAdapter(Context context){
        this.context = context;
        items = new ArrayList<>();
    }

    public void setItems(List<Result> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }


    public void addItems(List<Result> items){
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }


    @Override
    public ResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.result_item, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultsAdapter.ViewHolder holder, int position) {
        Result result = items.get(position);
        holder.name.setText(convertTime(result.getDatetime()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(date);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View resultcontainer;
        TextView name;
        ImageButton delete;


        public ViewHolder(View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onRemove(items.get(getAdapterPosition()));
                }
            });
            name = itemView.findViewById(R.id.name);
            resultcontainer = itemView.findViewById(R.id.resultcontainer);
            resultcontainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onClick(items.get(getAdapterPosition()));
                }
            });
        }



    }

}
