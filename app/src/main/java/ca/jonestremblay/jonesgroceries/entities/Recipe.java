package ca.jonestremblay.jonesgroceries.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey
    @ColumnInfo(name = "list_id")
    public int listId;
    @ColumnInfo(name = "recipe_name")
    public String recipeName;
    @ColumnInfo(name = "icon_id")
    public int iconId;
    @Ignore
    public ArrayList<RowItem> items;
    public boolean completed;

    public Recipe(){
        isNotCompleted();
    }

    public Recipe(int listId, String recipeName, int iconId, ArrayList<RowItem> items) {
        this.listId = listId;
        this.recipeName = recipeName;
        this.iconId = iconId;
        this.items = items;
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

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String groceryName) {
        this.recipeName = groceryName;
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
