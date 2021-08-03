package ca.jonestremblay.jonesgroceries.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "items_list",
        foreignKeys=@ForeignKey(
                entity=Product.class,
                parentColumns="product_id",
                childColumns="product_id",
                onDelete=CASCADE),
        indices= {@Index(value="list_id"), @Index(value = "product_id")})
public class ListItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "list_id")
    public int listID;

//    @ColumnInfo(name= "product_id")
//    public int product_id;

    @Embedded
    public Product product;

    public int quantity;
    @ColumnInfo(name= "measure_unit")
    public String measureUnit;
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
