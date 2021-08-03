package ca.jonestremblay.jonesgroceries.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.entities.ListItem;


/**
 * The Adapter creates ViewHolder objects as needed,
 * and also sets th e data for those views.
 * The process of associating views to their data is called binding.
 */
public class ProductsListAdapter extends RecyclerView.Adapter<ProductsListAdapter.ViewHolder> {
    private static final String TAG = "ProductsListAdapter";
    private Context context;
    private List<ListItem> productsList;
    private HandleProductsClick clickListener;

    public ProductsListAdapter(Context context, HandleProductsClick clickListener){
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setItemsList(List<ListItem> productsList){
        this.productsList = productsList;
        notifyDataSetChanged();
    }

    /**
     * RecyclerView calls this method whenever it needs to create a new ViewHolder.
     * The method creates and initializes the ViewHolder and its associated View,
     * but does not fill in the view's contents — the ViewHolder
     * has not yet been bound to specific data.
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: JE SUIS EN TRAIN DE CRÉER LE VIEW HOLDER, SANS DATA");
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    /**
     * RecyclerView calls this method to associate a ViewHolder with data.
     * The method fetches the appropriate data
     * and uses the data to fill in the view holder's layout.
     * For example, if the RecyclerView dislays a list of names,
     * the method might find the appropriate name in the list
     * and fill in the view holder's TextView widget.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        /** Ici on bind les rows du reycler view avec du data */
        Log.d(TAG, "onBindViewHolder: JE SUIS EN TRAIN DE BIND LES DATA AVEC LE VIEWHOLDER");
        holder.itemName.setText(this.productsList.get(position).product.name);

        holder.itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productsList.get(position).completed){
                    holder.itemName.setPaintFlags(holder.itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.itemName.setPaintFlags(0);
                }
            }
        });

        /** Ici on set les listeners des boutons de l'item ( delete and edit )*/
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.editProduct(productsList.get(position));
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.removeProduct(productsList.get(position));
            }
        });


    }

    /**
     * RecyclerView calls this method to get the size of the data set.
     * For example, in an address book app, this might be the total number of addresses.
     * RecyclerView uses this to determine when there are no more items that can be displayed.
     * @return
     */
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: JE DIS COMBIEN D'ELEMENT IL Y A DANS LA LISTE");
        if (productsList == null ||productsList.size() == 0){
            Log.d(TAG, "getItemCount: IL Y EN A : null" );
            return 0;
        } else {
            Log.d(TAG, "getItemCount: IL Y EN A : " + productsList.size());
            return productsList.size();
        }
    }

    /**
     * The ViewHolder is a wrapper around a View
     * that contains the layout for an individual item in the list.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView editBtn;
        ImageView deleteBtn;
        TextView itemName;

        public ViewHolder(View view){
            super(view);
            editBtn = view.findViewById(R.id.editItem);
            itemName = view.findViewById(R.id.item_name);
            deleteBtn = view.findViewById(R.id.removeItem);
            Log.d(TAG, "ViewHolder: fin de la construction de l'objet viewholder");
        }
    }

    public interface HandleProductsClick {
        void productClick(ListItem product);
        void removeProduct(ListItem product);
        void editProduct(ListItem product);
    }
}
