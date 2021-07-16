package ca.jonestremblay.jonesgroceries.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ca.jonestremblay.jonesgroceries.entities.Category;
import ca.jonestremblay.jonesgroceries.entities.Product;

@Dao
public interface CategoriesDAO {

    @Query("SELECT * FROM categories")
    List<Category> getAllCategories();

    @Insert
    void insertCategory(Category...category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

}
