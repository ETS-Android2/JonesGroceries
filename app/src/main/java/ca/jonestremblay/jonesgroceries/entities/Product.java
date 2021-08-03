package ca.jonestremblay.jonesgroceries.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "products", indices = {@Index(value = "item_name", unique = true)})
public class Product {
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


    public Product(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "_id=" + product_id +
                ", name='" + name + '\'' +
                ", category=" + category +
                '}';
    }
}
