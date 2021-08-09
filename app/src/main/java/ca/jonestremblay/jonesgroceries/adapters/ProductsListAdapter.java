package ca.jonestremblay.jonesgroceries.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.dialogs.EditNotesDialog;
import ca.jonestremblay.jonesgroceries.dialogs.QuantityDialog;
import ca.jonestremblay.jonesgroceries.entities.ListItem;


/***
 * Adapter for the recyclerView displaying items_list's table content of a list.
 */
public class ProductsListAdapter extends RecyclerView.Adapter<ProductsListAdapter.ViewHolder> implements AdapterView.OnItemSelectedListener  {
    private static final String TAG = "ProductsListAdapter";
    private Context context;
    private Resources resources;
    private List<ListItem> productsList;
    private HandleProductsClick clickListener;
    public static boolean showIcons;

    public ProductsListAdapter(Context context, HandleProductsClick clickListener){
        this.context = context;
        this.clickListener = clickListener;
        this.productsList = null;
        this.resources = context.getResources();
        showIcons = true;
    }

    public ProductsListAdapter(Context context, HandleProductsClick clickListener, List<ListItem> listOfProducts){
        this.context = context;
        this.clickListener = clickListener;
        this.productsList = listOfProducts;
        this.resources = context.getResources();
        showIcons = true;
    }

    public void setItemsList(List<ListItem> productsList){
        this.productsList = productsList;
        notifyDataSetChanged();
    }

    public static void hideIcons(){
        showIcons = false;
    }

    public static void showIcons(){
        showIcons = true;
    }

    public List<ListItem> getProductsList() {
        return productsList;
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
        View view = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);

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
        ListItem item = productsList.get(position);
        int iconID = productsList.get(position).product.category.icon_id;
        int imageID = context.getResources().getIdentifier("ic_" +  String.valueOf(iconID),
                "drawable", context.getPackageName());
        if (showIcons){
            holder.itemIcon.setImageResource(imageID);
        } else {
            holder.itemIcon.setVisibility(View.GONE);
        }
        holder.itemName.setText(productsList.get(position).product.name);
        holder.itemNotes.setText(item.notes);
        int quantity = productsList.get(position).quantity;
        holder.itemQuantity.setText(quantity + " " + item.measureUnit);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Toggle / invert completed state */
                productsList.get(position).completed = !productsList.get(position).completed;
                if (productsList.get(position).completed){
                    ListItem item = productsList.get(position);
                    holder.itemName.setBackgroundResource(R.drawable.strike_line);
                    holder.itemNotes.setVisibility(View.GONE);
                    holder.itemIcon.setVisibility(View.GONE);
                } else {
                    holder.itemName.setBackground(null);
                    holder.itemNotes.setVisibility(View.VISIBLE);
                    if (showIcons){
                        holder.itemIcon.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final CharSequence[] items = {
                        getString(R.string.contextMenu_AdjustQty),
                        getString(R.string.contextMenu_AddNotes)};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle(getString(R.string.contextMenu_EditItem));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch(item){
                            case 0:
                                showQuantityDialog(position);
                                break;
                            case 1:
                                showEditNotesDialog(position);
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    private void showEditNotesDialog(int position) {
        ListItem itemClicked = productsList.get(position);
        EditNotesDialog editNotesDialog = new EditNotesDialog(context, this , productsList, itemClicked);
    }



    public String getString(int stringID){
        return resources.getString(stringID);
    }

    public void showQuantityDialog(int position){
        ListItem itemClicked = productsList.get(position);
        QuantityDialog quantityDialog = new QuantityDialog(context, this , productsList, itemClicked);
    }
    /**
     * RecyclerView calls this method to get the size of the data set.
     * For example, in an address book app, this might be the total number of addresses.
     * RecyclerView uses this to determine when there are no more items that can be displayed.
     * @return
     */
    @Override
    public int getItemCount() {
        if (productsList == null ||productsList.size() == 0){
            return 0;
        } else {
            return productsList.size();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface HandleProductsClick {
        void productClick(ListItem product);
        void removeProduct(ListItem product);
        void editProduct(ListItem product);
    }

    /**
     * The ViewHolder is a wrapper around a View
     * that contains the layout for an individual item in the list.
     */
    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView itemIcon;
        TextView itemName;
        TextView itemQuantity;
        TextView itemNotes;

        public ViewHolder(View view){
            super(view);
            itemIcon = view.findViewById(R.id.item_icon);
            itemName = view.findViewById(R.id.item_name);
            itemQuantity = view.findViewById(R.id.item_quantity);
            itemNotes = view.findViewById(R.id.item_notes);
        }
    }

}
