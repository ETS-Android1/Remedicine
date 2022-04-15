package com.iti.mad42.remedicine.MyAccount.View;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.Requests.View.RequestsViewActivity;


public class MyAccountFragment extends Fragment {

    ConstraintLayout myRequestsConst, addMedfriendConst, switchAcc;
    Dialog addMedfrienDialog, showReminderDialog;

    public MyAccountFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        myRequestsConst = view.findViewById(R.id.onClickRequests);
        addMedfriendConst = view.findViewById(R.id.onClickAddMedfriend);
        switchAcc = view.findViewById(R.id.onClickSwitch);

        addMedfrienDialog = new Dialog(getContext());
        showReminderDialog = new Dialog(getContext());

        myRequestsConst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToRequests = new Intent(view.getContext(),RequestsViewActivity.class);
                startActivity(goToRequests);
            }
        });

        addMedfriendConst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddMedfriendDialog();
            }
        });

        switchAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReminderDialog();
            }
        });
        return view;
    }

    public void openAddMedfriendDialog(){
        addMedfrienDialog.setContentView(R.layout.add_medfriend_dialog);
        addMedfrienDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView closeDialog = addMedfrienDialog.findViewById(R.id.dialogCloseBtn);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMedfrienDialog.dismiss();
            }
        });


        Button sendBtn, cancelBtn;
        sendBtn = addMedfrienDialog.findViewById(R.id.sendMedBtn);
        cancelBtn = addMedfrienDialog.findViewById(R.id.cancelMedBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Send Btn Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Cancel Btn Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        addMedfrienDialog.show();
    }
    public void openReminderDialog(){
        showReminderDialog.setContentView(R.layout.medication_reminder_dialog);
        showReminderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView closeDialog = showReminderDialog.findViewById(R.id.reminderDialogCloseBtn);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReminderDialog.dismiss();
            }
        });


        showReminderDialog.show();
    }
}