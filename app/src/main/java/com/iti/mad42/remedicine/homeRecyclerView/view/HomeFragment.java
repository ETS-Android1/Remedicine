package com.iti.mad42.remedicine.homeRecyclerView.view;

import static android.content.Context.MODE_PRIVATE;

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
import com.iti.mad42.remedicine.Model.database.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.Model.pojo.CurrentUser;
import com.iti.mad42.remedicine.Model.pojo.HomeParentItem;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.MedicineDose;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.data.FacebookAuthentication.RemoteDataSource;
import com.iti.mad42.remedicine.homeRecyclerView.presenter.HomePresenter;
import com.iti.mad42.remedicine.homeRecyclerView.presenter.HomePresenterInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment implements HomeFragmentInterface, OnNodeListener{

    private View view;
    private DayScrollDatePicker dayScrollDatePicker;
    private RecyclerView ParentRecyclerViewItem;
    private FloatingActionButton btnAddMedicine;
    private HomePresenterInterface presenter;
    private HomeParentItemAdapter parentItemAdapter;
    private LinearLayoutManager layoutManager;
    public List<MedicationPojo> medicines;
    private List<HomeParentItem> homeParentItemList = new ArrayList<>();
    private Dialog medicationAction;
    private ImageButton btnClose, btnInfo, btnTakeMedicine;
    private TextView medicationScheduleTV, pillStrengthTV, leftPillsTV, lastTakenTV, medicineNameDialogTV;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HomePresenter(getContext(),this, Repository.getInstance(getContext(), ConcreteLocalDataSource.getInstance(getContext()), RemoteDataSource.getInstance(getContext(), CallbackManager.Factory.create())));
        getSharedPref();
        presenter.setCurrentUser();
        Log.i("Current user email", CurrentUser.getInstance().getEmail() +"");
        presenter.getAlMedicines();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

    }


    private void initScrollDatePicker() {

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        dayScrollDatePicker = view.findViewById(R.id.dayDatePicker);
        dayScrollDatePicker.setStartDate(12,4,2022);
        dayScrollDatePicker.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Toast.makeText(getContext(),dateFormat.format(date),Toast.LENGTH_SHORT).show();
                filterMedicationByDay(medicines, dateFormat.format(date));
            }
        });

    }



    private void getSharedPref() {
        SharedPreferences prefs = getContext().getSharedPreferences("LoginTest", MODE_PRIVATE);
        String name = prefs.getString(Utility.myCredentials, "No user registered");//"No name defined" is the default value.
        Log.i("SharedPrefs", "onCreate: " + name);
    }

    @Override
    public void showData(LiveData<List<MedicationPojo>> medicines) {
        medicines.observe(this, new Observer<List<MedicationPojo>>() {
            @Override
            public void onChanged(List<MedicationPojo> medicationPojos) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    for (MedicationPojo m : medicationPojos){
                        System.out.println(m);
                    }
                    HomeFragment.this.medicines = medicationPojos;
                    filterMedicationByDay(medicationPojos,Utility.getCurrentDay());
                }
            }
        });
    }


    public void filterMedicationByDay(List<MedicationPojo> medicationList, String date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<MedicationPojo> medicinesPerDay = medicationList.stream().filter(medicine -> medicine.getMedDays().contains(date.trim())).collect(Collectors.toList());
            for (MedicationPojo m : medicinesPerDay) {
                System.out.println(m);
            }
            buildArrayOfTimes(medicinesPerDay);
        }
    }

    public void buildArrayOfTimes(List<MedicationPojo> medicines) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            List<Long> arrayOdTimes = new ArrayList<Long>();
            List<Long> distinctArrayOfTimes;
            for (MedicationPojo m : medicines) {
                for (MedicineDose medicineDose : m.getMedDoseReminders()) {
                    arrayOdTimes.add(medicineDose.getDoseTimeInMilliSec());
                }
            }
            distinctArrayOfTimes = arrayOdTimes.stream().distinct().collect(Collectors.toList());
            Collections.sort(distinctArrayOfTimes);
            for (Long m : distinctArrayOfTimes) {
                System.out.println(Utility.millisToTimeAsString(m) + "<<<<<<<<<<<<<<<<<<<<<");
            }
            matchMedicineToTime(medicines,distinctArrayOfTimes);
        }
    }

    public void matchMedicineToTime(List<MedicationPojo> medicines, List<Long> times) {
        ArrayList<ArrayList<MedicationPojo>> listsOfMedicines = new ArrayList<ArrayList<MedicationPojo>>();
        for (Long time : times) {
            ArrayList<MedicationPojo> list = new ArrayList<MedicationPojo>();
            for (MedicationPojo m : medicines) {
                for (MedicineDose md : m.getMedDoseReminders()) {
                    if (md.getDoseTimeInMilliSec() == time) {
                        list.add(m);
                    }
                }
            }
            listsOfMedicines.add(list);
        }
        printLists(listsOfMedicines);
        ParentItemList(listsOfMedicines,times);
    }

    public void printLists(ArrayList<ArrayList<MedicationPojo>> lists) {
        for (ArrayList<MedicationPojo> l : lists) {
            for (MedicationPojo m : l) {
                System.out.println(m);
            }
            System.out.println("\n");
        }
    }

    private void ParentItemList(ArrayList<ArrayList<MedicationPojo>> lists, List<Long> times ) {

        List<HomeParentItem> itemList = new ArrayList<>();
        for (int x = 0; x < times.size(); x++) {
            HomeParentItem item = new HomeParentItem(Utility.millisToTimeAsString(times.get(x)), lists.get(x));
            itemList.add(item);
        }
        homeParentItemList = itemList;
        parentItemAdapter = new HomeParentItemAdapter(getActivity(),itemList, this);
        ParentRecyclerViewItem.setAdapter(parentItemAdapter);
        parentItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void getChosenMedicine(MedicationPojo medicine) {
        System.out.println(medicine);
        initDialogUI();
        setDialogListeners();
        medicineNameDialogTV.setText(medicine.getName().trim());



    }

    private void initDialogUI() {
        medicationAction.setContentView(R.layout.dialog_medicine_info);
        medicationAction.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        medicineNameDialogTV = medicationAction.findViewById(R.id.txtViewMedicineNameDialog);
        medicationScheduleTV = medicationAction.findViewById(R.id.txtViewMedicineSchedule);
        pillStrengthTV = medicationAction.findViewById(R.id.txtViewPillStrengthDialog);
        leftPillsTV = medicationAction.findViewById(R.id.txtViewLeftPills);
        lastTakenTV = medicationAction.findViewById(R.id.txtViewLastTaken);
        btnClose = medicationAction.findViewById(R.id.btnCloseDialog);
        btnInfo = medicationAction.findViewById(R.id.btnMedicineInfoInDialog);
        btnTakeMedicine = medicationAction.findViewById(R.id.btnTakeMedicine);
        medicationAction.show();
    }

    private void setDialogListeners() {

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medicationAction.dismiss();
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnTakeMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}