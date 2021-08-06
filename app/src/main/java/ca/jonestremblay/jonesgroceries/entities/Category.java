package ca.jonestremblay.jonesgroceries.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


public class Category {

    public String category_name;
    public int icon_id;

    public Category(){

    }

    public Category(String category, int icon_id) {
        this.category_name = category;
        this.icon_id = icon_id;
    }

    @Override
    public String toString() {
        return this.category_name + "[icon : " + this.icon_id + "]";
    }
}
