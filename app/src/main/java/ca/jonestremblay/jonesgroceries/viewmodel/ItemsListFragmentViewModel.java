package ca.jonestremblay.jonesgroceries.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.Product;
import ca.jonestremblay.jonesgroceries.entities.RowItem;


public class ItemsListFragmentViewModel extends AndroidViewModel {
    private static final String TAG = "ShowProductsViewModel";
    private MutableLiveData<List<RowItem>> listOfItems;
    AppDatabase appDatabase;
    private int ID;


//    public void setID(int ID){
//        this.ID = ID;
//    }

    /** To function correctly, here we need the list_id . */
    public ItemsListFragmentViewModel(Application application) {
        super(application);
        /** Instancier un objet nous donnant accès au singleton de base de données  */
        appDatabase = AppDatabase.getInstance(getApplication().getApplicationContext());
        /** Instancier la liste qui contiendra les catégories */
        listOfItems = new MutableLiveData<>();
        listOfItems.postValue(appDatabase.ItemListDAO().getAllItems(ID));
    }

    public MutableLiveData<List<RowItem>> getListOfRowItemsObserver(){
        return listOfItems;
    }

    /** Met à jour la liste de categories (mutableLiveData) selon le resultat retourné par la BD. */
    public void getAllProductsList(int listID){
        Log.d(TAG, "getAllCategoryList: GETTING THE LIST OUT OF THE DATABASE");
        List<RowItem> itemsList = appDatabase.ItemListDAO().getAllItems(ID);
        if (itemsList.size() > 0){
            Log.d(TAG, "getAllCategoryList: LISTE COUNT OBTENUE : " + itemsList.size());
            listOfItems.postValue(itemsList);
        } else {
            Log.d(TAG, "getAllCategoryList: LISTE EST NULL");
            listOfItems.postValue(null);
        }
    }


    public void insertItem(RowItem item){
        appDatabase.ItemListDAO().insertItem(item);
        getAllProductsList(item.listID);
    }

    public void updateItem(RowItem item){
        appDatabase.ItemListDAO().updateItem(item);
        getAllProductsList(item.listID);
    }

    public void deleteItem(RowItem item){
        appDatabase.ItemListDAO().deleteItem(item);
        getAllProductsList(item.listID);
    }
}
