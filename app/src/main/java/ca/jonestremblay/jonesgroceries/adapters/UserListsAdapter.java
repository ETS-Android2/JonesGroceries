package ca.jonestremblay.jonesgroceries.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.entities.UserList;
import ca.jonestremblay.jonesgroceries.entities.enums.ListType;


/**
 * The Adapter creates ViewHolder objects as needed,
 * and also sets th e data for those views.
 * The process of associating views to their data is called binding.
 */
public class UserListsAdapter extends RecyclerView.Adapter<UserListsAdapter.ViewHolder> {
    private static final String TAG = "UserListsListAdapter";
    private MutableLiveData<List<UserList>> listOfUserLists;
    private Context context;
    private List<UserList> userLists;
    private HandleUserListClick clickListener;
    private ListType typeList;

    public UserListsAdapter(Context context, HandleUserListClick clickListener, ListType typeList){
        this.context = context;
        this.clickListener = clickListener;
        this.typeList = typeList;
    }

    public UserListsAdapter(Context context, HandleUserListClick clickListener,
                            List<UserList> userLists, ListType typeList){
        this.context = context;
        this.clickListener = clickListener;
        this.userLists = userLists;
        this.typeList = typeList;
    }

    public void setUserLists(List<UserList> userLists){
        this.userLists = userLists;
            notifyDataSetChanged();
    }

    public void setTypeList(ListType typeList) {
        this.typeList = typeList;
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
        View view = LayoutInflater.from(context).inflate(R.layout.user_list_row, parent, false);
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
        holder.grocery_name.setText(this.userLists.get(position).getListName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.itemClick(userLists.get(position));
            }
        });

        /** Ici on set les listeners des boutons de l'item ( delete and edit )*/
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.editItem(userLists.get(position));
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.removeItem(userLists.get(position));
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
        if (userLists == null || userLists.size() == 0){
            return 0;
        } else {
            return userLists.size();
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

    public interface HandleUserListClick {
        void itemClick(UserList userList);
        void removeItem(UserList userList);
        void editItem(UserList userList);
    }
}
