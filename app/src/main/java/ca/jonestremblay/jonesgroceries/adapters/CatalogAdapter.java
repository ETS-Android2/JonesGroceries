package ca.jonestremblay.jonesgroceries.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.entities.ListItem;
import ca.jonestremblay.jonesgroceries.entities.Product;
import ca.jonestremblay.jonesgroceries.entities.UserList;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

    private static final String TAG = "CatalogAdapter";
    private Context context;
    private Resources resources;
    private List<Product> productsList;
    private int tempLastIconID;

    public CatalogAdapter(Context context){
        this.context = context;
        this.productsList = null;
        this.resources = context.getResources();
        tempLastIconID = 0;
    }

    public CatalogAdapter(Context context, List<Product> listOfProducts){
        this.context = context;
        this.productsList = listOfProducts;
        this.resources = context.getResources();
        tempLastIconID = 0;

    }

    public void setProductsList(List<Product> productsList){
        this.productsList = productsList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public CatalogAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.catalog_row, parent, false);
        return new CatalogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CatalogAdapter.ViewHolder holder, int position) {
        /* doitbind les bon obj ici */
        String[] colors = new String[]{
                "#e6194b", "#3cb44b", "#ffe119", "#4363d8", "#f58231",
                "#911eb4", "#46f0f0", "#f032e6", "#bcf60c", "#fabebe",
                "#008080", "#e6beff", "#9a6324", "#fffac8", "#800000",
                "#aaffc3", "#808000", "#ffd8b1", "#000075", "#808080",
                "#ffffff", "#858182", "#D4E9B9", "#3D7397", "#CAE8CE",
                "#D60034", "#AA6746", "#9E5585", "#BA6200", "#999999"
        };
        Product product = productsList.get(position);
        int iconID = productsList.get(position).category.icon_id;
        int imageID = context.getResources().getIdentifier("ic_" +  String.valueOf(iconID),
                "drawable", context.getPackageName());
        holder.itemIcon.setImageResource(imageID);
        holder.itemName.setText(productsList.get(position).name);

        holder.itemView.setBackgroundColor(Color.parseColor(colors[product.category.icon_id]));


        tempLastIconID = product.category.icon_id;

    }

    @Override
    public int getItemCount() {
        if (productsList == null ||productsList.size() == 0){
            return 0;
        } else {
            return productsList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView itemIcon;
        TextView itemName;

        public ViewHolder(View view){
            super(view);
            itemIcon = view.findViewById(R.id.item_icon);
            itemName = view.findViewById(R.id.item_name);
        }
    }
}
