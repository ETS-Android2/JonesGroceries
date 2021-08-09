package ca.jonestremblay.jonesgroceries.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ca.jonestremblay.jonesgroceries.R;
import ca.jonestremblay.jonesgroceries.entities.UserList;
import ca.jonestremblay.jonesgroceries.viewmodel.UserListViewModel;

public class DeleteUserListDialog extends AlertDialog {

    View view;
    UserListViewModel viewModel;
    Button deleteButton;
    Button cancelButton;
    TextView listName;
    UserList clickedUserList;

    public DeleteUserListDialog(Context context,  UserListViewModel viewModel, UserList clickedUserList) {
        super(context);
        this.viewModel = viewModel;
        this.clickedUserList = clickedUserList;
        setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dialog_delete_user_list, null);
        setWidgets();
        setListeners();
        setView(view);
        show();
        getWindow().setLayout(1000, 585);
    }

    private void setWidgets() {
        /** Instanciation des objets UI */
        listName = view.findViewById(R.id.listName);
        deleteButton = view.findViewById(R.id.deleteButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        listName.setText(clickedUserList.getListName());
    }

    private void setListeners() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteUserList(clickedUserList);
                dismiss();
            }
        });
    }
}
