package ca.jonestremblay.jonesgroceries.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import org.jetbrains.annotations.NotNull;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.activities.CatalogActivity;
import ca.jonestremblay.jonesgroceries.activities.MainActivity;
import ca.jonestremblay.jonesgroceries.activities.SplashActivity;
import eltos.simpledialogfragment.SimpleDialog;
import eltos.simpledialogfragment.color.SimpleColorDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceClickListener, SimpleDialog.OnDialogResultListener {

    final String COLOR_PICKER = "COLOR_DIALOG";
    Preference seeCatalog;
    Preference changeNavBarColor;
    SimpleColorDialog simpleColorDialog;
    // SwitchPreferenceCompat nightMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleOnBackPressed();
    }

    public static Fragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        setWidgets();
        setListeners();
    }

    private void setWidgets(){
        seeCatalog = findPreference("seeCatalog");
        changeNavBarColor = findPreference("changeNavBarColor");
//        nightMode = findPreference("nightMode");
//        nightMode.setChecked(false);
    }

    private void setListeners(){
        seeCatalog.setOnPreferenceClickListener(this);
        changeNavBarColor.setOnPreferenceClickListener(this);
        // nightMode.setOnPreferenceClickListener(this);
    }

    public void handleOnBackPressed(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public boolean onPreferenceClick(Preference pref) {
        if (pref == seeCatalog) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(
                    R.id.fl_nav_wrapper, CatalogFragment.newInstance()).commit();
            return true;
        } else if (pref == changeNavBarColor) {
            SimpleColorDialog.build()
                    .title("Pick a color")
                    .colorPreset(Color.RED)
                    .allowCustom(true).show(this, COLOR_PICKER);
            return true;
        }
//        } else if (pref == nightMode){
//            if (!nightMode.isEnabled()){
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            }
//            return true;
//        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onResult(@NonNull @NotNull String dialogTag, int which, @NonNull @NotNull Bundle extras) {
        switch (dialogTag){
            case COLOR_PICKER:
                int colorPickedInDialog = extras.getInt(SimpleColorDialog.COLOR);
                int red = Color.red(colorPickedInDialog);
                int green = Color.green(colorPickedInDialog);
                int blue = Color.blue(colorPickedInDialog);
                String hexCode = String.format("#%02x%02x%02x", red, green, blue);
                ((AppCompatActivity)getActivity()).getSupportActionBar()
                        .setBackgroundDrawable(new ColorDrawable(Color.parseColor(hexCode)));
                saveActionBarColor(hexCode);
                return true;
        }
        return false;
    }

    public void saveActionBarColor(String colorHexCode){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("actionBarColor", colorHexCode);
        editor.apply();
    }
}