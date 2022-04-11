package com.iti.mad42.remedicine.ShowMedications.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iti.mad42.remedicine.Model.Medication;
import com.iti.mad42.remedicine.R;

import java.util.ArrayList;
import java.util.List;

public class ShowMedicationsFragment extends Fragment {

    RecyclerView activeMedsRecycler;
    RecyclerView inActiveMedsRecycler;
    ActiveMedicationsAdapter activeAdapter, inactiveAdapter;
    List<Medication> myMeds = new ArrayList<>();

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
        return view;
    }
}