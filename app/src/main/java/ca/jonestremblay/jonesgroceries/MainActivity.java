package ca.jonestremblay.jonesgroceries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ca.jonestremblay.jonesgroceries.entities.Product;
import ca.jonestremblay.jonesgroceries.fragments.FlyersFragment;
import ca.jonestremblay.jonesgroceries.fragments.GroceriesFragment;
import ca.jonestremblay.jonesgroceries.fragments.RecipesFragment;
import ca.jonestremblay.jonesgroceries.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
// TODO : good read https://stuff.mit.edu/afs/sipb/project/android/docs/training/basics/fragments/fragment-ui.html
// for dialogs : dialogsFragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidgets();
        setListeners();
        // Toast.makeText(this, obtainStyledAttributes(), Toast.LENGTH_SHORT).show();
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
                            getSupportActionBar().setTitle("Groceries");
                            break;
                        case R.id.menu_recipes:
                            fragment = RecipesFragment.newInstance();
                            getSupportActionBar().setTitle("Recipes");
                            break;
                        case R.id.menu_flyers:
                            fragment = FlyersFragment.newInstance();
                            getSupportActionBar().setTitle("Flyers");
                            break;
                        case R.id.menu_settings:
                            fragment = SettingsFragment.newInstance();
                            getSupportActionBar().setTitle("Settings");
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
}