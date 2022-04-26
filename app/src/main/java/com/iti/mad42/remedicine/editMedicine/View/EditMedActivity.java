package com.iti.mad42.remedicine.editMedicine.View;

import static com.iti.mad42.remedicine.utility.Utility.longToDateAsString;

import static com.iti.mad42.remedicine.utility.Utility.medReminderPerDayList;
import static com.iti.mad42.remedicine.utility.Utility.medReminderPerWeekList;
import static com.iti.mad42.remedicine.utility.Utility.medStrengthUnit;
import static com.iti.mad42.remedicine.utility.Utility.millisToTimeAsString;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.facebook.CallbackManager;
import com.google.android.material.textfield.TextInputLayout;
import com.iti.mad42.remedicine.editMedicine.Presenter.EditMedPresenter;
import com.iti.mad42.remedicine.editMedicine.Presenter.EditMedPresenterInterface;
import com.iti.mad42.remedicine.data.localDataSource.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.data.pojo.MedicationPojo;
import com.iti.mad42.remedicine.data.pojo.MedicineDose;
import com.iti.mad42.remedicine.data.repositry.Repository;
import com.iti.mad42.remedicine.utility.Utility;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.WorkManger.MyPeriodicWorkManger;
import com.iti.mad42.remedicine.data.remoteDataSource.RemoteDataSource;
import com.iti.mad42.remedicine.home.view.BottomNavigationBar;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EditMedActivity extends AppCompatActivity implements  EditMedActivityInterface{

    private ImageView back;
    private TextInputLayout editMedicationNameEdt;
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

    RecyclerView.LayoutManager layoutManager;
    EditRemindersRecyclerAdapter adapter;

    EditMedPresenterInterface presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_med);
        initView();
        initRecyclerView();
        setAdapters();
        setListeners();
        presenter = new EditMedPresenter(this,this, Repository.getInstance(this, ConcreteLocalDataSource.getInstance(this), RemoteDataSource.getInstance(this, new CallbackManager() {
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

    public void setDataToTextViews(MedicationPojo medicationPojo){

        Log.e("TAG", "setDataToTextViews: "+medicationPojo.getName());

        editMedicationFormSpinner.setSelection(medicationPojo.getFormIndex());
        editMedicationReoccurrenceIntervalSpinner.setSelection(medicationPojo.getRecurrencePerWeekIndex());
        editMedicationReoccurrenceSpinner.setSelection(medicationPojo.getRecurrencePerDayIndex());
        editMedicationStrengthUnitSpinner.setSelection(medicationPojo.getStrengthUnitIndex());
        editMedicationStrengthUnitEdt.getEditText().setText(medicationPojo.getStrength());
        editMedicationReasonEdt.getEditText().setText(medicationPojo.getReason());
        editMedicationNameEdt.getEditText().setText(medicationPojo.getName());
        editMedicationInstructionEdt.getEditText().setText(medicationPojo.getInstructions());
        editMedicationLeftEdt.getEditText().setText(medicationPojo.getMedQty()+"");
        editMedicationRefillReminderNumOfItemsEdt.getEditText().setText(medicationPojo.getReminderMedQtyLeft()+"");
        editMedicationRefillReminderTimeEdt.getEditText().setText(millisToTimeAsString(medicationPojo.getRefillReminderTimeInMilliSec()));
        editMedicationTreatmentDurationFromEdt.getEditText().setText(longToDateAsString(medicationPojo.getStartDate()));
        editMedicationTreatmentDurationToEdt.getEditText().setText(longToDateAsString(medicationPojo.getEndDate()));
        Log.e("TAG", "setDataToTextViews: "+medicationPojo.getMedDoseReminders().get(0));
        setMedDoseListToAdapter(medicationPojo.getMedDoseReminders());
    }

    private void setMedDoseListToAdapter(List<MedicineDose> medDose ){
        Log.e("mando", "setMedDoseListToAdapter: " + medDose.size());
        adapter = new EditRemindersRecyclerAdapter(this, medDose);
        editMedTimeRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void setListeners(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editMedicationFormSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setMedicineForm(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        editMedicationReoccurrenceIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setInterval(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        editMedicationStrengthUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setMedStrength(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        editMedicationReoccurrenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        editMedicationRefillReminderTimeEdt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openTimePicker();
            }
        });

        editMedicationTreatmentDurationFromEdt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openDatePicker("from");

            }
        });

        editMedicationTreatmentDurationToEdt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openDatePicker("to");
            }
        });

        saveEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDataValid()){
                    presenter.getData();
                    presenter.updateMedication();
                    setWorkTimer();
                    startActivity(new Intent(EditMedActivity.this, BottomNavigationBar.class));
                    finish();
                }
            }
        });

    }

    private void setWorkTimer() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyPeriodicWorkManger.class,
                15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("Counter", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }

    private void setAdapters(){
        ArrayAdapter<String> medFormAdapter = new ArrayAdapter<>(EditMedActivity.this, android.R.layout.simple_spinner_dropdown_item, Utility.medForm);
        editMedicationFormSpinner.setAdapter(medFormAdapter);

        ArrayAdapter<String> medStrengthUnitAdapter = new ArrayAdapter<>(EditMedActivity.this, android.R.layout.simple_spinner_dropdown_item, medStrengthUnit);
        editMedicationStrengthUnitSpinner.setAdapter(medStrengthUnitAdapter);

        ArrayAdapter<String> medReoccurrenceAdapter = new ArrayAdapter<>(EditMedActivity.this, android.R.layout.simple_spinner_dropdown_item, medReminderPerDayList);
        editMedicationReoccurrenceSpinner.setAdapter(medReoccurrenceAdapter);

        ArrayAdapter<String> medReoccurrenceIntervalAdapter = new ArrayAdapter<>(EditMedActivity.this, android.R.layout.simple_spinner_dropdown_item, medReminderPerWeekList);
        editMedicationReoccurrenceIntervalSpinner.setAdapter(medReoccurrenceIntervalAdapter);
    }

    public void setStartDateTextView(String startDate){
        editMedicationTreatmentDurationFromEdt.getEditText().setText(startDate);
    }

    public void setEndDateTextView(String endDate){
        editMedicationTreatmentDurationToEdt.getEditText().setText(endDate);
    }

    public void setRefillTime(String time){
        editMedicationRefillReminderTimeEdt.getEditText().setText(time);
    }

    public List<MedicineDose> getDoseFromAdapter(){
        return adapter.getMyMeds();
    }
    public MedicationPojo getMedicationObject(){


        return (MedicationPojo) getIntent().getSerializableExtra("fromDetailsToEdit");////////////////////////////////////
    }

    public String getMedNameTextView(){
        return editMedicationNameEdt.getEditText().getText().toString();
    }

    public String getMedStrengthTextView(){
        return editMedicationStrengthUnitEdt.getEditText().getText().toString();
    }

    public String getMedReasonTextView(){
        return editMedicationReasonEdt.getEditText().getText().toString();
    }

    public String getMedInstructionTextView(){
        return editMedicationInstructionEdt.getEditText().getText().toString();
    }

    public int getMedQtyTextView(){
        return Integer.parseInt(editMedicationLeftEdt.getEditText().getText().toString());
    }

    @Override
    public String getStartDateTextView() {
        return editMedicationTreatmentDurationFromEdt.getEditText().getText().toString();
    }

    @Override
    public String getEndDateTextView() {
        return editMedicationTreatmentDurationToEdt.getEditText().getText().toString();
    }

    public int getMedReminderQtyTextView(){
        return Integer.parseInt(editMedicationRefillReminderNumOfItemsEdt.getEditText().getText().toString());
    }

    private boolean isDataValid(){
        if (editMedicationStrengthUnitEdt.getEditText().getText().toString().length()==0){
            editMedicationStrengthUnitEdt.requestFocus();
            editMedicationStrengthUnitEdt.setError("Required Field");
        }
        else if(editMedicationReasonEdt.getEditText().getText().toString().length()==0){
            editMedicationNameEdt.setError(null);
            editMedicationStrengthUnitEdt.setError(null);
            editMedicationReasonEdt.requestFocus();
            editMedicationReasonEdt.setError("Required Field");
        }
        else if(editMedicationInstructionEdt.getEditText().getText().toString().length()==0){
            editMedicationNameEdt.setError(null);
            editMedicationStrengthUnitEdt.setError(null);
            editMedicationReasonEdt.setError(null);
            editMedicationInstructionEdt.requestFocus();
            editMedicationInstructionEdt.setError("Required Field");
        }
        else if(editMedicationTreatmentDurationFromEdt.getEditText().getText().toString().length()==0){
            editMedicationNameEdt.setError(null);
            editMedicationStrengthUnitEdt.setError(null);
            editMedicationReasonEdt.setError(null);
            editMedicationInstructionEdt.setError(null);
            editMedicationTreatmentDurationFromEdt.requestFocus();
            editMedicationTreatmentDurationFromEdt.setError("Required Field");
        }
        else if(editMedicationTreatmentDurationToEdt.getEditText().getText().toString().length()==0){
            editMedicationNameEdt.setError(null);
            editMedicationStrengthUnitEdt.setError(null);
            editMedicationReasonEdt.setError(null);
            editMedicationInstructionEdt.setError(null);
            editMedicationTreatmentDurationFromEdt.setError(null);
            editMedicationTreatmentDurationToEdt.requestFocus();
            editMedicationTreatmentDurationToEdt.setError("Required Field");
        }
        else if(editMedicationLeftEdt.getEditText().getText().toString().length()==0){
            editMedicationNameEdt.setError(null);
            editMedicationStrengthUnitEdt.setError(null);
            editMedicationReasonEdt.setError(null);
            editMedicationInstructionEdt.setError(null);
            editMedicationTreatmentDurationFromEdt.setError(null);
            editMedicationTreatmentDurationToEdt.setError(null);
            editMedicationLeftEdt.requestFocus();
            editMedicationLeftEdt.setError("Required Field");
        }
        else if(getMedQtyTextView()<=0){
            editMedicationNameEdt.setError(null);
            editMedicationStrengthUnitEdt.setError(null);
            editMedicationReasonEdt.setError(null);
            editMedicationInstructionEdt.setError(null);
            editMedicationTreatmentDurationFromEdt.setError(null);
            editMedicationTreatmentDurationToEdt.setError(null);
            editMedicationLeftEdt.requestFocus();
            editMedicationLeftEdt.setError("Invalid Value");
        }
        else if(editMedicationRefillReminderNumOfItemsEdt.getEditText().getText().toString().length()==0){
            editMedicationNameEdt.setError(null);
            editMedicationStrengthUnitEdt.setError(null);
            editMedicationReasonEdt.setError(null);
            editMedicationInstructionEdt.setError(null);
            editMedicationTreatmentDurationFromEdt.setError(null);
            editMedicationTreatmentDurationToEdt.setError(null);
            editMedicationLeftEdt.setError(null);
            editMedicationRefillReminderNumOfItemsEdt.requestFocus();
            editMedicationRefillReminderNumOfItemsEdt.setError("Required Field");
        }
        else if(getMedReminderQtyTextView()<=0){
            editMedicationNameEdt.setError(null);
            editMedicationStrengthUnitEdt.setError(null);
            editMedicationReasonEdt.setError(null);
            editMedicationInstructionEdt.setError(null);
            editMedicationTreatmentDurationFromEdt.setError(null);
            editMedicationTreatmentDurationToEdt.setError(null);
            editMedicationLeftEdt.setError(null);
            editMedicationRefillReminderNumOfItemsEdt.requestFocus();
            editMedicationRefillReminderNumOfItemsEdt.setError("Invalid Value");
        }
        else if(getMedReminderQtyTextView()>=getMedQtyTextView()){
            editMedicationNameEdt.setError(null);
            editMedicationStrengthUnitEdt.setError(null);
            editMedicationReasonEdt.setError(null);
            editMedicationInstructionEdt.setError(null);
            editMedicationTreatmentDurationFromEdt.setError(null);
            editMedicationTreatmentDurationToEdt.setError(null);
            editMedicationLeftEdt.setError(null);
            editMedicationRefillReminderNumOfItemsEdt.requestFocus();
            editMedicationRefillReminderNumOfItemsEdt.setError("Invalid Value");
        }
        else if(editMedicationRefillReminderTimeEdt.getEditText().getText().toString().length()==0){
            editMedicationNameEdt.setError(null);
            editMedicationStrengthUnitEdt.setError(null);
            editMedicationReasonEdt.setError(null);
            editMedicationInstructionEdt.setError(null);
            editMedicationTreatmentDurationFromEdt.setError(null);
            editMedicationTreatmentDurationToEdt.setError(null);
            editMedicationLeftEdt.setError(null);
            editMedicationRefillReminderNumOfItemsEdt.setError(null);
            editMedicationRefillReminderTimeEdt.requestFocus();
            editMedicationRefillReminderTimeEdt.setError("Required Field");
        }
        else {
            return true;
        }

        return false;
    }
    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        editMedicationNameEdt = (TextInputLayout) findViewById(R.id.edit_medication_name_edt);
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