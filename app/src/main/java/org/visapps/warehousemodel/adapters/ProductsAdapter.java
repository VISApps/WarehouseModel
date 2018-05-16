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

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>{

    public interface ItemsCallback{
        void onClick(Product product);
        void onRemove(Product product);
    }

    private List<Product> items;
    private Context context;
    private ItemsCallback callback;


    public void setCallback(ItemsCallback callback) {
        this.callback = callback;
    }

    public ProductsAdapter(Context context){
        this.context = context;
        items = new ArrayList<>();
    }

    public void setItems(List<Product> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }


    public void addItems(List<Product> items){
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }


    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductsAdapter.ViewHolder holder, int position) {
        Product product = items.get(position);
        holder.name.setText(product.getName());
        holder.currentquantity.setText(product.getCurrentquantity());
        holder.minimalquantity.setText(product.getMinimalquantity());
        holder.orderquantity.setText(product.getOrderquantity());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View productcontainer;
        TextView name,currentquantity,minimalquantity,orderquantity;
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
            currentquantity = itemView.findViewById(R.id.currentquantity);
            minimalquantity = itemView.findViewById(R.id.minimalquantity);
            orderquantity = itemView.findViewById(R.id.orderquantity);
            productcontainer = itemView.findViewById(R.id.productcontainer);
            productcontainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onClick(items.get(getAdapterPosition()));
                }
            });
        }



    }

}
