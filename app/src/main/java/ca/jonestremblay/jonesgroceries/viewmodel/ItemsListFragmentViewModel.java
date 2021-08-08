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


public class ItemsListFragmentViewModel extends AndroidViewModel {
     private static final String TAG = "ShowProductsViewModel";
     private MutableLiveData<List<ListItem>> listOfItems;
     AppDatabase appDatabase;
     private int ID;


     public void setID(int ID){
     this.ID = ID;
     }

     /** To function correctly, here we need the list_id . */
    public ItemsListFragmentViewModel(Application application) {
        super(application);
        /** Instancier un objet nous donnant accès au singleton de base de données  */
        appDatabase = AppDatabase.getInstance(getApplication().getApplicationContext());
        /** Instancier la liste qui contiendra les items */
        listOfItems = new MutableLiveData<>();
        listOfItems.postValue(appDatabase.ItemListDAO().getAllItems(ID));
    }

    public MutableLiveData<List<ListItem>> getListOfRowItemsObserver(){
        return listOfItems;
    }

    /** Met à jour la liste d'items (mutableLiveData) selon le resultat retourné par la BD. */
    public void getAllProductsList(){
        List<ListItem> itemsList = appDatabase.ItemListDAO().getAllItems(ID);
        if (itemsList.size() > 0){
            listOfItems.postValue(itemsList);
        } else {
            listOfItems.postValue(null);
        }

    }


    public void insertItem(ListItem item){
        try {
            List<Product> catalog = MainActivity.productsCatalog;
            Product pro = catalog.get(catalog.size() -1);
            appDatabase.ItemListDAO().insertItem(item);
        } catch(SQLiteConstraintException ex){
            if (ex.getMessage().contains("FOREIGN KEY")){
                /** Need to add the product in the product table before, then we can in items_list*/
                Log.v(TAG, ex.getMessage() + "\n" + ex.getStackTrace());
            }
        }
        getAllProductsList();
    }

    public void updateItem(ListItem item){
        appDatabase.ItemListDAO().updateItem(item);
        getAllProductsList();
    }

    public void deleteItem(ListItem item){
        appDatabase.ItemListDAO().deleteItem(item);
        getAllProductsList();
    }


}
