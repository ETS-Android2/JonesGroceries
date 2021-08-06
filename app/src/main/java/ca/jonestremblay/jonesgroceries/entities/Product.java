package ca.jonestremblay.jonesgroceries.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Comparator;

@Entity(tableName = "products",
        indices = {@Index(value = "item_name", unique = true),})
public class Product {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int product_id;

    @NonNull
    @ColumnInfo(name = "item_name")
    public String name;

    @Embedded
    public Category category;

    @Ignore
    public Product() {
    }

    @Ignore
    public Product(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public Product(String name, int product_id, Category category) {
        this.name = name;
        this.product_id = product_id;
        this.category = category;
    }

    @Override
    public String toString() {
        return this.name;
    }


}
