package com.iti.mad42.remedicine.ShowMedications.Presenter;

import androidx.lifecycle.LiveData;

import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.RepositoryInterface;
import com.iti.mad42.remedicine.ShowMedications.View.ShowMedicationFragmentInterface;

import java.util.List;

public class ShowMedicationsPresenter implements ShowMedicationsPresenterInterface {
    private ShowMedicationFragmentInterface view;
    private RepositoryInterface repository;

    public ShowMedicationsPresenter(ShowMedicationFragmentInterface view, RepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public LiveData<List<MedicationPojo>> getActiveMedications(long currentDate) {
         return repository.getActiveMedications(currentDate);
    }

    @Override
    public LiveData<List<MedicationPojo>> getInActiveMedications(long currentDate) {
        return repository.getInActiveMedication(currentDate);
    }
}
