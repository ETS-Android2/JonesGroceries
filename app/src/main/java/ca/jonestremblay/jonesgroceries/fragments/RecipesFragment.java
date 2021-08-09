package ca.jonestremblay.jonesgroceries.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Use the {@link RecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesFragment extends Fragment implements UserListsAdapter.HandleUserListClick{

    private View fragmentView;
    private UserListViewModel viewModel;
    private TextView noResultLabel;
    private RecyclerView recyclerView;
    private UserListsAdapter userListAdapter;
    private UserList recipeToEdit;
    private ImageView btnAddNew;

    public static Fragment newInstance() {
        RecipesFragment fragment = new RecipesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleOnBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_recipes, container, false);
        setWidgets();
        setListeners();
        initViewModel();
        initRecyclerView();
        viewModel.refreshUserList();
        return fragmentView;
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

    public void setWidgets() {
        btnAddNew = fragmentView.findViewById(R.id.add_new_recipe);
        noResultLabel  = fragmentView.findViewById(R.id.noUserListTxtView);
        recyclerView = fragmentView.findViewById(R.id.recyclerView);
    }

    public void setListeners(){
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateOrEditUserListDialog(null, ListType.recipe, false);
            }
        });
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        userListAdapter = new UserListsAdapter(this.getContext(), this, ListType.recipe);
        recyclerView.setAdapter(userListAdapter);
        userListAdapter.notifyDataSetChanged();
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(UserListViewModel.class);
        viewModel.setListType(ListType.recipe.toString());
        viewModel.getListOfUserListObserver().observe(this.getActivity(), new Observer<List<UserList>>() {
            @Override
            public void onChanged(List<UserList> userLists) {
                /** Show or hide noResultLabel if necessary */
                if (userLists == null){
                    noResultLabel.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    /** show the recyclerview */
                    userListAdapter.setUserLists(userLists);
                    noResultLabel.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void itemClick(UserList userList) {
        Fragment showProducts = new ItemsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("list_id", userList.getListId());
        bundle.putString("list_name", userList.getListName());
        bundle.putString("list_type", ListType.recipe.toString());
        showProducts.setArguments(bundle);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(getView().getId(), showProducts );
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

    @Override
    public void removeItem(UserList userList) {
        showDeleteUserListDialog(userList);
    }

    @Override
    public void editItem(UserList userList) {
        this.recipeToEdit = userList;
        showCreateOrEditUserListDialog(userList,ListType.recipe, true);
    }

    private void showCreateOrEditUserListDialog(UserList userList, ListType type, boolean isForEdit) {
        CreateOrEditUserListDialog dialog =
                new CreateOrEditUserListDialog(getContext(), isForEdit, viewModel, userList, type);
    }

    private void showDeleteUserListDialog(UserList userList){
        DeleteUserListDialog dialog = new DeleteUserListDialog(getContext(), viewModel, userList);
    }

}