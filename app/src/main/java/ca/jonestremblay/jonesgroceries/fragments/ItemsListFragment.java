package ca.jonestremblay.jonesgroceries.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.adapters.ProductsListAdapter;
import ca.jonestremblay.jonesgroceries.entities.RowItem;
import ca.jonestremblay.jonesgroceries.viewmodel.ItemsListFragmentViewModel;

public class ItemsListFragment extends Fragment implements ProductsListAdapter.HandleProductsClick {

    private String title;
    private EditText addNewProductInput;
    private ImageView saveButton;
    private RecyclerView recyclerView;
    private int listID;
    private ProductsListAdapter productsListAdapter;
    private ItemsListFragmentViewModel viewModel;
    private RowItem productToUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWidgets();
        setListeners();
        return inflater.inflate(R.layout.show_products_list, container, false);
    }

    void setWidgets(){
        Bundle bundle = this.getArguments();
        int id = -1;
        if (bundle != null) {
            id = bundle.getInt("list_id");
        }
        listID = id;
        title = getActivity().getIntent().getStringExtra("category_name");

//        getSupportActionBar().setTitle(category_name);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addNewProductInput = getView().findViewById(R.id.addNewProductInput);
        saveButton = getView().findViewById(R.id.saveButton);
    }

    void setListeners(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = addNewProductInput.getText().toString();
                if(TextUtils.isEmpty(productName)){
                    Toast.makeText(getActivity(), "Enter product name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(productToUpdate == null){
                    saveNewItem(productName);
                } else {
                    updateNewItem(productToUpdate.product.name);
                }
            }
        });
        initRecyclerView();
        initViewModel();
        viewModel.getAllProductsList(listID);
    }



    private void initRecyclerView(){
        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        productsListAdapter = new ProductsListAdapter(this.getContext(), this);
        recyclerView.setAdapter(productsListAdapter);
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(ItemsListFragmentViewModel.class);
        // viewModel.setID(productsListAdapter);
        viewModel.getListOfRowItemsObserver().observe(getViewLifecycleOwner(), new Observer<List<RowItem>>() {
            @Override
            public void onChanged(List<RowItem> items) {
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
    public void productClick(RowItem item) {
        if (item.completed){
            item.completed = false;
        } else {
            item.completed = true;
        }
        viewModel.updateItem(item);
    }

    @Override
    public void removeProduct(RowItem item) {
        viewModel.deleteItem(item);
    }

    @Override
    public void editProduct(RowItem item) {
        this.productToUpdate = item;
        addNewProductInput.setText(item.product.name);
    }

    private void updateNewItem(String newName) {
        this.productToUpdate.product.name = newName;
        viewModel.updateItem(productToUpdate);
        addNewProductInput.setText("");
        productToUpdate = null;
    }

    private void saveNewItem(String productName) {
        RowItem item = new RowItem();
        item.product.name = productName;
        //product.category.category_name = category_name;
        viewModel.insertItem(item);
        addNewProductInput.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem product) {
        switch (product.getItemId()){
            case android.R.id.home:
                getActivity().finish();
        }
        return super.onOptionsItemSelected(product);
    }
}