package ca.jonestremblay.jonesgroceries.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.adapters.CatalogAdapter;
import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.ListItem;
import ca.jonestremblay.jonesgroceries.entities.Product;
import ca.jonestremblay.jonesgroceries.fragments.FlyersFragment;
import ca.jonestremblay.jonesgroceries.fragments.GroceriesFragment;
import ca.jonestremblay.jonesgroceries.fragments.RecipesFragment;
import ca.jonestremblay.jonesgroceries.fragments.SettingsFragment;
import ca.jonestremblay.jonesgroceries.viewmodel.CatalogViewModel;

public class CatalogActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    public static ArrayList<ListItem> itemsCatalog;
    AppDatabase appDatabase;
    private CatalogAdapter catalogAdapter;
    private CatalogViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_fragment);
        handleOnBackPressed();
        appDatabase = AppDatabase.getInstance(getApplication().getApplicationContext());
        itemsCatalog = (ArrayList<ListItem>)appDatabase.ItemListDAO().getCompleteCatalog();
        setWidgets();
        setListeners();
    }

    private void setWidgets() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        recyclerView = findViewById(R.id.catalogRecyclerView);
    }

    private void setListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView(){
        //recyclerView = rootView.findViewById(R.id.fullCatalogInSettings);
        /* bad context ???! */
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        catalogAdapter = new CatalogAdapter(getApplicationContext());
        recyclerView.setAdapter(catalogAdapter);
        catalogAdapter.notifyDataSetChanged();
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        viewModel.getListOfRowItemsObserver().observe(this, new Observer<List<Product>>(){
            @Override
            public void onChanged(List<Product> products) {
                catalogAdapter.setProductsList(products);
            }
        });

    }

    public void handleOnBackPressed(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                /** Get us back to groceries lists home page */
               getSupportFragmentManager().beginTransaction().replace(
                        R.id.fl_nav_wrapper, new SettingsFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.menuBar_settings));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
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
}