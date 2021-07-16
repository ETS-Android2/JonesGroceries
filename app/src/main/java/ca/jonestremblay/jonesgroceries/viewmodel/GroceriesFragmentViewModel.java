package ca.jonestremblay.jonesgroceries.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.Category;
import ca.jonestremblay.jonesgroceries.entities.Grocery;


public class GroceriesFragmentViewModel extends AndroidViewModel {
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
    public void getAllGroceriesList(){
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

    public void insertGrocery(String name){
        Grocery grocery = new Grocery();
        grocery.groceryName = name;
        /** TODO : set list id of the new grocery */
        appDatabase.GroceriesListDAO().insertGrocery(grocery);
        getAllGroceriesList();
    }

    public void updateGrocery(Grocery grocery){
        appDatabase.GroceriesListDAO().updateGrocery(grocery);
        getAllGroceriesList();
    }

    public void deleteGrocery(Grocery grocery){
        appDatabase.GroceriesListDAO().deleteGrocery(grocery);
        getAllGroceriesList();
    }
}
