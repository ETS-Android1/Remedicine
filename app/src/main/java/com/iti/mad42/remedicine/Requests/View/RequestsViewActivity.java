package com.iti.mad42.remedicine.Requests.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.iti.mad42.remedicine.Model.Medication;
import com.iti.mad42.remedicine.R;

import java.util.ArrayList;

public class RequestsViewActivity extends AppCompatActivity {

    RecyclerView requestsRecycler;
    RequestScreenAdapter requestAdapter;
    ArrayList<Medication> myMeds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_view);
        myMeds.add(new Medication("Panadol", "500","g", "After Eating", "15"));
        myMeds.add(new Medication("Panadol", "500","g", "After Eating", "15"));
        myMeds.add(new Medication("Panadol", "500","g", "After Eating", "15"));

        requestsRecycler = findViewById(R.id.requestsRecycler);
        requestsRecycler.setHasFixedSize(true);
        requestAdapter = new RequestScreenAdapter(RequestsViewActivity.this, myMeds);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        requestsRecycler.setLayoutManager(layoutManager);

        requestsRecycler.setAdapter(requestAdapter);

        requestAdapter.notifyDataSetChanged();
    }
}