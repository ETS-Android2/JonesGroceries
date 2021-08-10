package ca.jonestremblay.jonesgroceries.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
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
import ca.jonestremblay.jonesgroceries.adapters.UserListsAdapter;
import ca.jonestremblay.jonesgroceries.dialogs.CreateOrEditUserListDialog;
import ca.jonestremblay.jonesgroceries.dialogs.DeleteUserListDialog;
import ca.jonestremblay.jonesgroceries.entities.UserList;
import ca.jonestremblay.jonesgroceries.entities.enums.ListType;
import ca.jonestremblay.jonesgroceries.viewmodel.UserListViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroceriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroceriesFragment extends Fragment implements UserListsAdapter.HandleUserListClick {

    private UserListViewModel viewModel;
    private TextView noResultLabel;
    private RecyclerView recyclerView;
    private UserListsAdapter groceryAdapter;
    private UserList groceryListToEdit;


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
        noResultLabel = rootView.findViewById(R.id.noUserListTxtView);

        ImageView btnAddNew = rootView.findViewById(R.id.add_new_grocery);
        initViewModel();
        initRecyclerView(rootView);
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateOrEditUserListDialog(null, ListType.grocery, false);
            }
        });
        viewModel.refreshUserList();
        /** Set the action's bar color with the user saved color */
        setActionBarColor();
        return rootView;
    }

    private void setActionBarColor() {
        /***
         *  Set the action's bar color with the user saved color
         */
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String hexColorCode = sharedPref.getString("actionBarColor", "#6200EE");
        ((AppCompatActivity)getActivity()).getSupportActionBar()
                .setBackgroundDrawable(new ColorDrawable(Color.parseColor(hexColorCode)));
    }

    private void initRecyclerView(View rootView){
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        groceryAdapter = new UserListsAdapter(this.getContext(), this, ListType.grocery);
        recyclerView.setAdapter(groceryAdapter);
        groceryAdapter.notifyDataSetChanged();
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(UserListViewModel.class);
        viewModel.setListType(ListType.grocery.toString());
        viewModel.getListOfUserListObserver().observe(this.getActivity(), new Observer<List<UserList>>() {
            @Override
            public void onChanged(List<UserList> groceries) {
                /** Show or hide noResultLabel if necessary */
                if (groceries == null){
                    noResultLabel.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    // show the recyclerview
                    groceryAdapter.setUserLists(groceries);
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
    private void showCreateOrEditUserListDialog(UserList userList, ListType type, boolean isForEdit) {
        CreateOrEditUserListDialog dialog =
                new CreateOrEditUserListDialog(getContext(), isForEdit, viewModel, userList, type);
    }

    private void showDeleteUserListDialog(UserList userList){
        DeleteUserListDialog dialog = new DeleteUserListDialog(getContext(), viewModel, userList);
    }

    @Override
    public void itemClick(UserList userList) {
        Fragment showProducts = new ItemsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("list_id", userList.getListId());
        bundle.putString("list_name", userList.getListName());
        bundle.putString("list_type", ListType.grocery.toString());
        showProducts.setArguments(bundle);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.groceriesFragment, showProducts );
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

    @Override
    public void removeItem(UserList userList) {
        showDeleteUserListDialog(userList);
    }

    @Override
    public void editItem(UserList userList) {
        this.groceryListToEdit = userList;
        showCreateOrEditUserListDialog(userList, ListType.grocery, true);
    }

}