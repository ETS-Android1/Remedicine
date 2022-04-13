package com.iti.mad42.remedicine.ShowMedications.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.mad42.remedicine.AddDose.View.AddDose;
import com.iti.mad42.remedicine.AddNewMedicine.View.AddNewMedicineActivity;
import com.iti.mad42.remedicine.EditMed.View.EditMed;
import com.iti.mad42.remedicine.MedDetails.View.MedDetails;
import com.iti.mad42.remedicine.Model.Medication;
import com.iti.mad42.remedicine.R;

import java.util.ArrayList;
import java.util.List;

public class ShowMedicationsFragment extends Fragment {

    RecyclerView activeMedsRecycler;
    RecyclerView inActiveMedsRecycler;
    ActiveMedicationsAdapter activeAdapter, inactiveAdapter;
    List<Medication> myMeds = new ArrayList<>();
    FloatingActionButton addMedBtn;

    public ShowMedicationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myMeds.add(new Medication("Panadol", "500","g", "After Eating", "15"));
        myMeds.add(new Medication("Panadol", "500","g", "After Eating", "15"));
        myMeds.add(new Medication("Panadol", "500","g", "After Eating", "15"));
        myMeds.add(new Medication("Panadol", "500","g", "After Eating", "15"));
        myMeds.add(new Medication("Panadol", "500","g", "After Eating", "15"));
        myMeds.add(new Medication("Panadol", "500","g", "After Eating", "15"));
        myMeds.add(new Medication("Panadol", "500","g", "After Eating", "15"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_medications, container, false);

        addMedBtn = view.findViewById(R.id.btnAddMedicineHome);
        addMedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewMedicineActivity.class));

            }
        });

        activeMedsRecycler = view.findViewById(R.id.activeMedsRecycler);
        inActiveMedsRecycler = view.findViewById(R.id.inactiveMedsRecycler);

        activeMedsRecycler.setHasFixedSize(true);
        inActiveMedsRecycler.setHasFixedSize(true);

        activeAdapter = new ActiveMedicationsAdapter(getContext(), myMeds);
        inactiveAdapter = new ActiveMedicationsAdapter(getContext(), myMeds);

        LinearLayoutManager activeLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager inactiveLayoutManager = new LinearLayoutManager(getContext());

        activeLayoutManager.setOrientation(RecyclerView.VERTICAL);
        inactiveLayoutManager.setOrientation(RecyclerView.VERTICAL);

        activeMedsRecycler.setLayoutManager(activeLayoutManager);
        inActiveMedsRecycler.setLayoutManager(inactiveLayoutManager);

        inActiveMedsRecycler.setAdapter(inactiveAdapter);
        activeMedsRecycler.setAdapter(activeAdapter);

        activeAdapter.notifyDataSetChanged();
        inactiveAdapter.notifyDataSetChanged();
        activeAdapter.setOnItemClickListener(new ActiveMedicationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(getActivity(), MedDetails.class));

                Toast.makeText(getContext(),myMeds.get(position).getMedName()+" Pressed", Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }
}