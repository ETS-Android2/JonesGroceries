package ca.jonestremblay.jonesgroceries.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.models.CatalogSingleton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogFragment extends Fragment {



    public static CatalogFragment newInstance(){
        CatalogFragment fragment = new CatalogFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        CatalogSingleton catalog = CatalogSingleton.getInstance(this.getContext());
//
//        catalog.printCatalog();


        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }
}