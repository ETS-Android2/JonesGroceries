package ca.jonestremblay.jonesgroceries.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ca.jonestremblay.jonesgroceries.dao.CategoriesDAO;
import ca.jonestremblay.jonesgroceries.dao.GroceriesListDAO;
import ca.jonestremblay.jonesgroceries.dao.ItemListDAO;
import ca.jonestremblay.jonesgroceries.dao.ProductDAO;
import ca.jonestremblay.jonesgroceries.dao.RecipesListDAO;
import ca.jonestremblay.jonesgroceries.entities.Category;
import ca.jonestremblay.jonesgroceries.entities.Grocery;
import ca.jonestremblay.jonesgroceries.entities.Product;
import ca.jonestremblay.jonesgroceries.entities.Recipe;
import ca.jonestremblay.jonesgroceries.entities.RowItem;


@Database( entities = {Product.class, Category.class, RowItem.class, Grocery.class, Recipe.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    public abstract CategoriesDAO CategoriesListDao();
    public abstract GroceriesListDAO GroceriesListDAO();
    public abstract RecipesListDAO RecipesListDAOp();
    public abstract ProductDAO ProductListDAO();
    public abstract ItemListDAO ItemListDAO();

    private static volatile AppDatabase UNIQUE_INSTANCE;

    public static AppDatabase getInstance(Context context){
        if (UNIQUE_INSTANCE == null){
            synchronized (AppDatabase.class){
                if (UNIQUE_INSTANCE == null){
                    UNIQUE_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "groceries_catalog").allowMainThreadQueries().build();
                }
            }
        }
        return UNIQUE_INSTANCE;
    }


}
