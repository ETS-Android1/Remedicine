package com.iti.mad42.remedicine.homeRecyclerView.view;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;
import com.iti.mad42.remedicine.AddNewMedicine.View.AddNewMedicineActivity;
import com.iti.mad42.remedicine.Broadcast.NetworkChangeReceiver;
import com.iti.mad42.remedicine.MedDetails.View.MedDetails;
import com.iti.mad42.remedicine.Model.database.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.Model.pojo.CurrentUser;
import com.iti.mad42.remedicine.Model.pojo.HomeParentItem;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.MedicineDose;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.data.FacebookAuthentication.NetworkDelegate;
import com.iti.mad42.remedicine.data.FacebookAuthentication.RemoteDataSource;
import com.iti.mad42.remedicine.homeRecyclerView.presenter.HomePresenter;
import com.iti.mad42.remedicine.homeRecyclerView.presenter.HomePresenterInterface;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class HomeFragment extends Fragment implements HomeFragmentInterface, OnNodeListener{

    private View view;
    private DayScrollDatePicker dayScrollDatePicker;
    private RecyclerView ParentRecyclerViewItem;
    private FloatingActionButton btnAddMedicine;
    private HomePresenterInterface presenter;
    private HomeParentItemAdapter parentItemAdapter;
    private LinearLayoutManager layoutManager;
    public List<MedicationPojo> medicines;
    private Dialog medicationAction;
    private ImageButton btnClose, btnInfo;
    private Button btnTakeMedicine;
    private TextView medicationScheduleTV, pillStrengthTV, leftPillsTV, lastTakenTV, medicineNameDialogTV;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HomePresenter(getContext(),this, Repository.getInstance(getContext(), ConcreteLocalDataSource.getInstance(getContext()), RemoteDataSource.getInstance(getContext(), CallbackManager.Factory.create())));
        presenter.getSharedPref();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_fragmenet,container,false);
        medicationAction = new Dialog(getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAddMedicine = view.findViewById(R.id.btnAddMedicineHome);
        btnAddMedicine.setOnClickListener(view1 -> {

            Intent intent = new Intent(getActivity(), AddNewMedicineActivity.class);
            startActivity(intent);
        });
        initScrollDatePicker();
        ParentRecyclerViewItem = view.findViewById(R.id.parent_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());

        ParentRecyclerViewItem.setLayoutManager(layoutManager);

        if (presenter.getSharedPref().equals(CurrentUser.getInstance().getEmail())) {
           if(NetworkChangeReceiver.isConnected){
               presenter.getOnlineData(CurrentUser.getInstance().getEmail().trim());
               btnAddMedicine.setEnabled(true);
           }
           else {
               presenter.getAlMedicines();
               btnAddMedicine.setEnabled(false);
           }
        }else {
            if (NetworkChangeReceiver.isConnected){
                presenter.getOnlineData(CurrentUser.getInstance().getEmail().trim());
                btnAddMedicine.setEnabled(false);
            }else {
                btnAddMedicine.setEnabled(false);
                Toast.makeText(getContext(),"Please check your network connection",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initScrollDatePicker() {

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(getActivity(), R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Date date1 = date.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                presenter.filterMedicationByDay(medicines, dateFormat.format(date1));
            }
        });
    }



    @Override
    public void showData(LiveData<List<MedicationPojo>> medicines) {
        medicines.observe(this, medicationPojos -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                for (MedicationPojo m : medicationPojos){
                    System.out.println(m);
                }
                HomeFragment.this.medicines = medicationPojos;
                presenter.filterMedicationByDay(medicationPojos,Utility.getCurrentDay());
            }
        });
    }

    public void setParentItemAdapter(List<HomeParentItem> itemList, String date) {
        parentItemAdapter = new HomeParentItemAdapter(getActivity(),itemList, this, date);
        ParentRecyclerViewItem.setAdapter(parentItemAdapter);
        parentItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void getOnlineData(List<MedicationPojo> friendMedications) {
        Log.e("mando", "getOnlineData:9999 "+friendMedications.size() );
        HomeFragment.this.medicines = friendMedications;
        presenter.filterMedicationByDay(friendMedications,Utility.getCurrentDay());
    }

    @Override
    public void getChosenMedicine(MedicationPojo medicine, Long time) {
        System.out.println(medicine);
        initDialogUI();
        setDialogListeners(medicine, time);
        medicineNameDialogTV.setText(medicine.getName().trim());
        pillStrengthTV.setText(medicine.getStrength()+"g,take "+medicine.getMedDoseReminders().get(0).getMedDose()+" pill(s)");
        leftPillsTV.setText(medicine.getReminderMedQtyLeft()+" pill(s) left");
        medicationScheduleTV.setText("Scheduled for "+Utility.millisToTimeAsString(time)+", today");
    }

    private void initDialogUI() {
        medicationAction.setContentView(R.layout.dialog_medicine_info);
        medicationAction.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        medicineNameDialogTV = medicationAction.findViewById(R.id.txtViewMedicineNameDialog);
        medicationScheduleTV = medicationAction.findViewById(R.id.txtViewMedicineSchedule);
        pillStrengthTV = medicationAction.findViewById(R.id.txtViewPillStrengthDialog);
        leftPillsTV = medicationAction.findViewById(R.id.txtViewLeftPills);
        btnClose = medicationAction.findViewById(R.id.btnCloseDialog);
        btnInfo = medicationAction.findViewById(R.id.btnMedicineInfoInDialog);
        btnTakeMedicine = medicationAction.findViewById(R.id.btnTakeMedicine);
        medicationAction.show();
    }

    private void setDialogListeners(MedicationPojo medicine, Long time) {

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medicationAction.dismiss();
            }
        });

        btnInfo.setOnClickListener(view -> {
            Intent intent = new Intent(HomeFragment.this.getActivity(), MedDetails.class);
            intent.putExtra("fromActiveToDetails",(Serializable) medicine);
            startActivity(intent);
            ((Activity) getActivity()).overridePendingTransition(0, 0);
        });

        btnTakeMedicine.setOnClickListener(view -> {

            int medDoseIndex = 0;
            int medStatIndex = 0;
            for (int x = 0; x < medicine.getMedDoseReminders().size(); x++) {
                if (medicine.getMedDoseReminders().get(x).getDoseTimeInMilliSec() == time) {
                    medDoseIndex = x;
                    Log.i("TAG", "MedDose: "+ medicine.getMedDoseReminders().get(medDoseIndex).getMedDose());
                }
            }


            for (int x = 0; x < medicine.getMedState().size(); x++) {
                if (medicine.getMedState().get(x).getTime() == time) {
                    medStatIndex = x;
                    Log.i("TAG", "MedDose: "+ medicine.getMedState().get(medStatIndex).getState());
                }
            }

            int doseLeft = medicine.getMedQty() - medicine.getMedDoseReminders().get(medDoseIndex).getMedDose();
            medicine.setMedQty(doseLeft);
            medicine.getMedState().get(medStatIndex).setState("taken");
            updateMedication(medicine);
            medicationAction.dismiss();

        });
    }

    private void updateMedication(MedicationPojo medication) {
        presenter.updateMedication(medication);
    }
}