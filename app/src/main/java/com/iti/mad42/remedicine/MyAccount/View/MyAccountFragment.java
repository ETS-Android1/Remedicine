package com.iti.mad42.remedicine.MyAccount.View;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.iti.mad42.remedicine.Model.database.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.Model.pojo.CurrentUser;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.Model.pojo.RequestPojo;
import com.iti.mad42.remedicine.Model.pojo.User;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.MyAccount.Presenter.MyAccountPresenter;
import com.iti.mad42.remedicine.MyAccount.Presenter.MyAccountPresenterInterface;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.Requests.View.RequestsViewActivity;
import com.iti.mad42.remedicine.data.FacebookAuthentication.RemoteDataSource;
import com.iti.mad42.remedicine.login.view.view.LoginActivity;

import java.util.ArrayList;
import java.util.List;


public class MyAccountFragment extends Fragment implements MyAccountFragmentInterface , OnRowClickListenerInterface{

    ConstraintLayout myRequestsConst, addMedfriendConst, switchAcc, logoutConst;
    Dialog addMedfriendDialog, switchAccountDialog;
    Intent intent;
    MyAccountPresenterInterface presenter;
    RecyclerView usersToSwitchLV;
    UsersToSwitchAdapter usersAdapter;


    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MyAccountPresenter(getActivity().getApplicationContext(),this, Repository.getInstance(getContext(), ConcreteLocalDataSource.getInstance(getContext()), RemoteDataSource.getInstance(getContext(), new CallbackManager() {
            @Override
            public boolean onActivityResult(int i, int i1, @Nullable Intent intent) {
                return false;
            }
        })));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        initAccountScreenUI(view);
        goToMyRequestsScreen();
        goToAddMedFriendDialog();
        goToSwitchAccountScreen();
        doLogoutFromAccount();
        return view;
    }

    public void initAccountScreenUI(View view){
        myRequestsConst = view.findViewById(R.id.onClickRequests);
        addMedfriendConst = view.findViewById(R.id.onClickAddMedfriend);
        switchAcc = view.findViewById(R.id.onClickSwitch);
        logoutConst = view.findViewById(R.id.onClickLogout);
        addMedfriendDialog = new Dialog(getContext());
        switchAccountDialog = new Dialog(getContext());
    }

    private void goToMyRequestsScreen(){
        myRequestsConst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToRequests = new Intent(view.getContext(),RequestsViewActivity.class);
                startActivity(goToRequests);
            }
        });
    }

    private void goToAddMedFriendDialog(){
        addMedfriendConst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddMedfriendDialog();
            }
        });
    }

    private void goToSwitchAccountScreen(){
        switchAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSwitchAccountListDialog();
            }
        });
    }

    private void doLogoutFromAccount(){
        logoutConst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutFromApp();
            }
        });
    }

    public void openAddMedfriendDialog(){
        addMedfriendDialog.setContentView(R.layout.add_medfriend_dialog);
        addMedfriendDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button sendBtn, cancelBtn;
        EditText receiverEmailEdt;
        sendBtn = addMedfriendDialog.findViewById(R.id.sendMedBtn);
        cancelBtn = addMedfriendDialog.findViewById(R.id.cancelMedBtn);
        receiverEmailEdt = addMedfriendDialog.findViewById(R.id.sendRequestEdt);
        ImageView closeDialog = addMedfriendDialog.findViewById(R.id.dialogCloseBtn);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMedfriendDialog.dismiss();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.sendRequest(new RequestPojo(getString(Utility.myCredentials), receiverEmailEdt.getText().toString(), "notFriend"));
                addMedfriendDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMedfriendDialog.dismiss();
            }
        });

        addMedfriendDialog.show();
    }
    public void openSwitchAccountListDialog(){
        switchAccountDialog.setContentView(R.layout.medfriends_list);
        switchAccountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        usersToSwitchLV = switchAccountDialog.findViewById(R.id.usersListView);

        ImageView closeDialog = switchAccountDialog.findViewById(R.id.medfriensListCloseBtn);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchAccountDialog.dismiss();
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        usersAdapter = new UsersToSwitchAdapter(new ArrayList<>(), getContext());
        usersAdapter.setOnRowClickListener(this);
        usersToSwitchLV.setHasFixedSize(true);
        manager.setOrientation(RecyclerView.VERTICAL);
        usersToSwitchLV.setLayoutManager(manager);
        usersToSwitchLV.setAdapter(usersAdapter);

        presenter.getAllUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                showAllUsers(users);
                usersAdapter.notifyDataSetChanged();
            }
        });

        switchAccountDialog.show();
    }

    public String getString(String key){
        SharedPreferences sharedPreferences=
                getContext().getSharedPreferences("LoginTest",MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }

    private void logoutFromApp() {
        if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null){
            LoginManager.getInstance().logOut();
            Log.i("SharedPrefs", "onLogout: " + LoginManager.getInstance().getAuthType());

        }
        presenter.saveString(Utility.myCredentials,null);
        SharedPreferences prefs = getContext().getSharedPreferences("LoginTest", MODE_PRIVATE);
        String name = prefs.getString(Utility.myCredentials, null);//"No name defined" is the default value.
        Log.i("SharedPrefs", "onCreate: " + name);
        intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showAllUsers(List<User> users) {
        usersAdapter.setUsersList(users);
        usersAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClickRowItem(User user) {
        if(user.getEmail().equals(getString(Utility.myCredentials))){
            Utility.isMyAccount = true;
            Toast.makeText(getContext(), "This is Already your Account, You can't switch to!", Toast.LENGTH_SHORT).show();
        }else{
            //save in shared pref the current medfriend
            CurrentUser.getInstance().setEmail(user.getEmail().trim());
            Utility.isMyAccount = false;
            // make flag isMedfriendViewer
            //isMyAccount true --> getFrom Room
            //isMyAccount false --> getFrom Firebase with the currentMedfriend email --> in shaerd pref
//            checkISMyAccountAndGetMedData();
        }
    }


    @Override
    public void sendAllMedsToHomeScreen(List<MedicationPojo> meds) {
        Log.e("sandra","meds in account view count is: "+ meds.size());
        for(int i = 0;i<meds.size();i++){
            Log.e("sandra","meds in account view name is : "+ meds.get(i).getName());
            Log.e("sandra","meds in account view medOwner is: "+ meds.get(i).getMedOwnerEmail());
            Log.e("sandra","meds in account view reason is: "+ meds.get(i).getReason());
        }
    }

//    public void saveString (String key ,String value){
//        SharedPreferences.Editor editor = getContext().getSharedPreferences("LoginTest",MODE_PRIVATE).edit();
//        editor.putString(key,value);
//        editor.apply();
//    }

//    public void checkISMyAccountAndGetMedData(){
//        if(!Utility.isMyAccount){
//            //get data from fb with current med friend
//            CurrentUser.getInstance().setEmail("");
//            presenter.getAllMedsForMedfriend(getString(Utility.currentMedFriend));
//        }
//    }
}