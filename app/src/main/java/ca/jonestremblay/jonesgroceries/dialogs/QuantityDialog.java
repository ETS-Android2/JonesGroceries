package ca.jonestremblay.jonesgroceries.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.adapters.ProductsListAdapter;
import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.ListItem;
import ca.jonestremblay.jonesgroceries.entities.enums.MeasureUnits;

public class QuantityDialog extends AlertDialog {

    View view;
    Context context;
    EditText quantity;
    Spinner measureUnit;
    Button editButton;
    Button cancelButton;
    ListItem itemClicked;
    List<ListItem> productsList;
    ProductsListAdapter productsListAdapter;
    ArrayList<String> measureUnitsArray = new ArrayList<String>(
        Arrays.asList("x", "gr", "kg",
                "mL", "L", "oz")
    );

    public QuantityDialog(Context context, ProductsListAdapter adapter, List<ListItem> productsList,
                          ListItem itemClicked) {
        super(context);
        this.context = context;
        this.productsListAdapter = adapter;
        this.productsList = productsList;
        this.itemClicked = itemClicked;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.edit_quantity, null);
        setWidgets();
        setListeners();
        setView(view);
        show();
        // getWindow().setLayout(1100, 585);

    }

    private void setWidgets() {
        /** Instanciation des objets UI */
        quantity = view.findViewById(R.id.quantity);
        measureUnit = view.findViewById(R.id.measureUnit);
        editButton = view.findViewById(R.id.editButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        quantity.setText(String.valueOf(itemClicked.quantity));
        quantity.setTransformationMethod(null);
        int measUnitPos =  measureUnitsArray.indexOf(itemClicked.measureUnit);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        context, R.array.measure_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measureUnit.setAdapter(adapter);
        measureUnit.setSelection(measUnitPos);
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
                itemClicked.quantity = Integer.parseInt(quantity.getText().toString());
                itemClicked.measureUnit = getMeasureUnitByPositionInDialog(
                                                            measureUnit.getSelectedItemPosition());
                AppDatabase.getInstance(getContext().getApplicationContext())
                        .ItemListDAO().updateItem(itemClicked);
                productsListAdapter.notifyDataSetChanged();
                dismiss();
            }
        });
    }

public String getMeasureUnitByPositionInDialog(int position){
    String measureUnit = "";
    switch (position){
        case 0 :
            measureUnit = MeasureUnits.x.name();
            break;
        case 1 :
            measureUnit = MeasureUnits.gr.name();
            break;
        case 2 :
            measureUnit = MeasureUnits.kg.name();
            break;
        case 3 :
            measureUnit = MeasureUnits.mL.name();
            break;
        case 4 :
            measureUnit = MeasureUnits.L.name();
            break;
        case 5 :
            measureUnit = MeasureUnits.oz.name();
            break;
    }
    return measureUnit;
}

    public int getMeasureUnitPositionByName(MeasureUnits measureUnits){
        int position = -1;
        switch (measureUnits){
            case x :
                position = 0;
                break;
            case gr :
                position = 1;
                break;
            case kg :
                position = 2;
                break;
            case mL :
                position = 3;
                break;
            case L :
                position = 4;
                break;
            case oz :
                position = 5;
                break;
        }
        return position;
    }

}