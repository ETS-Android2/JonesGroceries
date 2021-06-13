package ca.jonestremblay.jonesgroceries.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.ui.bottomNav.fragments.FlyersFragment;
import ca.jonestremblay.jonesgroceries.ui.bottomNav.fragments.GroceriesFragment;
import ca.jonestremblay.jonesgroceries.ui.bottomNav.fragments.RecipesFragment;
import ca.jonestremblay.jonesgroceries.ui.bottomNav.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidgets();
        setListeners();
    }

    private void setWidgets(){
        bottomNavigationView = findViewById(R.id.bottom_nav);
    }

    private void setListeners(){
        /* BottomNavBarView */
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fl_nav_wrapper, GroceriesFragment.newInstance()).commit();
        /* Others */
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()){
                    case R.id.menu_groceries:
                        fragment = GroceriesFragment.newInstance();
                        break;
                    case R.id.menu_recipes:
                        fragment = RecipesFragment.newInstance();
                        break;
                    case R.id.menu_flyers:
                        fragment = FlyersFragment.newInstance();
                        break;
                    case R.id.menu_settings:
                        fragment = SettingsFragment.newInstance();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.fl_nav_wrapper, fragment).commit();
                return true;

            }
    };
}