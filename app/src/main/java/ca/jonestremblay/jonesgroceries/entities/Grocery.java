package ca.jonestremblay.jonesgroceries.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "groceries")
public class Grocery {
    @PrimaryKey
    @ColumnInfo(name = "list_id")
    public int listId;
    @ColumnInfo(name = "grocery_name")
    public String groceryName;
    @ColumnInfo(name = "icon_id")
    public int iconId;
    @Ignore
    public ArrayList<RowItem> items;
    public boolean completed;

    @Ignore
    public Grocery(){
        isNotCompleted();
    }

    public Grocery(int listId, String groceryName, int iconId) {
        this.listId = listId;
        this.groceryName = groceryName;
        this.iconId = iconId;
        isNotCompleted();
    }

    public void isCompleted(){
        this.completed = true;
    }
    public void isNotCompleted(){
        this.completed = false;
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

    public ArrayList<RowItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<RowItem> items) {
        this.items = items;
    }
}
