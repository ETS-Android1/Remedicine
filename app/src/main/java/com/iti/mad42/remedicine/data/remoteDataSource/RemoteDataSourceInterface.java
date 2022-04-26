package com.iti.mad42.remedicine.data.remoteDataSource;

import android.content.Context;

import com.facebook.AccessToken;
import com.iti.mad42.remedicine.data.pojo.MedicationPojo;
import com.iti.mad42.remedicine.data.repositry.OnlineDataInterface;
import com.iti.mad42.remedicine.data.pojo.RequestPojo;

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
//    public void getMedicationFromFB(String medOwnerEmail,String medName, OnlineDataInterface onlineDataInterface);
}
