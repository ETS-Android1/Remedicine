package com.iti.mad42.remedicine.MyAccount.View;

import androidx.lifecycle.LiveData;

import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.User;

import java.util.List;

public interface MyAccountFragmentInterface {
    void showAllUsers(List<User> users);
    void sendAllMedsToHomeScreen(List<MedicationPojo> meds);
}
