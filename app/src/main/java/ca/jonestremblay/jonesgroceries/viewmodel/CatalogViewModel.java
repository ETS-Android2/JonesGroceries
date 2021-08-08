package ca.jonestremblay.jonesgroceries.viewmodel;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ca.jonestremblay.jonesgroceries.activities.MainActivity;
import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.ListItem;
import ca.jonestremblay.jonesgroceries.entities.Product;


public class CatalogViewModel extends AndroidViewModel {
     private static final String TAG = "CatalogViewModel";
     private MutableLiveData<List<Product>> listOfProducts;
     AppDatabase appDatabase;

     /** To function correctly, here we need the list_id . */
    public CatalogViewModel(Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(getApplication().getApplicationContext());
        /* Instancier la liste qui contiendra les items */
        listOfProducts = new MutableLiveData<>();
        listOfProducts.postValue(MainActivity.productsCatalog);
    }

    public MutableLiveData<List<Product>> getListOfRowItemsObserver(){
        return listOfProducts;
    }

    /** Met à jour la liste d'items (mutableLiveData) selon le resultat retourné par la BD. */
    public void getAllProducts(){
        List<Product> itemsList = MainActivity.productsCatalog;
        if (itemsList.size() > 0){
            listOfProducts.postValue(itemsList);
        } else {
            listOfProducts.postValue(null);
        }

    }

}
