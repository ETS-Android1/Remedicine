package com.iti.mad42.remedicine.AddNewMedicine.View;

import static com.iti.mad42.remedicine.Model.Utility.medReminderPerDayList;
import static com.iti.mad42.remedicine.Model.Utility.medReminderPerWeekList;
import static com.iti.mad42.remedicine.Model.Utility.medStrengthUnit;

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
import com.iti.mad42.remedicine.EditMed.View.EditRemindersRecyclerAdapter;
import com.iti.mad42.remedicine.Model.MedState;
import com.iti.mad42.remedicine.Model.MedicationPojo;
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

public class AddNewMedicineActivity extends AppCompatActivity {

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
    int refillHour = 0 , refillMinute =0;
    String startDate;
    String endDate;
    String medName = "",strength ="" ,reason ="",instruction ="",medQty ="",reminderMedQtyLeft ="";
    boolean isActive;
    List<MedState> medStates = new ArrayList<>();

    String form="";
    List<MedicineDose> medDose = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    EditRemindersRecyclerAdapter adapter;
    MedicationPojo med = new MedicationPojo();
    int interval=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_medicine);
        initView();

        editMedTimeRecyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        editMedTimeRecyclerview.setLayoutManager(layoutManager);

        ArrayAdapter<String> medFormAdapter = new ArrayAdapter<>(AddNewMedicineActivity.this, android.R.layout.simple_spinner_dropdown_item, Utility.medForm);
        medicationFormSpinner.setAdapter(medFormAdapter);
        medicationFormSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedTime = times.get(position);
//                selectedTimeIndex = position;
                form = Utility.medForm[position];
                med.setFormIndex(position);
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
                med.setStrengthUnitIndex(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> medReoccurrenceAdapter = new ArrayAdapter<>(AddNewMedicineActivity.this, android.R.layout.simple_spinner_dropdown_item, medReminderPerDayList);
        medicationReoccurrenceSpinner.setAdapter(medReoccurrenceAdapter);
        medicationReoccurrenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedTime = times.get(position);
//                selectedTimeIndex = position;
                medDose = new ArrayList<>();
                med.setRecurrencePerDayIndex(position);

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
        ArrayAdapter<String> medReoccurrenceIntervalAdapter = new ArrayAdapter<>(AddNewMedicineActivity.this, android.R.layout.simple_spinner_dropdown_item, medReminderPerWeekList);
        medicationReoccurrenceIntervalSpinner.setAdapter(medReoccurrenceIntervalAdapter);
        medicationReoccurrenceIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedTime = times.get(position);
//                selectedTimeIndex = position;
                med.setRecurrencePerWeekIndex(position);
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
        medicationRefillReminderTimeEdt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker(view);
            }
        });

        medicationTreatmentDurationFromEdt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker("from");
            }
        });
        medicationTreatmentDurationToEdt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker("to");
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getData();
                Log.i("mando", "onClick: "+ med );
                // every day =1 , every 2days =2 , 3days = 3 ,every week = 7 ,ever month 30

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
                            med.setStartDate(dateToLong(startDate));
                            medicationTreatmentDurationFromEdt.getEditText().setText(startDate);

                        }
                        else{
                            endDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            med.setEndDate(dateToLong(endDate));
                            medicationTreatmentDurationToEdt.getEditText().setText(endDate);
                        }

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(now + (1000 * 60 * 60));

        datePickerDialog.show();
    }

    public void getData(){
        med.setName(medicationNameEdt.getEditText().getText().toString());
        med.setStrength(medicationStrengthUnitEdt.getEditText().getText().toString());
        med.setReason(medicationReasonEdt.getEditText().getText().toString());
        med.setInstructions(medicationInstructionEdt.getEditText().getText().toString());
        medDose =adapter.getMyMeds();
        med.setMedDoseReminders(medDose);
        med.setMedDays(getDays(startDate,endDate,interval));
        med.setMedQty(Integer.parseInt(medicationLeftEdt.getEditText().getText().toString()));
        med.setReminderMedQtyLeft(Integer.parseInt(medicationRefillReminderNumOfItemsEdt.getEditText().getText().toString()));
        for (int i = 0; i<medDose.size();i++){
            medStates.add(new MedState(medDose.get(i).getDoseTimeInMilliSec(),"non"));
        }
        med.setMedState(medStates);
        med.setActive(true);
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
                med.setRefillReminderTimeInMilliSec(timeToMillis(hour,minute));
                //Log.e("mando", "onTimeSet: "+timeToMillis(hour,minute) +"|||"+millisToTimeAsString(timeToMillis(hour,minute)));
                medicationRefillReminderTimeEdt.getEditText().setText(refillHour+":"+refillMinute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                , true);
        timePickerDialog.show();
        //flag=true;
    }


    public List<String> getDays(String startDate, String endDate , int x){

        final DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy");
        final LocalDate start = dtf.parseLocalDate(startDate);
        final LocalDate end = dtf.parseLocalDate(endDate).plusDays(1);

        List<String> myDays = new ArrayList<>();
        long days = Days.daysBetween(new LocalDate(start), new LocalDate(end)).getDays();

        for ( int i = 0; i < days; i+= x) {
            LocalDate current = start.plusDays(i);
            String date = current.toDateTimeAtStartOfDay().toString("dd-MM-yyyy");
            myDays.add(date);
            Log.e("mando", "printDays: "+date );
        }
        return myDays;


    }
    public long timeToMillis(int hour , int min){
        return ((hour*60)+min) * 60 * 1000;
    }

    public String millisToTimeAsString(long timeInMillis){
        int minutes = (int) ((timeInMillis / (1000*60)) % 60);
        int hours   = (int) ((timeInMillis / (1000*60*60)) % 24);
        return (hours+":"+minutes);
    }
    public String longToDateAsString(long dateInMillis){

        Date d = new Date(dateInMillis);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(d);
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