package ca.jonestremblay.jonesgroceries.ui.bottomNav.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.jonestremblay.jonesgroceries.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlyersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlyersFragment extends Fragment {

    public static Fragment newInstance() {
        FlyersFragment fragment = new FlyersFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flyers, container, false);
    }
}