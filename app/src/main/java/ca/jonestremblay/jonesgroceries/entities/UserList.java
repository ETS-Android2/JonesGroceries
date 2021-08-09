package ca.jonestremblay.jonesgroceries.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

import ca.jonestremblay.jonesgroceries.entities.enums.ListType;

@Entity(tableName = "user_lists", indices = {@Index(value = "list_name", unique = true)})
public class UserList {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "list_id")
    private int listId;

    @NonNull
    @ColumnInfo(name = "list_name")
    private String listName;

    @ColumnInfo(name = "icon_id")
    private int iconId;
    @Ignore
    public ArrayList<ListItem> items;

    @ColumnInfo(defaultValue = "0")
    private boolean completed = false;

    private String type;

    @Ignore
    public UserList(){
        this.completed = false;
    }

    public UserList(int listId, String listName, int iconId, String type) {
        this.listId = listId;
        this.listName = listName;
        this.iconId = iconId;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
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
