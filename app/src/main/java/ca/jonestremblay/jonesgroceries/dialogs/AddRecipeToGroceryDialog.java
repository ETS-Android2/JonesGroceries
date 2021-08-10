package ca.jonestremblay.jonesgroceries.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.adapters.ProductsListAdapter;
import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.ListItem;
import ca.jonestremblay.jonesgroceries.entities.UserList;
import ca.jonestremblay.jonesgroceries.viewmodel.ItemsListFragmentViewModel;
import ca.jonestremblay.jonesgroceries.viewmodel.UserListViewModel;

public class AddRecipeToGroceryDialog extends AlertDialog {

    AppDatabase appDatabase;
    View view;
    ItemsListFragmentViewModel viewModel;
    ProductsListAdapter productsListAdapter;
    Button cancelButton;
    ChipGroup chipGroup;
    TextView listNameLabel;
    String listName;
    List<UserList> recipes;
    int listID;

    public AddRecipeToGroceryDialog(String listName, int listID, Context context,
                ItemsListFragmentViewModel viewModel, ProductsListAdapter productsListAdapter) {
        super(context);
        appDatabase = AppDatabase.getInstance(getContext().getApplicationContext());
        this.viewModel = viewModel;
        this.productsListAdapter = productsListAdapter;
        this.listID = listID;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dialog_add_recipe_to_grocery, null);
        this.listName = listName;
        setWidgets();
        setListeners();
        setView(view);
        show();
    }

    private void setWidgets() {
        /** Instanciation des objets UI */
        listNameLabel = view.findViewById(R.id.listName);
        listNameLabel.setText(listName);
        cancelButton = view.findViewById(R.id.cancelButton);
        chipGroup = view.findViewById(R.id.chipGroup);
        chipGroup.setSingleSelection(true);
        chipGroup.removeAllViews();
        recipes = appDatabase.UserListDAO().getAllRecipes();
        if (recipes.size() == 0){
            chipGroup.addView(createLabelChip("you don't have any recipe."));
        } else {
            for (UserList recipe : recipes) {
                chipGroup.addView(createRecipeChip(recipe.getListName()));
            }
        }
    }

    private void setListeners() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public Chip createRecipeChip(String recipeName){
        Chip chip = new Chip(view.getContext());
        chip.setText(recipeName);
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipeToGroceryList(recipeName);
            }
        });
        return chip;
    }

    public Chip createLabelChip(String message){
        Chip chip = new Chip(view.getContext());
        chip.setText(message);
        chip.setEnabled(false);
        return chip;
    }

    public void addRecipeToGroceryList(String recipeName){
        int recipeId = appDatabase.UserListDAO().getRecipe(recipeName).getListId();
        /* on veut get des items, qui n'ont jamais ete set ... on dois les set . mais les trouver avant .*/
        List<ListItem> recipeItems = appDatabase.ItemListDAO().getAllItems(recipeId);
        if (recipeItems != null){
            for (ListItem item : recipeItems){
                /** Need to change item list id for the one id list we're adding products too (groceries)*/
                int lastGeneratedId = appDatabase.ItemListDAO().getLastUniqueId();
                item.unique_number = lastGeneratedId + 1;
                item.listID = listID;
                viewModel.insertItem(item);
                productsListAdapter.notifyDataSetChanged();
            }
            dismiss();
        } else {
            Toast.makeText(getContext(), "Error, recipesItems that we got is null 😎", Toast.LENGTH_SHORT).show();
        }

    }
}
