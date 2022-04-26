package com.iti.mad42.remedicine.myAccount.View;

import com.iti.mad42.remedicine.data.pojo.MedicationPojo;
import com.iti.mad42.remedicine.data.pojo.User;

import java.util.List;

public interface MyAccountFragmentInterface {
    void showAllUsers(List<User> users);
    void sendAllMedsToHomeScreen(List<MedicationPojo> meds);
}
