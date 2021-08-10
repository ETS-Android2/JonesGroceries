package ca.jonestremblay.jonesgroceries.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.entities.Category;
import ca.jonestremblay.jonesgroceries.entities.Product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlyersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlyersFragment extends Fragment {
    private static final String IGA_URL = "https://circulars.ca/cwf/flyers.do?ref=circulars.ca&sname=iga&page=iga&region=&fenetre=";
    private static final String MAXI_URL = "https://www.circulars.ca/Maxi/circulaire/";
    private static final String METRO_URL = "https://circulars.ca/cwf/?ref=circulars.ca&sname=metro&page=/metro/current-flyer";
    private static final String ADONIS_URL = "https://flipp.com/en-ca/montreal-qc/flyer/4275283-marche-adonis-weekly";
    private static final String SUPERC_URL = "https://www.circulars.ca/SuperC/";
    private static final String FLIPP_URL = "https://flipp.com/flyers";


    View rootview;
    ImageButton IGA;
    ImageButton MAXI;
    ImageButton METRO;
    ImageButton ADONIS;
    ImageButton SUPERC;
    ImageButton FLIPP;


    public static Fragment newInstance() {
        FlyersFragment fragment = new FlyersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleOnBackPressed();

    }

    private void setWidgets() {
        IGA = rootview.findViewById(R.id.btn_iga);
        MAXI = rootview.findViewById(R.id.btn_maxi);
        METRO = rootview.findViewById(R.id.btn_metro);
        ADONIS = rootview.findViewById(R.id.btn_adonis);
        SUPERC = rootview.findViewById(R.id.btn_superc);
        FLIPP = rootview.findViewById(R.id.btn_flipp);
    }

    private void setListeners() {
        IGA.setOnClickListener(IGAClickListener);
        MAXI.setOnClickListener(MaxiClickListener);
        METRO.setOnClickListener(MetroClickListener);
        ADONIS.setOnClickListener(AdonisClickListener);
        SUPERC.setOnClickListener(SuperCClickListener);
        FLIPP.setOnClickListener(FlippClickListener);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_flyers, container, false);
        setWidgets();
        setListeners();
        return rootview;
    }




    public void handleOnBackPressed(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void goToURL(String url){
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    View.OnClickListener IGAClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToURL(IGA_URL);
        }
    };

    View.OnClickListener MetroClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToURL(METRO_URL);
        }
    };

    View.OnClickListener MaxiClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToURL(MAXI_URL);
        }
    };

    View.OnClickListener AdonisClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToURL(ADONIS_URL);
        }
    };

    View.OnClickListener SuperCClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToURL(SUPERC_URL);
        }
    };

    View.OnClickListener FlippClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToURL(FLIPP_URL);
        }
    };
}