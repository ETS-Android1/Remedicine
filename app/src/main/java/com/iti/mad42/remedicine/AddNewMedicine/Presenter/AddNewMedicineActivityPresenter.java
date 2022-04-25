package com.iti.mad42.remedicine.AddNewMedicine.Presenter;

import static android.content.Context.MODE_PRIVATE;
import static com.iti.mad42.remedicine.Model.pojo.Utility.dateToLong;
import static com.iti.mad42.remedicine.Model.pojo.Utility.timeToMillis;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TimePicker;

import com.iti.mad42.remedicine.AddNewMedicine.View.AddNewMedicineActivityInterface;
import com.iti.mad42.remedicine.Model.pojo.MedState;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.MedicineDose;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.Model.pojo.RepositoryInterface;
import com.iti.mad42.remedicine.Model.pojo.Utility;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddNewMedicineActivityPresenter implements AddNewMedicineActivityPresenterInterface {
    int refillHour = 0 , refillMinute =0,interval=1;
    String startDate,endDate;
    List<MedState> medStates = new ArrayList<>();
    String form="";
    List<MedicineDose> medDose = new ArrayList<>();
    MedicationPojo med = new MedicationPojo();
    Context context;
    AddNewMedicineActivityInterface view;
    private RepositoryInterface repository;


    public AddNewMedicineActivityPresenter(Context context, AddNewMedicineActivityInterface view, RepositoryInterface repository) {
        this.context = context;
        this.view = view;
        this.repository = repository;
    }
    public void setMedicineForm(int pos){
        form = Utility.medForm[pos];
        med.setFormIndex(pos);
    }
    public void setInterval(int pos){
        med.setRecurrencePerWeekIndex(pos);
        switch (pos){
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
    public void setMedStrength(int pos){
        med.setStrengthUnitIndex(pos);
    }
    public List<MedicineDose> getMedDose(int pos){
        medDose = new ArrayList<>();
        med.setRecurrencePerDayIndex(pos);
        switch (pos){
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
        return medDose;
    }

    public void openDatePicker(String state){
        final Calendar c = Calendar.getInstance();
        long now = System.currentTimeMillis() - 1000;

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (viewDatePicker, year, monthOfYear, dayOfMonth) -> {

                    if (state.equals("from")){
                        startDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        med.setStartDate(dateToLong(startDate));
                        view.setStartDateTextView(startDate);
                    }
                    else{
                        endDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        med.setEndDate(dateToLong(endDate));
                        view.setEndDateTextView(endDate);
                    }

                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        if(state.equals("from")){
            datePickerDialog.getDatePicker().setMinDate(now - (1000 * 60 * 60 * 24 * 14));
        } else{
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }
        datePickerDialog.show();
    }


    public void openTimePicker() {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker viewTimePicker, int hour, int minute) {
                refillHour = hour;
                refillMinute = minute;
                med.setRefillReminderTimeInMilliSec(timeToMillis(hour,minute));
                view.setRefillTime(refillHour+":"+refillMinute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                , true);
        timePickerDialog.show();
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
            //Log.e("mando", "printDays: "+date );
        }
        return myDays;
    }

    public void getData(){
        med.setName(view.getMedNameTextView());
        med.setStrength(view.getMedStrengthTextView());
        med.setReason(view.getMedReasonTextView());
        med.setInstructions(view.getMedInstructionTextView());
        med.setMedQty(view.getMedQtyTextView());
        med.setReminderMedQtyLeft(view.getMedReminderQtyTextView());
        medDose =view.getDoseFromAdapter();
        med.setMedDoseReminders(medDose);
        med.setMedDays(getDays(startDate,endDate,interval));
        for (int i = 0; i<medDose.size();i++){
            medStates.add(new MedState(medDose.get(i).getDoseTimeInMilliSec(),"non"));
        }
        med.setMedState(medStates);
        med.setActive(true);
        med.setMedOwnerEmail(getString(Utility.myCredentials));

    }
    @Override
    public void insertMedication() {
        Log.i("mando", "onClick: "+ med );
        repository.insertMedication(med);
        repository.addMedicationToFirebase(med);
    }
    public String getString(String key){
        SharedPreferences sharedPreferences=
                context.getSharedPreferences("LoginTest",MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }

}
