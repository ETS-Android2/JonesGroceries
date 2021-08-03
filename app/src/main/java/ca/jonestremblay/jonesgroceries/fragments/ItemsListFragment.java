package ca.jonestremblay.jonesgroceries.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.jonestremblay.jonesgroceries.IOnBackPressed;
import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.adapters.ProductsListAdapter;
import ca.jonestremblay.jonesgroceries.entities.Category;
import ca.jonestremblay.jonesgroceries.entities.ListItem;
import ca.jonestremblay.jonesgroceries.entities.Product;
import ca.jonestremblay.jonesgroceries.viewmodel.ItemsListFragmentViewModel;

public class ItemsListFragment extends Fragment implements ProductsListAdapter.HandleProductsClick, IOnBackPressed {

    private String navbarTitle;
    private RecyclerView recyclerView;
    private int listID;
    private ProductsListAdapter productsListAdapter;
    private ItemsListFragmentViewModel viewModel;
    private ListItem productToUpdate;
    private AutoCompleteTextView searchBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleOnBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_of_items, container, false);
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
                        R.id.fl_nav_wrapper, new GroceriesFragment()).commit();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Groceries");
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    void setWidgets(View rootView){
        Bundle bundle = this.getArguments();
        int id = -1;
        if (bundle != null) {
            id = bundle.getInt("list_id");
            navbarTitle = bundle.getString("grocery_name");
           // navbarTitle = getResources().getString(R.string.bab)
        }
        listID = id;
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(navbarTitle);



        List<Product> productsList = new ArrayList<Product>();
        String[] categories = getResources().getStringArray(R.array.categories);
        Product product;
        int iconID = 0;
        for (String categorie : categories){
            Category category = new Category(categorie, iconID);
            int arrayID = getResources().getIdentifier(categorie, "array", getActivity().getPackageName());
            String[] itemsOfCategorie = getResources().getStringArray(arrayID);
            for (String item : itemsOfCategorie){
                product = new Product();
                product.name = item;
                product.category = category;
                productsList.add(product);
            }
            iconID++;
        }


        searchBar = rootView.findViewById(R.id.searchBar);
        // searchBar.setAdapter();
    }

    void setListeners(View rootView){
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String productName = addNewProductInput.getText().toString();
//                if(TextUtils.isEmpty(productName)){
//                    Toast.makeText(getActivity(), "Enter product name", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(productToUpdate == null){
//                    saveNewItem(productName);
//                } else {
//                    updateNewItem(productToUpdate.product.name);
//                }
//            }
//        });
        initRecyclerView(rootView);
        initViewModel();
        viewModel.getAllProductsList(listID);
    }



    private void initRecyclerView(View rootView){
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        productsListAdapter = new ProductsListAdapter(this.getContext(), this);
        recyclerView.setAdapter(productsListAdapter);
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(ItemsListFragmentViewModel.class);
        // viewModel.setID(productsListAdapter);
        viewModel.getListOfRowItemsObserver().observe(getViewLifecycleOwner(), new Observer<List<ListItem>>() {
            @Override
            public void onChanged(List<ListItem> items) {
                if (items == null){
                    recyclerView.setVisibility(View.GONE);
                    getView().findViewById(R.id.noProductsTxtView).setVisibility(View.VISIBLE);
                } else {
                    getView().findViewById(R.id.noProductsTxtView).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    productsListAdapter.setItemsList(items);
                }
            }
        });
    }

    @Override
    public void productClick(ListItem item) {
        if (item.completed){
            item.completed = false;
        } else {
            item.completed = true;
        }
        viewModel.updateItem(item);
    }

    @Override
    public void removeProduct(ListItem item) {
        viewModel.deleteItem(item);
    }

    @Override
    public void editProduct(ListItem item) {
        this.productToUpdate = item;
//        addNewProductInput.setText(item.product.name);
    }

    private void updateNewItem(String newName) {
        this.productToUpdate.product.name = newName;
        viewModel.updateItem(productToUpdate);
//        addNewProductInput.setText("");
        productToUpdate = null;
    }

    private void saveNewItem(String productName) {
        ListItem item = new ListItem();
        item.product.name = productName;
        item.listID = 2;
        item.product.product_id = -1;
//        item.product.product_id = 2;
        //product.category.category_name = category_name;
        viewModel.insertItem(item);
//        addNewProductInput.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem product) {
        switch (product.getItemId()){
            case android.R.id.home:
                getActivity().finish();
        }
        return super.onOptionsItemSelected(product);
    }

    @Override
    public void onBackPressed() {
        int count = getChildFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            getActivity().onBackPressed();
            //additional code
        } else {
            getChildFragmentManager().popBackStack();
        }

    }
}