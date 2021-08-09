package ca.jonestremblay.jonesgroceries.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.activities.CatalogActivity;
import ca.jonestremblay.jonesgroceries.activities.MainActivity;
import ca.jonestremblay.jonesgroceries.activities.SplashActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    Preference seeCatalog;
    Preference changeNavBarColor;
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
       // nightMode = findPreference("nightMode");
    }

    private void setListeners(){
        seeCatalog.setOnPreferenceClickListener(this);
        changeNavBarColor.setOnPreferenceClickListener(this);
      //  nightMode.setOnPreferenceClickListener(this);
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
}