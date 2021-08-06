package ca.jonestremblay.jonesgroceries.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.entities.Category;
import ca.jonestremblay.jonesgroceries.entities.Product;

/***
 * Adapter for the search bar when adding products in a list.
 */
public class SearchProductAdapter extends ArrayAdapter<Product> {
    private String TAG = "SearchProductAdapter";
    private Resources resources;
    List<Product> productsCatalog;
    List<Product> suggestions = new ArrayList<>();
    boolean gotNoResult = false;

    @NonNull
    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public SearchProductAdapter(Context context, ArrayList<Product> productsCatalog) {
        super(context, 0, new ArrayList<>());
        this.productsCatalog = productsCatalog;
        this.resources = getContext().getResources();
    }

    public List<Product> getSuggestions() {
        return suggestions;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            gotNoResult = false;
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0){
                suggestions.addAll(productsCatalog);
            } else {
                if (suggestions != null){
                    suggestions.clear();
                }
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Product product : productsCatalog){
                    if (product.name.toLowerCase().contains(filterPattern)){
                        suggestions.add(product);
                    }
                }
                suggestCreatingNewProductIfNoMatchFound(constraint.toString(), suggestions);
            }
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        public void suggestCreatingNewProductIfNoMatchFound(String typedText, List<Product> suggestions){
            if (suggestions.size() == 0){
                // if no results, suggest to user if wants to add his choice
                Product fakeProduct = new Product();
                int OTHER_ICON_ID = 21;
                fakeProduct.name = "\"" + typedText + "\"";
                fakeProduct.category = new Category(getContext().getResources().getString(R.string.Other), OTHER_ICON_ID);
                /* Adding user defined product in suggestions, in case he wants to create it for DB*/
                suggestions.add(fakeProduct);
                gotNoResult = true;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List)results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Product)resultValue).name;
        }


    };

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /* If the filtering methods returns no results, we change the layout */
        if (gotNoResult)
        {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.search_empty, parent, false
            );
        }
        if (convertView == null){
            if (!gotNoResult)
            {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.search_row, parent, false
                );
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.search_empty, parent, false
                );
            }
        }
        TextView productName = convertView.findViewById(R.id.item_name);
        Product product = getItem(position);
        productName.setText(product.name);

        return convertView;
    }



}
