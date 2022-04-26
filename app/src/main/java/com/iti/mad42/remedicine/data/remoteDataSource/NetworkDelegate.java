package com.iti.mad42.remedicine.data.remoteDataSource;

import com.iti.mad42.remedicine.data.pojo.MedicationPojo;
import com.iti.mad42.remedicine.data.pojo.RequestPojo;
import com.iti.mad42.remedicine.data.pojo.User;

import java.util.List;

public interface NetworkDelegate {
    public void saveString (String key ,String value);
    public void navigateToHome();
    public void successReturnRequests(List<RequestPojo> requests);
    public void insertMedFriend(User user);
    public void successReturnMedications(List<MedicationPojo> meds);
}
