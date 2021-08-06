package ca.jonestremblay.jonesgroceries.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "user_lists", indices = {@Index(value = "list_name", unique = true)})
public class UserList {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "list_id")
    private int listId;

    @NonNull
    @ColumnInfo(name = "list_name")
    private String groceryName;

    @ColumnInfo(name = "icon_id")
    private int iconId;
    @Ignore
    public ArrayList<ListItem> items;

    @ColumnInfo(defaultValue = "0")
    private boolean completed = false;

    @Ignore
    public UserList(){
        this.completed = false;
    }

    public UserList(int listId, String groceryName, int iconId) {
        this.listId = listId;
        this.groceryName = groceryName;
        this.iconId = iconId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getGroceryName() {
        return groceryName;
    }

    public void setGroceryName(String groceryName) {
        this.groceryName = groceryName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public ArrayList<ListItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ListItem> items) {
        this.items = items;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean getCompleted(boolean completed){
        return this.completed;
    }
}
