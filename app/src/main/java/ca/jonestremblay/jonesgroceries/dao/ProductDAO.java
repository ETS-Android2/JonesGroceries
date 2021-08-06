package ca.jonestremblay.jonesgroceries.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import ca.jonestremblay.jonesgroceries.entities.Product;

@Dao
public interface ProductDAO {
    @Query("SELECT * FROM products")
    List<Product> getAllProducts();

    @Insert
    long insertProduct (Product item);

    @Update
    void updateProduct(Product item);

    @Delete
    void deleteProduct(Product item);
}
