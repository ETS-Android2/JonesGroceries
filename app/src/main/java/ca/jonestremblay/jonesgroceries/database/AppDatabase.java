package ca.jonestremblay.jonesgroceries.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//import ca.jonestremblay.jonesgroceries.dao.CategoriesDAO;
import ca.jonestremblay.jonesgroceries.dao.UserListDAO;
import ca.jonestremblay.jonesgroceries.dao.ItemListDAO;
import ca.jonestremblay.jonesgroceries.dao.ProductDAO;
import ca.jonestremblay.jonesgroceries.entities.Category;
import ca.jonestremblay.jonesgroceries.entities.UserList;
import ca.jonestremblay.jonesgroceries.entities.Product;
import ca.jonestremblay.jonesgroceries.entities.ListItem;


@Database( entities = {Product.class,  ListItem.class, UserList.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    //public abstract CategoriesDAO CategoriesListDao();
    public abstract UserListDAO UserListDAO();
    public abstract ProductDAO ProductDAO();
    public abstract ItemListDAO ItemListDAO();

    private static volatile AppDatabase UNIQUE_INSTANCE;

    public static AppDatabase getInstance(Context context){
        if (UNIQUE_INSTANCE == null){
            synchronized (AppDatabase.class){
                if (UNIQUE_INSTANCE == null){
                    UNIQUE_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "groceries_catalog").allowMainThreadQueries()
                            .fallbackToDestructiveMigration().build();
                    /** .fallbackToDestructiveMigration() does a DB migration without
                     *  saving DB's data. So in every migration, data will be lost. */
                }
            }
        }
        return UNIQUE_INSTANCE;
    }


}
