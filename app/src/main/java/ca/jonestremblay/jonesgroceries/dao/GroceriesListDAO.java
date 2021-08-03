package ca.jonestremblay.jonesgroceries.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ca.jonestremblay.jonesgroceries.entities.Category;
import ca.jonestremblay.jonesgroceries.entities.Grocery;

@Dao
public interface GroceriesListDAO {

    @Query("SELECT * FROM groceries")
    List<Grocery> getAllGroceries();

    @Insert
    long insertGrocery(Grocery grocery);

    @Update
    int updateGrocery(Grocery grocery);

    @Delete
    void deleteGrocery(Grocery grocery);

}
