package ca.jonestremblay.jonesgroceries.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.entities.UserList;
import ca.jonestremblay.jonesgroceries.entities.enums.ListType;
import ca.jonestremblay.jonesgroceries.viewmodel.UserListViewModel;

public class CreateOrEditUserListDialog extends AlertDialog {

    final int MAX_CHAR_USER_LIST_NAME = 15;
    View dialogView;
    TextView titleTextView;
    EditText listNameInput;
    TextView createButton;
    TextView cancelButton;
    boolean isForEdit;
    UserListViewModel viewModel;
    UserList clickedUserList;
    private Resources resources;
    private String typeList;

    public CreateOrEditUserListDialog(Context context, boolean isForEdit, UserListViewModel viewModel,
                                      UserList clickedUserList, ListType type) {
        super(context);
        this.isForEdit = isForEdit;
        this.viewModel = viewModel;
        this.clickedUserList = clickedUserList;
        this.typeList = type.toString();

        this.resources = getContext().getResources();
        LayoutInflater inflater = LayoutInflater.from(context);
        dialogView = inflater.inflate(R.layout.dialog_add_user_list, null);
        setCancelable(false);
        setWidgets();
        setListeners();
        setView(dialogView);
        show();
        // getWindow().setLayout(1000, 585);
    }


    private void setWidgets() {
        titleTextView = dialogView.findViewById(R.id.titleTextView);
        if (typeList.equals(ListType.grocery.toString())){
            titleTextView.setText(getContext().getString(R.string.newNameForList));
        } else {
            titleTextView.setText(getContext().getString(R.string.newNameForRecipe));
        }
        listNameInput = dialogView.findViewById(R.id.enterCategoryInput);
        createButton = dialogView.findViewById(R.id.createButton);
        cancelButton = dialogView.findViewById(R.id.cancelButton);
        if (clickedUserList != null){
            clickedUserList.setType(typeList);
        }
    }

    private void setListeners() {
        listNameInput.setFilters(new InputFilter[] { new InputFilter.LengthFilter(MAX_CHAR_USER_LIST_NAME) });
        if (isForEdit){
            createButton.setText(R.string.confirm);
            listNameInput.setText(clickedUserList.getListName());
        }
        /** Listeners setters */
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = listNameInput.getText().toString();
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(getOwnerActivity(), resources.getString(R.string.newNameForList), Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (isForEdit){
                    clickedUserList.setListName(name);
                    if (viewModel.updateUserList(clickedUserList) == 0){
                        listNameInput.setText("");
                    } else {
                        dismiss();
                    }
                } else {
                    if (name.length() > MAX_CHAR_USER_LIST_NAME){
                        System.out.println(resources.getString(R.string.nameTooLong));
                        listNameInput.setText("");
                    } else {
                        UserList userList = new UserList();
                        userList.setType(typeList);
                        userList.setIconId(0);
                        userList.setListName(name);
                        viewModel.insertUserList(userList);
                        dismiss();
                    }
                }
                /** here we need to call view model */
            }
        });

    }
}
