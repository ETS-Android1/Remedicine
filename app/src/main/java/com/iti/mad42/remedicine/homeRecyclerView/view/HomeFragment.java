package com.iti.mad42.remedicine.homeRecyclerView.view;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;
import com.iti.mad42.remedicine.AddNewMedicine.View.AddNewMedicineActivity;
import com.iti.mad42.remedicine.Model.Utility;
import com.iti.mad42.remedicine.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private View view;
    DayScrollDatePicker dayScrollDatePicker;
    RecyclerView ParentRecyclerViewItem;
    FloatingActionButton btnAddMedicine;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragmenet.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SharedPreferences prefs = getContext().getSharedPreferences("LoginTest", MODE_PRIVATE);
        String name = prefs.getString(Utility.myCredentials, "No user registered");//"No name defined" is the default value.
        Log.i("SharedPrefs", "onCreate: " + name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_fragmenet,container,false);

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
        // Initialise the Linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // Pass the arguments
        // to the parentItemAdapter.
        // These arguments are passed
        // using a method ParentItemList()
        HomeParentItemAdapter parentItemAdapter = new HomeParentItemAdapter(getActivity(),ParentItemList());

        // Set the layout manager
        // and adapter for items
        // of the parent recyclerview
        ParentRecyclerViewItem
                .setAdapter(parentItemAdapter);
        ParentRecyclerViewItem
                .setLayoutManager(layoutManager);

    }


    private void initScrollDatePicker() {

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        dayScrollDatePicker = view.findViewById(R.id.dayDatePicker);
        dayScrollDatePicker.setStartDate(12,4,2022);
        dayScrollDatePicker.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {
                Toast.makeText(getContext(),date.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    private List<HomeParentItem> ParentItemList() {

        List<HomeParentItem> itemList = new ArrayList<>();

        HomeParentItem item = new HomeParentItem("10:00 AM", ChildItemList());
        itemList.add(item);
        HomeParentItem item1 = new HomeParentItem("01:00 PM", ChildItemList());
        itemList.add(item1);
        HomeParentItem item2 = new HomeParentItem("05:00 PM", ChildItemList());
        itemList.add(item2);
        HomeParentItem item3 = new HomeParentItem("11:00 PM", ChildItemList());
        itemList.add(item3);

        return itemList;
    }

    // Method to pass the arguments
    // for the elements
    // of child RecyclerView
    private List<HomeChildItem> ChildItemList() {

        List<HomeChildItem> ChildItemList = new ArrayList<>();

        ChildItemList.add(new HomeChildItem("Panadol","50g, Take 1 Pill(s) Before eating"));
        ChildItemList.add(new HomeChildItem("Panadol","50g, Take 1 Pill(s) Before eating"));
        ChildItemList.add(new HomeChildItem("Panadol","50g, Take 1 Pill(s) Before eating"));
        ChildItemList.add(new HomeChildItem("Panadol","50g, Take 1 Pill(s) Before eating"));

        return ChildItemList;
    }

}