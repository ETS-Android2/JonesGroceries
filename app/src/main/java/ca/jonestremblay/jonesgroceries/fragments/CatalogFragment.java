package ca.jonestremblay.jonesgroceries.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.adapters.CatalogAdapter;
import ca.jonestremblay.jonesgroceries.entities.ListItem;
import ca.jonestremblay.jonesgroceries.entities.Product;
import ca.jonestremblay.jonesgroceries.viewmodel.CatalogViewModel;

/***
 * This fragment is used to display a recycler view, containing list_item's table content of
 * a list, plus the search bar at the top for searching and adding products in the recycler view.
 */
public class CatalogFragment extends Fragment{

    private View rootView;
    private String navbarTitle;
    private RecyclerView recyclerView;
    private CatalogAdapter catalogAdapter;
    private CatalogViewModel viewModel;
    private ActionBar actionBar;

    public static Fragment newInstance() {
        CatalogFragment fragment = new CatalogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleOnBackPressed();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.catalog_fragment, container, false);
        super.onCreate(savedInstanceState);
        setWidgets(rootView);
        setListeners(rootView);
        return rootView;
    }

    public void handleOnBackPressed(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                /** Get us back to groceries lists home page */
                getActivity().getSupportFragmentManager().beginTransaction().replace(
                        R.id.fl_nav_wrapper, new SettingsFragment()).commit();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.menuBar_settings));
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    void setWidgets(View rootView){

        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        navbarTitle = getString(R.string.catalog);
        actionBar.setTitle(navbarTitle);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);


    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.list_options, menu);
    }


    void setListeners(View rootView){
        initRecyclerView(rootView);
        initViewModel();
        viewModel.getAllProducts();
    }


    private void initRecyclerView(View rootView){
        recyclerView = rootView.findViewById(R.id.catalogRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        catalogAdapter = new CatalogAdapter(getContext());
        recyclerView.setAdapter(catalogAdapter);
        catalogAdapter.notifyDataSetChanged();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.divider));
        recyclerView.addItemDecoration(itemDecoration);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            //this.getActivity().onBackPressed();
            getActivity().finish();
            return true;// close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}