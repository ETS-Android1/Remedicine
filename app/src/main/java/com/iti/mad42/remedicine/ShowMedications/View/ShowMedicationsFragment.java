package com.iti.mad42.remedicine.ShowMedications.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.core.Repo;
import com.iti.mad42.remedicine.AddNewMedicine.View.AddNewMedicineActivity;
import com.iti.mad42.remedicine.MedDetails.View.MedDetails;
import com.iti.mad42.remedicine.Model.database.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.Model.pojo.MedState;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.MedicineDose;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.R;

import java.util.ArrayList;
import java.util.List;

public class ShowMedicationsFragment extends Fragment implements ShowMedicationFragmentInterface{

    RecyclerView activeMedsRecycler;
    RecyclerView inActiveMedsRecycler;
    ActiveMedicationsAdapter activeAdapter, inactiveAdapter;
    FloatingActionButton addMedBtn;

    List<MedicationPojo> myMeds = new ArrayList<>();
    Repository repository;

    public ShowMedicationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = Repository.getInstance(getContext(), ConcreteLocalDataSource.getInstance(getContext()));
        repository.updateActiveStateForMedication(Utility.dateToLong(Utility.getCurrentDay()));
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
                Intent goToMedDetails = new Intent(getActivity(), MedDetails.class);
                goToMedDetails.putExtra("fromActiveToDetails", myMeds.get(position));
                startActivity(goToMedDetails);

            }
        });


        return view;
    }

    @Override
    public void showActiveMedications(LiveData<List<MedicationPojo>> meds) {
        meds.observe(this, new Observer<List<MedicationPojo>>() {
            @Override
            public void onChanged(List<MedicationPojo> medicationPojos) {
                activeAdapter = new ActiveMedicationsAdapter(getContext(), medicationPojos);
                activeAdapter.notifyDataSetChanged();
            }
        });

    }
}