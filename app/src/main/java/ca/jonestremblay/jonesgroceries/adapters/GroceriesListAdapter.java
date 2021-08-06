package ca.jonestremblay.jonesgroceries.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.UserList;


/**
 * The Adapter creates ViewHolder objects as needed,
 * and also sets th e data for those views.
 * The process of associating views to their data is called binding.
 */
public class GroceriesListAdapter extends RecyclerView.Adapter<GroceriesListAdapter.ViewHolder> {
    private static final String TAG = "GroceriesViewModel";
    private MutableLiveData<List<UserList>> listOfGroceries;
    AppDatabase appDatabase;
    private Context context;
    private List<UserList> groceriesList;
    private HandleGroceryClick clickListener;

    public GroceriesListAdapter(Context context, HandleGroceryClick clickListener){
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setGroceriesList(List<UserList> groceriesList){
        this.groceriesList = groceriesList;
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
        holder.grocery_name.setText(this.groceriesList.get(position).getGroceryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.itemClick(groceriesList.get(position));
            }
        });

        /** Ici on set les listeners des boutons de l'item ( delete and edit )*/
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.editItem(groceriesList.get(position));
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.removeItem(groceriesList.get(position));
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
        if (groceriesList == null ||groceriesList.size() == 0){
            return 0;
        } else {
            return groceriesList.size();
        }
    }

    /**
     * The ViewHolder is a wrapper around a View
     * that contains the layout for an individual item in the list.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIcon;
        ImageView editBtn;
        ImageView deleteBtn;
        TextView grocery_name;

        public ViewHolder(View view){
            super(view);
            itemIcon = view.findViewById(R.id.itemIcon);
            editBtn = view.findViewById(R.id.editItem);
            grocery_name = view.findViewById(R.id.item_name);
            deleteBtn = view.findViewById(R.id.removeItem);
        }
    }

    public interface HandleGroceryClick {
        void itemClick(UserList userList);
        void removeItem(UserList userList);
        void editItem(UserList userList);
    }
}
