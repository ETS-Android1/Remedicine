package com.iti.mad42.remedicine.MedDetails.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.iti.mad42.remedicine.AddDose.View.AddDose;
import com.iti.mad42.remedicine.EditMed.View.EditMedActivity;
import com.iti.mad42.remedicine.Model.pojo.MedState;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.MedicineDose;
import com.iti.mad42.remedicine.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedDetails extends AppCompatActivity {

    RemindersRecyclerAdapter adapter;
    List<MedicineDose>medDose = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    private ImageView back;
    private ImageView delete;
    private ImageView edit;
    private TextView title;
    private ImageView medImg;
    private TextView medicationNameLabel;
    private TextView medicationStrengthLabel;
    private View seperator;
    private TextView medDurationLabel;
    private MaterialButton suspendBtn;
    private RecyclerView medTimeRecyclerview;
    private TextView medReasonLabel;
    private TextView pillsLeftLabel;
    private TextView prescriptionRefillLabel;
    private MaterialButton refillBtn;
    private TextView howToUseLabel;
    private Button add;
    private TextView lastTimeTakenLabel;
    List<String> medDays = new ArrayList<>();
    List<MedState>medStates = new ArrayList<>();
    MedicationPojo medicationPojo;
       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_details);
        initView();

        Log.i("sandra", "Med Details Name:" + medicationPojo.getName());
        medicationNameLabel.setText(medicationPojo.getName());
        medicationStrengthLabel.setText(medicationPojo.getStrength());
        medTimeRecyclerview.setHasFixedSize(true);
        adapter = new RemindersRecyclerAdapter(this, medicationPojo.getMedDoseReminders());
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        medTimeRecyclerview.setLayoutManager(layoutManager);
        medTimeRecyclerview.setAdapter(adapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedDetails.this, AddDose.class);
                startActivity(intent);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedDetails.this, EditMedActivity.class);
                intent.putExtra("fromDetailsToEdit",(Serializable) (MedicationPojo) getIntent().getSerializableExtra("fromActiveToDetails"));
                startActivity(intent);
            }
        });
    }

    void addDummyData() {

        medDose.add(new MedicineDose("Pill", 2, 1650234756160L));
        medDose.add(new MedicineDose("Pill", 2, 1650234756160L));
        medDose.add(new MedicineDose("Pill", 2, 1650234756160L));

        medDays.add("Saturday");
        medDays.add("Monday");

        medStates.add(new MedState(1652740553000L, "none"));
        medStates.add(new MedState(1652740553000L, "none"));

    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        delete = (ImageView) findViewById(R.id.delete);
        edit = (ImageView) findViewById(R.id.edit);
        title = (TextView) findViewById(R.id.title);
        medImg = (ImageView) findViewById(R.id.med_img);
        medicationNameLabel = (TextView) findViewById(R.id.medication_name_label);
        medicationStrengthLabel = (TextView) findViewById(R.id.medication_strength_label);
        seperator = (View) findViewById(R.id.seperator);
        medDurationLabel = (TextView) findViewById(R.id.med_duration_label);
        suspendBtn = (MaterialButton) findViewById(R.id.suspend_btn);
        medTimeRecyclerview = (RecyclerView) findViewById(R.id.med_time_recyclerview);
        medReasonLabel = (TextView) findViewById(R.id.med_reason_label);
        pillsLeftLabel = (TextView) findViewById(R.id.pills_left_label);
        prescriptionRefillLabel = (TextView) findViewById(R.id.prescription_refill_label);
        refillBtn = (MaterialButton) findViewById(R.id.refill_btn);
        howToUseLabel = (TextView) findViewById(R.id.how_to_use_label);
        add = (Button) findViewById(R.id.add);
        lastTimeTakenLabel = (TextView) findViewById(R.id.last_time_taken_label);
    }
}