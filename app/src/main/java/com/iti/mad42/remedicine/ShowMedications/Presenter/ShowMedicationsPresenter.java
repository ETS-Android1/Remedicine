package com.iti.mad42.remedicine.ShowMedications.Presenter;

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
    public void getActiveMedications(long currentDate) {
         repository.getActiveMedications(currentDate);
    }

    @Override
    public void getInActiveMedications(long currentDate) {
        repository.getInActiveMedication(currentDate);
    }
}
