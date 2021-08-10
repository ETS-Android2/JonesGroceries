package ca.jonestremblay.jonesgroceries.viewmodel;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.dao.UserListDAO;
import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.UserList;
import ca.jonestremblay.jonesgroceries.entities.enums.ListType;


public class UserListViewModel extends AndroidViewModel  implements UserListDAO {
    private static final String TAG = "GroceriesViewModel";
    private MutableLiveData<List<UserList>> listOfUserList; // recipe or grocery
    private String listType;
    AppDatabase appDatabase;

    public UserListViewModel(Application application) {
        super(application);
        /** Instancier un objet nous donnant accès au singleton de base de données  */
        appDatabase = AppDatabase.getInstance(getApplication().getApplicationContext());
        /** Instancier la liste qui contiendra les catégories */
        listOfUserList = new MutableLiveData<>();
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public MutableLiveData<List<UserList>> getListOfUserListObserver(){
        return listOfUserList;
    }

    /** Met à jour la liste de categories (mutableLiveData) selon le resultat retourné par la BD. */
    public void refreshUserList(){
        List<UserList> userLists = null;
        if (listType.equals(ListType.grocery.toString())){
            /** Loading groceries */
            userLists= appDatabase.UserListDAO().getAllGroceries();
        } else if (listType.equals((ListType.recipe.toString()))){
            /** Loading recipes */
            userLists= appDatabase.UserListDAO().getAllRecipes();
        }
        if (userLists != null){
            if (userLists.size() > 0){
                listOfUserList.postValue(userLists);
            } else {
                listOfUserList.postValue(null);
            }
        }
    }


    @Override
    public List<UserList> getAllRecipes() {
        return appDatabase.UserListDAO().getAllRecipes();
    }

    @Override
    public UserList getRecipe(String recipeName) {
        return appDatabase.UserListDAO().getRecipe(recipeName);
    }

    @Override
    public List<UserList> getAllGroceries() {
        return appDatabase.UserListDAO().getAllGroceries();
    }

    @Override
    public long insertUserList(UserList userList){
        int isInserted = 0;
        try {
            appDatabase.UserListDAO().insertUserList(userList);
            isInserted = 1;
        } catch (SQLiteConstraintException ex) {
            Toast errMsg = Toast.makeText(getApplication().getApplicationContext(),
                    R.string.listNameTaken, Toast.LENGTH_LONG);
            errMsg.show();
        }
        refreshUserList();
        return  isInserted;
    }


    public int updateUserList(UserList userList){
        int isUpdated = 0;
        try {
                appDatabase.UserListDAO().updateUserList(userList);
            isUpdated = 1;
        } catch (SQLiteConstraintException ex) {
            Toast errMsg = Toast.makeText(getApplication().getApplicationContext(),
                    R.string.listNameTaken, Toast.LENGTH_LONG);
            errMsg.show();
            isUpdated = 0;
        }
        refreshUserList();
        return isUpdated;
    }

    public void deleteUserList(UserList userList){
        appDatabase.UserListDAO().deleteUserList(userList);
        refreshUserList();
    }
}
