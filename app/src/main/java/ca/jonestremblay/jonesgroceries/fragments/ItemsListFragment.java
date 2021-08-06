package ca.jonestremblay.jonesgroceries.fragments;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ca.jonestremblay.jonesgroceries.activities.MainActivity;
import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.adapters.SearchProductAdapter;
import ca.jonestremblay.jonesgroceries.adapters.ProductsListAdapter;
import ca.jonestremblay.jonesgroceries.database.AppDatabase;
import ca.jonestremblay.jonesgroceries.entities.Category;
import ca.jonestremblay.jonesgroceries.entities.ListItem;
import ca.jonestremblay.jonesgroceries.entities.MeasureUnits;
import ca.jonestremblay.jonesgroceries.entities.Product;
import ca.jonestremblay.jonesgroceries.viewmodel.ItemsListFragmentViewModel;

/***
 * This fragment is used to display a recycler view, containing list_item's table content of
 * a list, plus the search bar at the top for searching and adding products in the recycler view.
 */
public class ItemsListFragment extends Fragment implements ProductsListAdapter.HandleProductsClick {

    private String navbarTitle;
    private RecyclerView recyclerView;
    private int listID;
    private SearchProductAdapter searchProductAdapter;
    private ProductsListAdapter productsListAdapter;
    private ItemsListFragmentViewModel viewModel;
    private ListItem productToUpdate;
    private AutoCompleteTextView searchBar;
    private ImageButton btnClearText;
    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleOnBackPressed();
        setHasOptionsMenu(true);
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
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.menuBar_groceries));
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

            listID = id;
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(navbarTitle);
        }
        searchProductAdapter = new SearchProductAdapter(this.getContext(),
                                    MainActivity.productsCatalog);
        searchBar = rootView.findViewById(R.id.searchBar);
        searchBar.setAdapter(searchProductAdapter);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        btnClearText = rootView.findViewById(R.id.btn_clear_text);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.list_options, menu);
    }



    void setListeners(View rootView){
        initRecyclerView(rootView);
        initViewModel();
        viewModel.setID(listID);
        viewModel.getAllProductsList();

        btnClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
            }
        });

        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product productToAdd = ((Product)parent.getItemAtPosition(position));
                // int size = searchProductAdapter.getSuggestions().size();
                if (productsListAdapter.getProductsList() != null ){
                    /** Checks if item is already in the list, adjust qty if needed */
                    for (ListItem item : productsListAdapter.getProductsList()){
                        if (productToAdd.name.equals(item.product.name)){
                            Toast.makeText(getContext(), getString(R.string.itemAlreadyInList)
                                + getResources().getString(R.string.canAdjustIfLongPress),
                                                                Toast.LENGTH_LONG).show();
                        }
                    }
                }
                Product newUserProduct = getNewUserProduct(productToAdd);
                boolean isAdded = addUserProductToDatabase(newUserProduct);
                if (isAdded){
                    Toast.makeText(getContext(), newUserProduct.name +
                            getResources().getString(R.string.hasBeenAdded), Toast.LENGTH_SHORT).show();
                }
                searchBar.setText(""); /** Resets user input */
                addProductToList(productToAdd);
            }
        });
    }

    public boolean addUserProductToDatabase(Product newUserProduct){
        if (newUserProduct != null){
            /* Then user choice wasn't already in database : we need to add it.*/
            try {
                long id = AppDatabase.getInstance(getActivity().getApplication().
                        getApplicationContext()).ProductDAO().insertProduct(newUserProduct);
                ListItem item = new ListItem();
                item.product = newUserProduct;
                item.product.product_id = (int)id;
                item.quantity = 1;
                /** Adding the new product in the productCatalog arrayList, since that ArrayList
                 * is initialized on startup of the app only. s*/
                MainActivity.productsCatalog.add(newUserProduct);
                return true;
            } catch(SQLiteConstraintException ex){
                Toast.makeText(getContext(), getString(R.string.errorAddindInDB),Toast.LENGTH_SHORT).show();}
        }
        return false;
    }

    /***
     * Return user new product if that's a new product, otherwise returns null.
     * If null, we know we can use original obtained product to add to the list.
     * @param product obtained for user click in search bar's dropdown
     * @return
     */
    private Product getNewUserProduct(Product product){
        int OTHER_ICON_ID = 21;
        /* Set the right name (remove sentence, keep only product name) */
        if (product.category.icon_id == OTHER_ICON_ID && product.name.contains("\"")){
            String newItemName = product.name.replaceAll("\"", "");
            product.name = newItemName;
            return product;
        }
        return null;
    }


    private void initRecyclerView(View rootView){
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        productsListAdapter = new ProductsListAdapter(this.getContext(), this);
        recyclerView.setAdapter(productsListAdapter);
        productsListAdapter.notifyDataSetChanged();
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(ItemsListFragmentViewModel.class);
        viewModel.getListOfRowItemsObserver().observe(getViewLifecycleOwner(), new Observer<List<ListItem>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
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
    }

    private void updateNewItem(String newName) {
        this.productToUpdate.product.name = newName;
        viewModel.updateItem(productToUpdate);
        productToUpdate = null;
    }

    private void addProductToList(Product product) {
        int DEFAULT_QUANTITY = 1;
        ListItem item = new ListItem();
        item.listID = listID;
        Category category = new Category(product.category.category_name, product.category.icon_id);
        item.product = new Product(product.name, product.product_id, category);
        item.quantity = DEFAULT_QUANTITY;
        item.measureUnit = MeasureUnits.x.toString();
        /* Ajout du produit dans la liste */
        viewModel.insertItem(item);
        productsListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem product) {
        switch (product.getItemId()){
            case android.R.id.home:
                getActivity().finish();
            case R.id.action_delete_bought_products:
                List<ListItem> itemsToDelete = new ArrayList<>();
                for (ListItem it : productsListAdapter.getProductsList()){
                    if (it.completed){
                        itemsToDelete.add(it);
                    }
                }
                for(ListItem item : itemsToDelete){
                    AppDatabase.getInstance(getContext().getApplicationContext())
                            .ItemListDAO().deleteItem(item);
                }
                productsListAdapter.getProductsList().removeAll(itemsToDelete);
                productsListAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(product);
    }
}