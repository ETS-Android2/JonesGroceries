package ca.jonestremblay.jonesgroceries.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.UserList;
import ca.jonestremblay.jonesgroceries.viewmodel.ItemsListFragmentViewModel;
import ca.jonestremblay.jonesgroceries.viewmodel.UserListViewModel;

public class AddRecipeToGroceryDialog extends AlertDialog {

    AppDatabase appDatabase;
    View view;
    ItemsListFragmentViewModel viewModel;
    Button cancelButton;
    ChipGroup chipGroup;
    TextView listNameLabel;
    String listName;
    ArrayList<String> recipes;
    ArrayList<Boolean> checkedItems;

    public AddRecipeToGroceryDialog(Context context, ItemsListFragmentViewModel viewModel, String listName) {
        super(context);
        appDatabase = AppDatabase.getInstance(getContext().getApplicationContext());
        recipes = new ArrayList<String>();
        checkedItems = new ArrayList<Boolean>();
        this.viewModel = viewModel;
        setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dialog_add_recipe_to_grocery, null);
        this.listName = listName;
        setWidgets();
        setListeners();
        setView(view);
        show();
        /* in for loop */
    }

    private void setWidgets() {
        /** Instanciation des objets UI */
        listNameLabel = view.findViewById(R.id.listName);
        listNameLabel.setText(listName);
        cancelButton = view.findViewById(R.id.cancelButton);
        chipGroup = view.findViewById(R.id.chipGroup);
        chipGroup.removeAllViews();
        List<UserList> recipes = appDatabase.UserListDAO().getAllRecipes();
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
        Chip chip = new Chip(getContext());
        chip.setText(recipeName);
        // chip.setChipBackgroundColorResource(android.R.color.colorAccent);
        //chip.setCloseIconVisible(true);
        // chip.setTextColor(getResources().getColor(R.color.white));
        // chip.setTextAppearance(R.style.ChipTextAppearance);
        return chip;
    }

    public Chip createLabelChip(String message){
        Chip chip = new Chip(getContext());
        chip.setText(message);
        chip.setEnabled(false);
       // chip.setChipBackgroundColorResource(android.R.color.colorAccent);
        //chip.setCloseIconVisible(true);
        // chip.setTextColor(getResources().getColor(R.color.white));
        // chip.setTextAppearance(R.style.ChipTextAppearance);
        return chip;
    }
}
