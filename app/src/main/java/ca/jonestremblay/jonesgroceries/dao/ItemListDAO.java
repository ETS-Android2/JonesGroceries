package ca.jonestremblay.jonesgroceries.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ca.jonestremblay.jonesgroceries.entities.ListItem;

@Dao
public interface ItemListDAO {

    /** This DAO is for the specific list (groceries or recipes, no important) */

    @Query("SELECT seq as lastGeneratedId FROM sqlite_sequence WHERE name = 'items_list'")
    int getLastUniqueId();
    
    @Query("SELECT * FROM items_list")
    List<ListItem> getCompleteCatalog();

    @Query("SELECT * FROM items_list WHERE list_id = :list_id")
    List<ListItem> getAllItems(int list_id);

    @Insert
    void insertItem(ListItem listItem);

    @Update
    void updateItem(ListItem listItemLists);

    @Delete
    void deleteItem(ListItem listItemLists);

}
