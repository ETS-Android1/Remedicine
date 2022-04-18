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
import com.iti.mad42.remedicine.EditMed.View.EditMed;
import com.iti.mad42.remedicine.Model.MedicationPojo;
import com.iti.mad42.remedicine.Model.MedicineDose;
import com.iti.mad42.remedicine.Model.Utility;
import com.iti.mad42.remedicine.R;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    MedicationPojo myMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_details);
        initView();
        //addDummyData();
        myMed = getIntent().getParcelableExtra("fromActiveToDetails");
        Log.i("TAG", ""+myMed.getName());
        Log.i("TAG", ""+myMed.getInstructions());
        Log.i("TAG", ""+myMed.getMedDoseReminders());
        Log.i("TAG", ""+myMed.getRecurrencePerWeekIndex());
        medicationNameLabel.setText(myMed.getName());

        medicationStrengthLabel.setText(myMed.getStrength()+" "+Utility.medStrengthUnit[myMed.getStrengthUnitIndex()]);
        medDurationLabel.setText(Utility.medReminderPerWeekList[myMed.getRecurrencePerWeekIndex()]+", for "+calcIntervalDays()+" days.");
        medTimeRecyclerview.setHasFixedSize(true);
        adapter = new RemindersRecyclerAdapter(this, medDose);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        medTimeRecyclerview.setLayoutManager(layoutManager);
        medTimeRecyclerview.setAdapter(adapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MedDetails.this, AddDose.class));
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG", "onClick: ");
                startActivity(new Intent(MedDetails.this, EditMed.class));
            }
        });
    }

    void addDummyData() {
        medDose.add(new MedicineDose(Utility.medForm[0],1,1650153600000L ));
        medDose.add(new MedicineDose(Utility.medForm[0],1,1650153600000L ));
        medDose.add(new MedicineDose(Utility.medForm[0],1,1650153600000L ));

    }

    public String longToDateAsString(long dateInMillis){

        Date d = new Date(dateInMillis);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(d);
    }

    public long calcIntervalDays(){
        final DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy");
        final LocalDate start = dtf.parseLocalDate(longToDateAsString(myMed.getStartDate()));
        final LocalDate end = dtf.parseLocalDate(longToDateAsString(myMed.getEndDate())).plusDays(1);

        return Days.daysBetween(new LocalDate(start), new LocalDate(end)).getDays();

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