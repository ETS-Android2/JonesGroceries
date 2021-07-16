package ca.jonestremblay.jonesgroceries.adapters;

import android.content.Context;
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
import ca.jonestremblay.jonesgroceries.entities.Category;


/**
 * The Adapter creates ViewHolder objects as needed,
 * and also sets th e data for those views.
 * The process of associating views to their data is called binding.
 */
public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.ViewHolder> {
    private static final String TAG = "CategoriesListAdapter";
    private Context context;
    private List<Category> categoryList;
    private HandleCategoryClick clickListener;

    public CategoriesListAdapter(Context context, HandleCategoryClick clickListener){
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setCategoryList(List<Category> categoryList){
        this.categoryList = categoryList;
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
        holder.category_name.setText(this.categoryList.get(position).category_name);

        holder.category_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.itemClick(categoryList.get(position));
            }
        });

        /** Ici on set les listeners des boutons de l'item ( delete and edit )*/
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.editItem(categoryList.get(position));
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.removeItem(categoryList.get(position));
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
        if (categoryList == null ||categoryList.size() == 0){
            Log.d(TAG, "getItemCount: IL Y EN A : null" );
            return 0;
        } else {
            Log.d(TAG, "getItemCount: IL Y EN A : " + categoryList.size());
            return categoryList.size();
        }
    }

    /**
     * The ViewHolder is a wrapper around a View
     * that contains the layout for an individual item in the list.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIcon;
        ImageView editBtn;
        ImageView deleteBtn;
        TextView category_name;

        public ViewHolder(View view){
            super(view);
            categoryIcon = view.findViewById(R.id.itemIcon);
            editBtn = view.findViewById(R.id.editItem);
            category_name = view.findViewById(R.id.item_name);
            deleteBtn = view.findViewById(R.id.removeItem);
            Log.d(TAG, "ViewHolder: fin de la construction de l'objet viewholder");
        }
    }

    public interface HandleCategoryClick {
        void itemClick(Category category);
        void removeItem(Category category);
        void editItem(Category category);
    }
}
