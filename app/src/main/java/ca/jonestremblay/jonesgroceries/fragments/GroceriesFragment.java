package ca.jonestremblay.jonesgroceries.fragments;


import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.adapters.GroceriesListAdapter;
import ca.jonestremblay.jonesgroceries.entities.Grocery;
import ca.jonestremblay.jonesgroceries.viewmodel.GroceriesFragmentViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroceriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroceriesFragment extends Fragment implements GroceriesListAdapter.HandleGroceryClick {

    private GroceriesFragmentViewModel viewModel;
    private TextView noResultLabel;
    private RecyclerView recyclerView; /** list of groceries */
    private GroceriesListAdapter groceryAdapter;
    private Grocery groceryToEdit;

    public static Fragment newInstance() {
        GroceriesFragment fragment = new GroceriesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_groceries, container, false);
        // Inflate the layout for this fragment
        noResultLabel = rootView.findViewById(R.id.noGroceryTxtView);

        ImageView btnAddNew = rootView.findViewById(R.id.add_new_category_imageView);
        initViewModel();
        initRecyclerView(rootView);
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Apparition du AlertDialog pour ajouter une new grocery lists */
                showAddGroceryDialog(false);
            }
        });
        viewModel.getAllGroceriesList();

        return rootView;
    }

    private void initRecyclerView(View rootView){
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        groceryAdapter = new GroceriesListAdapter(this.getContext(), this);
        recyclerView.setAdapter(groceryAdapter);
        groceryAdapter.notifyDataSetChanged();
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(GroceriesFragmentViewModel.class);
        viewModel.getListOfGroceryObserver().observe(this.getActivity(), new Observer<List<Grocery>>() {
            @Override
            public void onChanged(List<Grocery> groceries) {
                if (groceries == null){
                    noResultLabel.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    // show the recyclerview
                    groceryAdapter.setGroceriesList(groceries);
                    noResultLabel.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showAddGroceryDialog(boolean isForEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this.getContext()).create();
        View dialogView = getLayoutInflater().inflate(R.layout.add_category_dialog_layout, null);
        /** Instanciation des objets UI */
        EditText enterCategoryInput = dialogView.findViewById(R.id.enterCategoryInput);
        TextView createButton = dialogView.findViewById(R.id.createButton);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);
        if (isForEdit){
            createButton.setText("Update");
            enterCategoryInput.setText(groceryToEdit.groceryName);
        }

        /** Listeners setters */
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enterCategoryInput.getText().toString();
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(getActivity(), "Enter category name", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (isForEdit){
                    groceryToEdit.groceryName = name;
                    viewModel.updateGrocery(groceryToEdit);
                } else {
                    viewModel.insertGrocery(name);
                }
                /** here we need to call view model */
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    @Override
    public void itemClick(Grocery grocery) {
        Fragment showProducts = new ItemsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("list_id", grocery.listId);
        showProducts.setArguments(bundle);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.groceriesFragment, showProducts ); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

    @Override
    public void removeItem(Grocery grocery) {
        viewModel.deleteGrocery(grocery);
    }

    @Override
    public void editItem(Grocery grocery) {
        this.groceryToEdit = grocery;
        showAddGroceryDialog(true);
    }

}