package com.iti.mad42.remedicine.AddNewMedicine.View;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.iti.mad42.remedicine.R;

public class AddNewMedicineActivity extends AppCompatActivity {

    private ImageView back;
    private TextInputLayout medicationNameEdt;
    private TextInputLayout medicationDoseEdt;
    private Spinner medicationFormSpinner;
    private TextInputLayout medicationStrengthUnitEdt;
    private Spinner medicationStrengthUnitSpinner;
    private TextInputLayout medicationReasonEdt;
    private TextInputLayout medicationInstructionEdt;
    private Spinner medicationReoccurrenceSpinner;
    private Spinner medicationReoccurrenceIntervalSpinner;
    private TextInputLayout medicationTreatmentDurationFromEdt;
    private TextInputLayout medicationTreatmentDurationToEdt;
    private TextInputLayout medicationLeftEdt;
    private TextInputLayout medicationRefillReminderNumOfItemsEdt;
    private TextInputLayout medicationRefillReminderTimeEdt;
    private Button add;
    String [] medForm ={"pill","capsule"};
    String [] medStrengthUnit ={"g","mg"};
    String [] medReoccurrence ={"one time per day","two times per day","three times per day"};
    String [] medReoccurrenceInterval ={"Every day","Every two days","Every three days"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_medicine);
        initView();
        ArrayAdapter<String> medFormAdapter = new ArrayAdapter<>(AddNewMedicineActivity.this, android.R.layout.simple_spinner_dropdown_item, medForm);
        medicationFormSpinner.setAdapter(medFormAdapter);
        medicationFormSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedTime = times.get(position);
//                selectedTimeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> medStrengthUnitAdapter = new ArrayAdapter<>(AddNewMedicineActivity.this, android.R.layout.simple_spinner_dropdown_item, medStrengthUnit);
        medicationStrengthUnitSpinner.setAdapter(medStrengthUnitAdapter);
        medicationStrengthUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedTime = times.get(position);
//                selectedTimeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> medReoccurrenceAdapter = new ArrayAdapter<>(AddNewMedicineActivity.this, android.R.layout.simple_spinner_dropdown_item, medReoccurrence);
        medicationReoccurrenceSpinner.setAdapter(medReoccurrenceAdapter);
        medicationReoccurrenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedTime = times.get(position);
//                selectedTimeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> medReoccurrenceIntervalAdapter = new ArrayAdapter<>(AddNewMedicineActivity.this, android.R.layout.simple_spinner_dropdown_item, medReoccurrenceInterval);
        medicationReoccurrenceIntervalSpinner.setAdapter(medReoccurrenceIntervalAdapter);
        medicationReoccurrenceIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        medicationNameEdt = (TextInputLayout) findViewById(R.id.medication_name_edt);
        medicationDoseEdt = (TextInputLayout) findViewById(R.id.medication_dose_edt);
        medicationFormSpinner = (Spinner) findViewById(R.id.medication_form_spinner);
        medicationStrengthUnitEdt = (TextInputLayout) findViewById(R.id.medication_strength_unit_edt);
        medicationStrengthUnitSpinner = (Spinner) findViewById(R.id.medication_strength_unit_spinner);
        medicationReasonEdt = (TextInputLayout) findViewById(R.id.medication_reason_edt);
        medicationInstructionEdt = (TextInputLayout) findViewById(R.id.medication_instruction_edt);
        medicationReoccurrenceSpinner = (Spinner) findViewById(R.id.medication_Reoccurrence_spinner);
        medicationReoccurrenceIntervalSpinner = (Spinner) findViewById(R.id.medication_Reoccurrence_interval_spinner);
        medicationTreatmentDurationFromEdt = (TextInputLayout) findViewById(R.id.medication_treatment_Duration_from_edt);
        medicationTreatmentDurationToEdt = (TextInputLayout) findViewById(R.id.medication_treatment_Duration_to_edt);
        medicationLeftEdt = (TextInputLayout) findViewById(R.id.medication_left_edt);
        medicationRefillReminderNumOfItemsEdt = (TextInputLayout) findViewById(R.id.medication_refill_reminder_num_of_items_edt);
        medicationRefillReminderTimeEdt = (TextInputLayout) findViewById(R.id.medication_refill_reminder_time_edt);
        add = (Button) findViewById(R.id.add);
    }
}