package com.iti.mad42.remedicine.healthTakerRequests.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.iti.mad42.remedicine.data.localDataSource.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.data.repositry.Repository;
import com.iti.mad42.remedicine.data.pojo.RequestPojo;
import com.iti.mad42.remedicine.data.pojo.User;
import com.iti.mad42.remedicine.utility.Utility;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.healthTakerRequests.Presenter.RequestsPresenter;
import com.iti.mad42.remedicine.healthTakerRequests.Presenter.RequestsPresenterInterface;
import com.iti.mad42.remedicine.data.remoteDataSource.RemoteDataSource;

import java.util.List;

public class RequestsViewActivity extends AppCompatActivity implements RequestsViewActivityInterface, OnClickListenerInterface{

    RecyclerView requestsRecycler;
    RequestScreenAdapter requestAdapter;
    ImageView backBtn;
    RequestsPresenterInterface presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_view);
        initUI();
        setAdapter();
        setPresenter();
        presenter.getAllRequests(getString(Utility.myCredentials));

    }
    void initUI(){
        requestsRecycler = findViewById(R.id.requestsRecycler);
        backBtn = findViewById(R.id.backRequest);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    void setPresenter(){
        presenter = new RequestsPresenter(this,this, Repository.getInstance(this, ConcreteLocalDataSource.getInstance(this), RemoteDataSource.getInstance(this, new CallbackManager() {
            @Override
            public boolean onActivityResult(int i, int i1, @Nullable Intent intent) {
                return false;
            }
        })));
    }
    void setAdapter(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        requestsRecycler.setHasFixedSize(true);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        requestsRecycler.setLayoutManager(layoutManager);
    }

    public void updateListWhenReject(RequestPojo request){
        presenter.rejectRequest(request);
        requestAdapter.notifyDataSetChanged();
    }
    public void updateListWhenAccept(RequestPojo request){
        presenter.updateStateWhenAcceptRequest(request);
        requestAdapter.notifyDataSetChanged();
    }
    @Override
    public void onClickAcceptBtn(RequestPojo request) {
        updateListWhenAccept(request);
        presenter.insertMedFriend(new User(getString(Utility.currentMedFriend), "",""));
       // presenter.updateStateWhenAcceptRequest(request);
    }

    @Override
    public void onClickRejectBtn(RequestPojo request) {
        updateListWhenReject(request);
    }

    public String getString(String key){
        SharedPreferences sharedPreferences=
                getApplicationContext().getSharedPreferences("LoginTest",MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }
    @Override
    public void getAllRequests(List<RequestPojo> requests) {
        requestAdapter = new RequestScreenAdapter(RequestsViewActivity.this, requests, this);
        requestsRecycler.setAdapter(requestAdapter);
        requestAdapter.setList(requests);
        requestAdapter.notifyDataSetChanged();
    }

}