package ca.jonestremblay.jonesgroceries.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.adapters.ProductsListAdapter;
import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.ListItem;

public class EditNotesDialog extends AlertDialog {

    final int MAX_CHAR_NOTES = 40;
    View view;
    Context context;
    ProductsListAdapter productsListAdapter;
    List<ListItem> productsList;
    ListItem itemClicked;
    EditText notesText;
    Button editButton;
    Button cancelButton;
    String currentNotesTemp;

    public EditNotesDialog(Context context, ProductsListAdapter adapter,
                           List<ListItem> productsList, ListItem itemClicked) {
        super(context);
        this.context = context;
        this.productsListAdapter = adapter;
        this.productsList = productsList;
        this.itemClicked = itemClicked;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dialog_edit_notes, null);
        setWidgets();
        setListeners();
        setView(view);
        show();
        // getWindow().setLayout(1100, 585);
    }

    private void setWidgets() {
        /** Instanciation des objets UI */
        notesText = view.findViewById(R.id.notesInput);
        notesText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(MAX_CHAR_NOTES) });
        editButton = view.findViewById(R.id.editButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        currentNotesTemp = itemClicked.notes;
        if (currentNotesTemp != null){
            notesText.setText(currentNotesTemp);
        }
    }

    private void setListeners() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNotes = notesText.getText().toString();
                if (!newNotes.equals(currentNotesTemp)){
                    itemClicked.notes = notesText.getText().toString();
                    AppDatabase.getInstance(getContext().getApplicationContext())
                            .ItemListDAO().updateItem(itemClicked);
                    productsListAdapter.notifyDataSetChanged();
                }
                dismiss();
            }
        });
    }
}
