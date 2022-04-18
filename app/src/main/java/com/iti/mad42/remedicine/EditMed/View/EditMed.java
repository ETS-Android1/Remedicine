package com.iti.mad42.remedicine.EditMed.View;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.iti.mad42.remedicine.Model.MedicineDose;
import com.iti.mad42.remedicine.Model.Utility;
import com.iti.mad42.remedicine.R;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    int refillHour = 0 , refillMinute =0;
    String startDate;
    String endDate;

    List<MedicineDose> medDose = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    EditRemindersRecyclerAdapter adapter;

    private String form = "";
    private String strength = "";
    private String recurrencePerDay = "";
    private String recurrencePerWeek = "";
    int interval=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_med);
        initView();
        editMedTimeRecyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        editMedTimeRecyclerview.setLayoutManager(layoutManager);

        ArrayAdapter<String> medFormAdapter = new ArrayAdapter<>(EditMed.this, android.R.layout.simple_spinner_dropdown_item, Utility.medForm);
        editMedicationFormSpinner.setAdapter(medFormAdapter);
        editMedicationFormSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                form = Utility.medForm[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> medStrengthUnitAdapter = new ArrayAdapter<>(EditMed.this, android.R.layout.simple_spinner_dropdown_item, Utility.medStrengthUnit);
        editMedicationStrengthUnitSpinner.setAdapter(medStrengthUnitAdapter);
        editMedicationStrengthUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedTime = times.get(position);
//                selectedTimeIndex = position;
                strength =Utility.medStrengthUnit[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> medReoccurrenceAdapter = new ArrayAdapter<>(EditMed.this, android.R.layout.simple_spinner_dropdown_item, Utility.medReminderPerDayList);
        editMedicationReoccurrenceSpinner.setAdapter(medReoccurrenceAdapter);
        editMedicationReoccurrenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedTime = times.get(position);
//                selectedTimeIndex = position;
                medDose = new ArrayList<>();
                recurrencePerDay = Utility.medReminderPerDayList[position];
                switch (position){
                    case 0  :
                        medDose.add(new MedicineDose(form,1, 1650153600000L));
                        break;

                    case 1  :
                        medDose.add(new MedicineDose(form,1, 1650153600000L));
                        medDose.add(new MedicineDose(form,1, 1650153600000L));

                        break;
                    case 2  :
                        medDose.add(new MedicineDose(form,1, 1650153600000L));

                        medDose.add(new MedicineDose(form,1, 1650153600000L));

                        medDose.add(new MedicineDose(form,1, 1650153600000L));

                        break;
                }
                adapter = new EditRemindersRecyclerAdapter(view.getContext(), medDose);
                editMedTimeRecyclerview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> medReoccurrenceIntervalAdapter = new ArrayAdapter<>(EditMed.this, android.R.layout.simple_spinner_dropdown_item, Utility.medReminderPerWeekList);
        editMedicationReoccurrenceIntervalSpinner.setAdapter(medReoccurrenceIntervalAdapter);
        editMedicationReoccurrenceIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedTime = times.get(position);
//                selectedTimeIndex = position;
                recurrencePerWeek = Utility.medReminderPerWeekList[position];
                switch (position){
                    case 0  :
                        interval = 1;
                        break;
                    case 1  :
                       interval =2;
                        break;
                    case 2  :
                        interval = 3;
                        break;
                    case 3  :
                        interval = 7;
                        break;
                    case 4  :
                        interval = 30;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        editMedicationRefillReminderTimeEdt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker(view);
            }
        });

        editMedicationTreatmentDurationFromEdt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker("from");
            }
        });
        editMedicationTreatmentDurationToEdt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker("to");
            }
        });
        saveEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // every day =1 , every 2days =2 , 3days = 3 ,every week = 7 ,ever month 30
                printDays(startDate,endDate,interval);
            }
        });

    }

    public void openDatePicker(String state){
        final Calendar c = Calendar.getInstance();
        long now = System.currentTimeMillis() - 1000;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        if (state.equals("from")){
                            startDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            Log.e("mando", "onDateSet: "+dateToLong(startDate));
                            Log.e("mando","date baaaack "+longToDateAsString(dateToLong(startDate)));
                            editMedicationTreatmentDurationFromEdt.getEditText().setText(startDate);
                        }
                        else{
                            endDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            editMedicationTreatmentDurationToEdt.getEditText().setText(endDate);
                        }

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(now + (1000 * 60 * 60));

        datePickerDialog.show();
    }
    public String longToDateAsString(long dateInMillis){

        Date d = new Date(dateInMillis);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(d);
    }
    public long dateToLong(String date){
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        long milliseconds = 0;
        try {
            Date d = f.parse(date);
             milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }
    public void openTimePicker(View view) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                refillHour = hour;
                refillMinute = minute;
                editMedicationRefillReminderTimeEdt.getEditText().setText(refillHour+":"+refillMinute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                , true);
        timePickerDialog.show();
        //flag=true;
    }

    public void printDays(String startDate, String endDate , int x){

        final DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy");
        final LocalDate start = dtf.parseLocalDate(startDate);
        final LocalDate end = dtf.parseLocalDate(endDate).plusDays(1);

        long days =Days.daysBetween(new LocalDate(start), new LocalDate(end)).getDays();

        for ( int i = 0; i < days; i+= x) {
            LocalDate current = start.plusDays(i);
            String date = current.toDateTimeAtStartOfDay().toString("dd-MM-yyyy");
            Log.e("mando", "printDays: "+date );
        }


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