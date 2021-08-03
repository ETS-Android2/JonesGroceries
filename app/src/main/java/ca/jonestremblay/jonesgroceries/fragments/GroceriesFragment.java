package ca.jonestremblay.jonesgroceries.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.adapters.GroceriesListAdapter;
import ca.jonestremblay.jonesgroceries.dao.GroceriesListDAO;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleOnBackPressed();
    }

    public void handleOnBackPressed(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_groceries, container, false);
        noResultLabel = rootView.findViewById(R.id.noGroceryTxtView);

        ImageView btnAddNew = rootView.findViewById(R.id.add_new_category_imageView);
        initViewModel();
        initRecyclerView(rootView);
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Apparition du AlertDialog pour ajouter une new grocery list */
                showGroceryListDialog(false);
            }
        });
        viewModel.refreshGroceriesList();
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
                /** Show or hide noResultLabel if necessary */
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


    /***
     * Used to show the correct dialog (create or edit) for a grocery list.
     *
     * @param isForEdit <br/>If TRUE, the dialog will be about editing the clicked grocery list.<br/>>
     *                  If FALSE, the dialog will be about creating a new grocery list.
     */
    private void showGroceryListDialog(boolean isForEdit) {
        int MAX_CHAR_GROCERY_NAME = 15;
        AlertDialog dialogBuilder = new AlertDialog.Builder(this.getContext()).create();
        dialogBuilder.setCancelable(false);
        View dialogView = getLayoutInflater().inflate(R.layout.add_category_dialog_layout, null);
        /** Instanciation des objets UI */
        EditText listNameInput = dialogView.findViewById(R.id.enterCategoryInput);
        TextView createButton = dialogView.findViewById(R.id.createButton);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);

        listNameInput.setFilters(new InputFilter[] { new InputFilter.LengthFilter(MAX_CHAR_GROCERY_NAME) });
        if (isForEdit){
            createButton.setText("Update");
            listNameInput.setText(groceryToEdit.groceryName);
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
                String name = listNameInput.getText().toString();
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(getActivity(), "Enter category name", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (isForEdit){
                    groceryToEdit.groceryName = name;
                    if (viewModel.updateGrocery(groceryToEdit) == 0){
                        listNameInput.setText("");
                    }
                } else {
                     if (name.length() > MAX_CHAR_GROCERY_NAME){
                         System.out.println("Name is too long, please enter a shorter one.");
                         listNameInput.setText("");
                     } else {
                         Grocery grocery = new Grocery();
                         grocery.setIconId(0);
                         grocery.setGroceryName(name);
                         viewModel.insertGrocery(grocery);
                         dialogBuilder.dismiss();
                     }
                }
                /** here we need to call view model */

            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void showDeleteGroceryDialog(Grocery grocery) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this.getContext()).create();
        dialogBuilder.setCancelable(false);
        View dialogView = getLayoutInflater().inflate(R.layout.delete_grocery_list_dialog, null);
        /** Instanciation des objets UI */
        TextView listName = dialogView.findViewById(R.id.listName);
        Button deleteButton = dialogView.findViewById(R.id.deleteButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        listName.setText(grocery.groceryName);
        /** Listeners setters */
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteGrocery(grocery);
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
        dialogBuilder.getWindow().setLayout(1000, 585);
    }

    @Override
    public void itemClick(Grocery grocery) {
        Fragment showProducts = new ItemsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("list_id", grocery.listId);
        bundle.putString("grocery_name", grocery.groceryName);
        showProducts.setArguments(bundle);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        // FrameLayout fl = (FrameLayout)R.layout.activity_main;
        transaction.replace(R.id.groceriesFragment, showProducts ); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

    @Override
    public void removeItem(Grocery grocery) {
        showDeleteGroceryDialog(grocery);
    }

    @Override
    public void editItem(Grocery grocery) {
        this.groceryToEdit = grocery;
        showGroceryListDialog(true);
    }

}