package ca.jonestremblay.jonesgroceries.viewmodel;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ca.jonestremblay.jonesgroceries.dao.GroceriesListDAO;
import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.Grocery;


public class GroceriesFragmentViewModel extends AndroidViewModel  implements GroceriesListDAO {
    private static final String TAG = "GroceriesViewModel";
    private MutableLiveData<List<Grocery>> listOfGroceries;
    AppDatabase appDatabase;
    
    public GroceriesFragmentViewModel(Application application) {
        super(application);
        /** Instancier un objet nous donnant accès au singleton de base de données  */
        appDatabase = AppDatabase.getInstance(getApplication().getApplicationContext());
        /** Instancier la liste qui contiendra les catégories */
        listOfGroceries = new MutableLiveData<>();
        // listOfCategory.postValue(appDatabase.shoppingListDao().getAllCategories());

    }

    public MutableLiveData<List<Grocery>> getListOfGroceryObserver(){
        return listOfGroceries;
    }

    /** Met à jour la liste de categories (mutableLiveData) selon le resultat retourné par la BD. */
    public void refreshGroceriesList(){
        Log.d(TAG, "getAllCategoryList: GETTING THE LIST OUT OF THE DATABASE");
        List<Grocery> groceryList = appDatabase.GroceriesListDAO().getAllGroceries();
        if (groceryList.size() > 0){
            Log.d(TAG, "getAllCategoryList: LISTE COUNT OBTENUE : " + groceryList.size());
            listOfGroceries.postValue(groceryList);
        } else {
            Log.d(TAG, "getAllCategoryList: LISTE EST NULL");
            listOfGroceries.postValue(null);
        }
    }

    @Override
    public List<Grocery> getAllGroceries() {
        return null;
    }


    @Override
    public long insertGrocery(Grocery grocery){
        // grocery.listId = 1;
        int isInserted = 0;
        /** TODO : set list id of the new grocery */
        try {
            appDatabase.GroceriesListDAO().insertGrocery(grocery);
            isInserted = 1;
        } catch (SQLiteConstraintException ex) {
            Toast errMsg = Toast.makeText(getApplication().getApplicationContext(),
                    "You already have a list with this name", Toast.LENGTH_LONG);
            errMsg.show();
        }
        refreshGroceriesList();
        return  isInserted;
    }


    public int updateGrocery(Grocery grocery){
        int isUpdated = 0;
        try {
                appDatabase.GroceriesListDAO().updateGrocery(grocery);
            isUpdated = 1;
        } catch (SQLiteConstraintException ex) {
            Toast errMsg = Toast.makeText(getApplication().getApplicationContext(),
                    "You already have a list with this name", Toast.LENGTH_LONG);
            errMsg.show();
            isUpdated = 0;
        }
        refreshGroceriesList();
        return isUpdated;
    }

    public void deleteGrocery(Grocery grocery){
        appDatabase.GroceriesListDAO().deleteGrocery(grocery);
        refreshGroceriesList();
    }
}
