package ca.jonestremblay.jonesgroceries.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ca.jonestremblay.jonesgroceries.entities.UserList;

@Dao
public interface UserListDAO {

    /** This DAO is for the the groceries and recipes lists. */

    @Query("SELECT * FROM user_lists WHERE type='recipe'")
    List<UserList> getAllRecipes();

    @Query("SELECT * FROM user_lists WHERE type='recipe' AND list_name=:recipeName")
    UserList getRecipe(String recipeName);

    @Query("SELECT * FROM user_lists WHERE type='grocery' ")
    List<UserList> getAllGroceries();

    @Insert
    long insertUserList(UserList userList);

    @Update
    int updateUserList(UserList userList);

    @Delete
    void deleteUserList(UserList userList);

}
