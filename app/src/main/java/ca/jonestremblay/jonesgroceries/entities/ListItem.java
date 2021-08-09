package ca.jonestremblay.jonesgroceries.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Comparator;
import java.util.List;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "items_list",
        primaryKeys = {"list_id" , "product_id"},
        foreignKeys= {@ForeignKey(
                            entity=Product.class,
                            parentColumns="product_id",
                            childColumns="product_id",
                            onUpdate=CASCADE,
                            onDelete=CASCADE),
                    @ForeignKey(
                            entity=UserList.class,
                            parentColumns="list_id",
                            childColumns="list_id",
                            onUpdate=CASCADE,
                            onDelete=CASCADE)},
        indices= {@Index(value = {"product_id", "list_id"}, unique = true),})
public class ListItem {



    @ColumnInfo(name= "list_id")
    public int listID;

    @NonNull
    @Embedded
    public Product product = new Product();

    public int quantity;
    @ColumnInfo(name= "measure_unit", defaultValue = "x")
    public String measureUnit;
    
    @ColumnInfo(defaultValue = "")
    public String notes;
    public boolean completed;

    public ListItem() {
        isNotCompleted();
        product = new Product();
    }

    @Ignore
    public ListItem(Product product, int quantity, String measureUnit, String notes) {
        this.product = product;
        this.quantity = quantity;
        this.measureUnit = measureUnit;
        this.notes = notes;
        isNotCompleted();
    }



    public void isCompleted(){
        this.completed = true;
    }

    public void isNotCompleted(){
        this.completed = false;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
