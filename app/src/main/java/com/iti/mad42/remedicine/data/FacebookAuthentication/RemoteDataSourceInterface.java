package com.iti.mad42.remedicine.data.FacebookAuthentication;

import android.content.Context;

import com.facebook.AccessToken;
import com.iti.mad42.remedicine.Model.database.LocalDatabaseSourceInterface;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.OnlineDataInterface;
import com.iti.mad42.remedicine.Model.pojo.RequestPojo;
import com.iti.mad42.remedicine.Model.pojo.User;

public interface RemoteDataSourceInterface {

    public void registerListeners();

    public void unregisterListeners();

    public void handleFacebookToken(AccessToken token, NetworkDelegate networkDelegate, Context context);

    public void addMedicationToFirebase(MedicationPojo med);
    public void deleteFromFirebase();
    public void updateMedicationToFirebase(MedicationPojo med);
    public void deleteMedicationFromFirebase(MedicationPojo med);


    public void sendRequest(RequestPojo request);
    public void getAllRequestsForReceiver(String receiverEmail);
    public void setNetworkDelegate(NetworkDelegate networkDelegate);
    public void changeRequestStateWhenAccept(RequestPojo request);
    public void rejectRequest(RequestPojo request);
    //public void getUserData(String senderEmail);
    public void getAllMedicationFromFBForCurrentMedOwner(String medOwnerEmail, OnlineDataInterface onlineDataInterface);
}
