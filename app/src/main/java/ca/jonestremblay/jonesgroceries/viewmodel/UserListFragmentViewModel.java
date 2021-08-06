package ca.jonestremblay.jonesgroceries.viewmodel;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.dao.UserListDAO;
import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.UserList;


public class UserListFragmentViewModel extends AndroidViewModel  implements UserListDAO {
    private static final String TAG = "GroceriesViewModel";
    private MutableLiveData<List<UserList>> listOfGroceries;
    AppDatabase appDatabase;
    
    public UserListFragmentViewModel(Application application) {
        super(application);
        /** Instancier un objet nous donnant accès au singleton de base de données  */
        appDatabase = AppDatabase.getInstance(getApplication().getApplicationContext());
        /** Instancier la liste qui contiendra les catégories */
        listOfGroceries = new MutableLiveData<>();
        // listOfCategory.postValue(appDatabase.shoppingListDao().getAllCategories());

    }

    public MutableLiveData<List<UserList>> getListOfGroceryObserver(){
        return listOfGroceries;
    }

    /** Met à jour la liste de categories (mutableLiveData) selon le resultat retourné par la BD. */
    public void refreshGroceriesList(){
        List<UserList> userListList = appDatabase.GroceriesListDAO().getAllUsersList();
        if (userListList.size() > 0){
            listOfGroceries.postValue(userListList);
        } else {
            listOfGroceries.postValue(null);
        }
    }

    @Override
    public List<UserList> getAllUsersList() {
        return null;
    }


    @Override
    public long insertUserList(UserList userList){
        int isInserted = 0;
        try {
            appDatabase.GroceriesListDAO().insertUserList(userList);
            isInserted = 1;
        } catch (SQLiteConstraintException ex) {
            Toast errMsg = Toast.makeText(getApplication().getApplicationContext(),
                    R.string.listNameTaken, Toast.LENGTH_LONG);
            errMsg.show();
        }
        refreshGroceriesList();
        return  isInserted;
    }


    public int updateUserList(UserList userList){
        int isUpdated = 0;
        try {
                appDatabase.GroceriesListDAO().updateUserList(userList);
            isUpdated = 1;
        } catch (SQLiteConstraintException ex) {
            Toast errMsg = Toast.makeText(getApplication().getApplicationContext(),
                    R.string.listNameTaken, Toast.LENGTH_LONG);
            errMsg.show();
            isUpdated = 0;
        }
        refreshGroceriesList();
        return isUpdated;
    }

    public void deleteUserList(UserList userList){
        appDatabase.GroceriesListDAO().deleteUserList(userList);
        refreshGroceriesList();
    }
}
