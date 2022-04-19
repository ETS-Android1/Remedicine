package com.iti.mad42.remedicine.AddNewMedicine.View;

import static com.iti.mad42.remedicine.Model.pojo.Utility.medReminderPerDayList;
import static com.iti.mad42.remedicine.Model.pojo.Utility.medReminderPerWeekList;

import static com.iti.mad42.remedicine.Model.pojo.Utility.medStrengthUnit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.CallbackManager;
import com.google.android.material.textfield.TextInputLayout;
import com.iti.mad42.remedicine.AddNewMedicine.Presenter.AddNewMedicineActivityPresenter;
import com.iti.mad42.remedicine.AddNewMedicine.Presenter.AddNewMedicineActivityPresenterInterface;
import com.iti.mad42.remedicine.EditMed.View.EditRemindersRecyclerAdapter;
import com.iti.mad42.remedicine.Model.database.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.Model.pojo.MedicineDose;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.data.FacebookAuthentication.RemoteDataSource;

import java.util.List;

public class AddNewMedicineActivity extends AppCompatActivity implements AddNewMedicineActivityInterface {

    private ImageView back;
    private TextInputLayout medicationNameEdt;
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
    private RecyclerView editMedTimeRecyclerview;
    private Button add;

    RecyclerView.LayoutManager layoutManager;
    EditRemindersRecyclerAdapter adapter;
    AddNewMedicineActivityPresenterInterface presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_medicine);
        initView();
        initRecyclerView();
        setAdapters();
        setListeners();
        presenter = new AddNewMedicineActivityPresenter(this,this, Repository.getInstance(this, ConcreteLocalDataSource.getInstance(this), RemoteDataSource.getInstance(this, new CallbackManager() {
            @Override
            public boolean onActivityResult(int i, int i1, @Nullable Intent intent) {
                return false;
            }
        })));

    }

    private void initRecyclerView(){
        editMedTimeRecyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        editMedTimeRecyclerview.setLayoutManager(layoutManager);

    }
    private void setListeners(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        medicationFormSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setMedicineForm(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        medicationReoccurrenceIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               presenter.setInterval(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        medicationStrengthUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setMedStrength(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        medicationReoccurrenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter = new EditRemindersRecyclerAdapter(view.getContext(), presenter.getMedDose(position));
                editMedTimeRecyclerview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        medicationRefillReminderTimeEdt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openTimePicker();
            }
        });

        medicationTreatmentDurationFromEdt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openDatePicker("from");

            }
        });

        medicationTreatmentDurationToEdt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openDatePicker("to");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.insertMedication();
            }
        });

    }

    private void setAdapters(){
        ArrayAdapter<String> medFormAdapter = new ArrayAdapter<>(AddNewMedicineActivity.this, android.R.layout.simple_spinner_dropdown_item, Utility.medForm);
        medicationFormSpinner.setAdapter(medFormAdapter);

        ArrayAdapter<String> medStrengthUnitAdapter = new ArrayAdapter<>(AddNewMedicineActivity.this, android.R.layout.simple_spinner_dropdown_item, medStrengthUnit);
        medicationStrengthUnitSpinner.setAdapter(medStrengthUnitAdapter);

        ArrayAdapter<String> medReoccurrenceAdapter = new ArrayAdapter<>(AddNewMedicineActivity.this, android.R.layout.simple_spinner_dropdown_item, medReminderPerDayList);
        medicationReoccurrenceSpinner.setAdapter(medReoccurrenceAdapter);

        ArrayAdapter<String> medReoccurrenceIntervalAdapter = new ArrayAdapter<>(AddNewMedicineActivity.this, android.R.layout.simple_spinner_dropdown_item, medReminderPerWeekList);
        medicationReoccurrenceIntervalSpinner.setAdapter(medReoccurrenceIntervalAdapter);
    }

    public void setStartDateTextView(String startDate){
        medicationTreatmentDurationFromEdt.getEditText().setText(startDate);
    }

    public void setEndDateTextView(String endDate){
        medicationTreatmentDurationToEdt.getEditText().setText(endDate);
    }

    public void setRefillTime(String time){
       medicationRefillReminderTimeEdt.getEditText().setText(time);
    }

    public List<MedicineDose> getDoseFromAdapter(){
        return adapter.getMyMeds();
    }

    public String getMedNameTextView(){
        return medicationNameEdt.getEditText().getText().toString();
    }

    public String getMedStrengthTextView(){
        return medicationStrengthUnitEdt.getEditText().getText().toString();
    }

    public String getMedReasonTextView(){
        return medicationReasonEdt.getEditText().getText().toString();
    }

    public String getMedInstructionTextView(){
        return medicationInstructionEdt.getEditText().getText().toString();
    }

    public int getMedQtyTextView(){
        return Integer.parseInt(medicationLeftEdt.getEditText().getText().toString());
    }

    public int getMedReminderQtyTextView(){
        return Integer.parseInt(medicationRefillReminderNumOfItemsEdt.getEditText().getText().toString());
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        medicationNameEdt = (TextInputLayout) findViewById(R.id.medication_name_edt);
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
        editMedTimeRecyclerview = (RecyclerView) findViewById(R.id.edit_med_time_recyclerview);

    }
}