package ca.jonestremblay.jonesgroceries.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.Category;
import ca.jonestremblay.jonesgroceries.entities.Product;
import ca.jonestremblay.jonesgroceries.fragments.FlyersFragment;
import ca.jonestremblay.jonesgroceries.fragments.GroceriesFragment;
import ca.jonestremblay.jonesgroceries.fragments.RecipesFragment;
import ca.jonestremblay.jonesgroceries.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public static ArrayList<Product> productsCatalog;
    AppDatabase appDatabase;

// TODO : good read https://stuff.mit.edu/afs/sipb/project/android/docs/training/basics/fragments/fragment-ui.html
// for dialogs : dialogsFragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appDatabase = AppDatabase.getInstance(getApplication().getApplicationContext());
        List<Product> productsInDatabase = appDatabase.ProductDAO().getAllProducts();
        int numberOfProductsInDatabase = productsInDatabase.size();
        if (numberOfProductsInDatabase == 0){
            productsCatalog = initializeProductsFromResources();
            addProductsToDatabase();
            productsCatalog = (ArrayList<Product>)appDatabase.ProductDAO().getAllProducts();
        } else {
            productsCatalog = new ArrayList<>(productsInDatabase);
        }
        setWidgets();
        setListeners();
    }

    private void addProductsToDatabase(){
        for (Product pro : productsCatalog){
            appDatabase.ProductDAO().insertProduct(pro);
        }
    }

    private void setWidgets() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
    }

    private void setListeners() {
        /* BottomNavBarView */
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fl_nav_wrapper, GroceriesFragment.newInstance()).commit();
        /* Others */
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
                    Fragment fragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.menu_groceries:
                            fragment = GroceriesFragment.newInstance();
                            getSupportActionBar().setTitle(getString(R.string.menuBar_groceries));
                            break;
                        case R.id.menu_recipes:
                            fragment = RecipesFragment.newInstance();
                            getSupportActionBar().setTitle(getString(R.string.menuBar_recipes));
                            break;
                        case R.id.menu_flyers:
                            fragment = FlyersFragment.newInstance();
                            getSupportActionBar().setTitle(getString(R.string.menuBar_flyers));
                            break;
                        case R.id.menu_settings:
                            fragment = SettingsFragment.newInstance();
                            getSupportActionBar().setTitle(getString(R.string.menuBar_settings));
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(
                            R.id.fl_nav_wrapper, fragment).commit();
                    return true;
                }
    };

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    public ArrayList<Product> initializeProductsFromResources(){

        ArrayList<Product> productsList = new ArrayList<Product>();
        String[] categories = getResources().getStringArray(R.array.categories);
        Product product;
        int iconID = 0;
        /* Fix category names (replace spaces with underscores)*/
        int index = 0;
        for (String categorie : categories){
            categories[index] = categorie.replace(" ", "_").toLowerCase();
            index += 1;
        }
        /* end of fixing */

        String[] categoriesName = getResources().getStringArray(R.array.categories);
        int index2 = 0;
        for (String categorie : categories){
            Category category = new Category(categoriesName[index2], iconID);
            int arrayID = getResources().getIdentifier(categorie, "array", this.getPackageName());
            String[] itemsOfCategorie = getResources().getStringArray(arrayID);
            for (String item : itemsOfCategorie){
                product = new Product();
                product.name = item;
                product.category = category;
                productsList.add(product);
            }
            index2++;
            iconID++;
        }
        return productsList;
    }

}