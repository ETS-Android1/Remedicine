package com.iti.mad42.remedicine.EditMed.View;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.iti.mad42.remedicine.AddNewMedicine.View.AddNewMedicineActivity;
import com.iti.mad42.remedicine.MedDetails.View.RemindersRecyclerAdapter;
import com.iti.mad42.remedicine.Model.MedicineDose;
import com.iti.mad42.remedicine.R;

import java.util.ArrayList;
import java.util.List;

public class EditMed extends AppCompatActivity {

    private ImageView back;
    private TextInputLayout editMedicationNameEdt;
    private TextInputLayout editMedicationDoseEdt;
    private Spinner editMedicationFormSpinner;
    private TextInputLayout editMedicationStrengthUnitEdt;
    private Spinner editMedicationStrengthUnitSpinner;
    private TextInputLayout editMedicationReasonEdt;
    private TextInputLayout editMedicationInstructionEdt;
    private Spinner editMedicationReoccurrenceSpinner;
    private RecyclerView editMedTimeRecyclerview;
    private Spinner editMedicationReoccurrenceIntervalSpinner;
    private TextInputLayout editMedicationTreatmentDurationFromEdt;
    private TextInputLayout editMedicationTreatmentDurationToEdt;
    private TextInputLayout editMedicationLeftEdt;
    private TextInputLayout editMedicationRefillReminderNumOfItemsEdt;
    private TextInputLayout editMedicationRefillReminderTimeEdt;
    private Button saveEditBtn;

    List<MedicineDose> medDose = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    EditRemindersRecyclerAdapter adapter;
    String [] medForm ={"pill","capsule"};
    String [] medStrengthUnit ={"g","mg"};
    String [] medReoccurrence ={"one time per day","two times per day","three times per day"};
    String [] medReoccurrenceInterval ={"Every day","Every two days","Every three days"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_med);
        initView();
        addDummyData();
        editMedTimeRecyclerview.setHasFixedSize(true);
        adapter = new EditRemindersRecyclerAdapter(this, medDose);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        editMedTimeRecyclerview.setLayoutManager(layoutManager);
        editMedTimeRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ArrayAdapter<String> medFormAdapter = new ArrayAdapter<>(EditMed.this, android.R.layout.simple_spinner_dropdown_item, medForm);
        editMedicationFormSpinner.setAdapter(medFormAdapter);
        editMedicationFormSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedTime = times.get(position);
//                selectedTimeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> medStrengthUnitAdapter = new ArrayAdapter<>(EditMed.this, android.R.layout.simple_spinner_dropdown_item, medStrengthUnit);
        editMedicationStrengthUnitSpinner.setAdapter(medStrengthUnitAdapter);
        editMedicationStrengthUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedTime = times.get(position);
//                selectedTimeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> medReoccurrenceAdapter = new ArrayAdapter<>(EditMed.this, android.R.layout.simple_spinner_dropdown_item, medReoccurrence);
        editMedicationReoccurrenceSpinner.setAdapter(medReoccurrenceAdapter);
        editMedicationReoccurrenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedTime = times.get(position);
//                selectedTimeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> medReoccurrenceIntervalAdapter = new ArrayAdapter<>(EditMed.this, android.R.layout.simple_spinner_dropdown_item, medReoccurrenceInterval);
        editMedicationReoccurrenceIntervalSpinner.setAdapter(medReoccurrenceIntervalAdapter);
        editMedicationReoccurrenceIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedTime = times.get(position);
//                selectedTimeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    void addDummyData() {
        medDose.add(new MedicineDose("Pill", 3, 9, 30));
        medDose.add(new MedicineDose("Pill", 3, 9, 30));
        medDose.add(new MedicineDose("Pill", 3, 9, 30));

    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        editMedicationNameEdt = (TextInputLayout) findViewById(R.id.edit_medication_name_edt);
        editMedicationDoseEdt = (TextInputLayout) findViewById(R.id.edit_medication_dose_edt);
        editMedicationFormSpinner = (Spinner) findViewById(R.id.edit_medication_form_spinner);
        editMedicationStrengthUnitEdt = (TextInputLayout) findViewById(R.id.edit_medication_strength_unit_edt);
        editMedicationStrengthUnitSpinner = (Spinner) findViewById(R.id.edit_medication_strength_unit_spinner);
        editMedicationReasonEdt = (TextInputLayout) findViewById(R.id.edit_medication_reason_edt);
        editMedicationInstructionEdt = (TextInputLayout) findViewById(R.id.edit_medication_instruction_edt);
        editMedicationReoccurrenceSpinner = (Spinner) findViewById(R.id.edit_medication_Reoccurrence_spinner);
        editMedTimeRecyclerview = (RecyclerView) findViewById(R.id.edit_med_time_recyclerview);
        editMedicationReoccurrenceIntervalSpinner = (Spinner) findViewById(R.id.edit_medication_Reoccurrence_interval_spinner);
        editMedicationTreatmentDurationFromEdt = (TextInputLayout) findViewById(R.id.edit_medication_treatment_Duration_from_edt);
        editMedicationTreatmentDurationToEdt = (TextInputLayout) findViewById(R.id.edit_medication_treatment_Duration_to_edt);
        editMedicationLeftEdt = (TextInputLayout) findViewById(R.id.edit_medication_left_edt);
        editMedicationRefillReminderNumOfItemsEdt = (TextInputLayout) findViewById(R.id.edit_medication_refill_reminder_num_of_items_edt);
        editMedicationRefillReminderTimeEdt = (TextInputLayout) findViewById(R.id.edit_medication_refill_reminder_time_edt);
        saveEditBtn = (Button) findViewById(R.id.save_edit_btn);
    }
}