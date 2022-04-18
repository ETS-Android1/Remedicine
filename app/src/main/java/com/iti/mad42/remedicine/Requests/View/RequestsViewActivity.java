package com.iti.mad42.remedicine.Requests.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.iti.mad42.remedicine.R;

public class RequestsViewActivity extends AppCompatActivity {

    RecyclerView requestsRecycler;
    RequestScreenAdapter requestAdapter;
    //ArrayList<Medication> myMeds = new ArrayList<>();
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_view);
        backBtn = findViewById(R.id.backRequest);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        myMeds.add(new Medication("Panadol", "500","g", "After Eating", "15"));
//        myMeds.add(new Medication("Panadol", "500","g", "After Eating", "15"));
//        myMeds.add(new Medication("Panadol", "500","g", "After Eating", "15"));

        requestsRecycler = findViewById(R.id.requestsRecycler);
        requestsRecycler.setHasFixedSize(true);
        //requestAdapter = new RequestScreenAdapter(RequestsViewActivity.this, myMeds);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        requestsRecycler.setLayoutManager(layoutManager);

        requestsRecycler.setAdapter(requestAdapter);

        requestAdapter.notifyDataSetChanged();
    }
}