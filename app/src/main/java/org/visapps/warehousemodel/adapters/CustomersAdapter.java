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
import org.visapps.warehousemodel.models.Customer;
import org.visapps.warehousemodel.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.ViewHolder>{

    public interface ItemsCallback{
        void onClick(Customer customer);
        void onRemove(Customer customer);
    }

    private List<Customer> items;
    private Context context;
    private ItemsCallback callback;


    public void setCallback(ItemsCallback callback) {
        this.callback = callback;
    }

    public CustomersAdapter(Context context){
        this.context = context;
        items = new ArrayList<>();
    }

    public void setItems(List<Customer> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }


    public void addItems(List<Customer> items){
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }


    @Override
    public CustomersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_item, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomersAdapter.ViewHolder holder, int position) {
        Customer customer = items.get(position);
        holder.name.setText(customer.getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View customercontainer;
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
            customercontainer = itemView.findViewById(R.id.customercontainer);
            customercontainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onClick(items.get(getAdapterPosition()));
                }
            });
        }



    }

}
