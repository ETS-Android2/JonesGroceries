package ca.jonestremblay.jonesgroceries.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ca.jonestremblay.jonesgroceries.entities.Product;
import ca.jonestremblay.jonesgroceries.entities.RowItem;

@Dao
public interface ItemListDAO {

    @Query("SELECT * FROM products_lists WHERE _id = :list_id")
    List<RowItem> getAllItems(int list_id);

    @Insert
    void insertItem(RowItem... rowItems);

    @Update
    void updateItem(RowItem rowItemLists);

    @Delete
    void deleteItem(RowItem rowItemLists);

}
