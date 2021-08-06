package ca.jonestremblay.jonesgroceries.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Entity(tableName = "categories")
public class Category {
//    @PrimaryKey
//    @NonNull
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
        return "Category{" +
                "category='" + category_name + '\'' +
                ", icon=" + icon_id +
                '}';
    }
}
