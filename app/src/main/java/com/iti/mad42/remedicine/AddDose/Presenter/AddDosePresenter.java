package com.iti.mad42.remedicine.AddDose.Presenter;

import com.iti.mad42.remedicine.AddDose.View.AddDoseActivityInterface;
import com.iti.mad42.remedicine.Model.pojo.RepositoryInterface;

public class AddDosePresenter implements AddDosePresenterInterface {
    AddDoseActivityInterface view;
    private RepositoryInterface repository;

    public AddDosePresenter(AddDoseActivityInterface view, RepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }


}
