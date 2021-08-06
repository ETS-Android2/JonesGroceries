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

    @Query("SELECT * FROM user_lists")
    List<UserList> getAllUsersList();

    @Insert
    long insertUserList(UserList userList);

    @Update
    int updateUserList(UserList userList);

    @Delete
    void deleteUserList(UserList userList);

}
