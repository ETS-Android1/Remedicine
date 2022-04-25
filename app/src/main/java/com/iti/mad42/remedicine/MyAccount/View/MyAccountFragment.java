package com.iti.mad42.remedicine.MyAccount.View;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<User> users) {
                //users.stream().distinct().collect(Collectors.toList());
                //users.stream().filter( distinctByKey(user -> user.getEmail())).distinct().collect(Collectors.toList());
                showAllUsers(users);
                usersAdapter.notifyDataSetChanged();
            }
        });

        switchAccountDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
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
        String name = prefs.getString(Utility.myCredentials, null);
        CurrentUser.getInstance().setEmail(null);
        presenter.emptyLocalDB();
        //"No name defined" is the default value.
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
        Log.e("sandra", "Current user is: "+getString(Utility.myCredentials));
        Log.e("sandra", "Current user get instance is: "+CurrentUser.getInstance().getEmail());

        if(user.getEmail().equals(getString(Utility.myCredentials)) && CurrentUser.getInstance().getEmail().equals(Utility.myCredentials)){
            Utility.isMyAccount = true;
            Toast.makeText(getContext(), "This is Already your Account, You can't switch to!", Toast.LENGTH_SHORT).show();
        }else if(user.getEmail().equals(getString(Utility.myCredentials)) && !CurrentUser.getInstance().getEmail().equals(Utility.myCredentials)){
            Utility.isMyAccount = true;
            CurrentUser.getInstance().setEmail(getString(Utility.myCredentials));
            switchAccountDialog.dismiss();
            Toast.makeText(getContext(), "Switched to your account successfully", Toast.LENGTH_SHORT).show();
        }else{
            CurrentUser.getInstance().setEmail(user.getEmail().trim());
            Utility.isMyAccount = false;
            switchAccountDialog.dismiss();
            Toast.makeText(getContext(), "switched successfully", Toast.LENGTH_SHORT).show();
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

}