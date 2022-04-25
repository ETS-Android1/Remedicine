package com.iti.mad42.remedicine.ShowMedications.View;

import android.content.Intent;
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
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.mad42.remedicine.AddNewMedicine.View.AddNewMedicineActivity;
import com.iti.mad42.remedicine.Broadcast.NetworkChangeReceiver;
import com.iti.mad42.remedicine.MedDetails.View.MedDetails;
import com.iti.mad42.remedicine.Model.database.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.Model.pojo.CurrentUser;
import com.iti.mad42.remedicine.Model.pojo.MedState;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.MedicineDose;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.ShowMedications.Presenter.ShowMedicationsPresenter;
import com.iti.mad42.remedicine.ShowMedications.Presenter.ShowMedicationsPresenterInterface;
import com.iti.mad42.remedicine.data.FacebookAuthentication.RemoteDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShowMedicationsFragment extends Fragment implements ShowMedicationFragmentInterface, OnItemClickListenerInterface{

    RecyclerView activeMedsRecycler;
    RecyclerView inActiveMedsRecycler;
    ActiveMedicationsAdapter activeAdapter, inactiveAdapter;
    ShowMedicationsPresenterInterface presenter;
    FloatingActionButton addMedBtn;
    Repository repository;
    LinearLayoutManager activeLayoutManager;

    public ShowMedicationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_medications, container, false);

        initUI(view);
        setPresenter();
        addMedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewMedicineActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setActiveAdapter();
//        setInactiveAdapter();
        if (presenter.getSharedPref().equals(CurrentUser.getInstance().getEmail())) {
            if (NetworkChangeReceiver.isConnected){
                setActiveAdapter();
                setInactiveAdapter();
                addMedBtn.setEnabled(true);
            }else {
                setActiveAdapter();
                setInactiveAdapter();
                addMedBtn.setEnabled(false);
            }
        }else {
            if (NetworkChangeReceiver.isConnected){
                Log.e("mando", "onViewCreated: " +CurrentUser.getInstance().getEmail().trim());

                presenter.getOnlineData(CurrentUser.getInstance().getEmail().trim());
                addMedBtn.setEnabled(false);
            }else {
                addMedBtn.setEnabled(false);
                Toast.makeText(getContext(),"Please check your network connection",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initUI(View view){
        addMedBtn = view.findViewById(R.id.btnAddMedicineHome);
        activeMedsRecycler = view.findViewById(R.id.activeMedsRecycler);
        inActiveMedsRecycler = view.findViewById(R.id.inactiveMedsRecycler);

    }
    private void setPresenter(){
        repository = Repository.getInstance(getContext(), ConcreteLocalDataSource.getInstance(getContext()), RemoteDataSource.getInstance(getContext(), new CallbackManager() {
            @Override
            public boolean onActivityResult(int i, int i1, @Nullable Intent intent) {
                return false;
            }
        }));
        repository.updateActiveStateForMedication(Utility.dateToLong(Utility.getCurrentDay()));
        presenter = new ShowMedicationsPresenter(getContext(),this,repository);

    }

    private void setActiveAdapter(){
        activeLayoutManager = new LinearLayoutManager(getContext());
        activeAdapter = new ActiveMedicationsAdapter(getContext(), new ArrayList<>());
        activeAdapter.setOnItemClickListener(this);
        activeMedsRecycler.setHasFixedSize(true);
        activeLayoutManager.setOrientation(RecyclerView.VERTICAL);
        activeMedsRecycler.setLayoutManager(activeLayoutManager);
        activeMedsRecycler.setAdapter(activeAdapter);
        presenter.getActiveMedications(Utility.dateToLong(Utility.getCurrentDay())).observe(getViewLifecycleOwner(), new Observer<List<MedicationPojo>>() {
            @Override
            public void onChanged(List<MedicationPojo> medicationPojos) {
                showActiveMedications(medicationPojos);
                activeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setInactiveAdapter(){
        LinearLayoutManager inactiveLayoutManager = new LinearLayoutManager(getContext());
        inactiveAdapter = new ActiveMedicationsAdapter(getContext(), new ArrayList<>());
        inactiveAdapter.setOnItemClickListener(this);
        inActiveMedsRecycler.setHasFixedSize(true);
        inactiveLayoutManager.setOrientation(RecyclerView.VERTICAL);
        inActiveMedsRecycler.setLayoutManager(inactiveLayoutManager);
        inActiveMedsRecycler.setAdapter(inactiveAdapter);
        presenter.getInActiveMedications(Utility.dateToLong(Utility.getCurrentDay())).observe(getViewLifecycleOwner(), new Observer<List<MedicationPojo>>() {
            @Override
            public void onChanged(List<MedicationPojo> medicationPojos) {
                showInActiveMedications(medicationPojos);
                inactiveAdapter.notifyDataSetChanged();
            }
        });
    }

    private void gotToMedicationDetails(MedicationPojo medDetails){
        Intent goToMedDetails = new Intent(getActivity(), MedDetails.class);
        goToMedDetails.putExtra("fromActiveToDetails", medDetails);
        startActivity(goToMedDetails);
    }

    @Override
    public void showActiveMedications(List<MedicationPojo> meds) {
        activeAdapter.setList(meds);
        activeAdapter.notifyDataSetChanged();
    }

    @Override
    public void showInActiveMedications(List<MedicationPojo> meds) {
        inactiveAdapter.setList(meds);
        inactiveAdapter.notifyDataSetChanged();
    }

    @Override
    public void getOnlineData(List<MedicationPojo> friendMedications) {
        activeLayoutManager = new LinearLayoutManager(getContext());
        activeAdapter = new ActiveMedicationsAdapter(getContext(), new ArrayList<>());
        activeAdapter.setOnItemClickListener(this);
        activeMedsRecycler.setHasFixedSize(true);
        activeLayoutManager.setOrientation(RecyclerView.VERTICAL);
        activeMedsRecycler.setLayoutManager(activeLayoutManager);
        activeMedsRecycler.setAdapter(activeAdapter);
        Log.e("mando", "getOnlineData:medd " + friendMedications.size());
        showActiveMedications(friendMedications);

//        presenter.getActiveMedications(Utility.dateToLong(Utility.getCurrentDay())).observe(getViewLifecycleOwner(), new Observer<List<MedicationPojo>>() {
//            @Override
//            public void onChanged(List<MedicationPojo> medicationPojos) {
//                showActiveMedications(medicationPojos);
//                inactiveAdapter.notifyDataSetChanged();
//            }
//        });
    }

    @Override
    public void onItemClick(MedicationPojo med) {
        gotToMedicationDetails(med);
    }
}