package com.iti.mad42.remedicine.data.FacebookAuthentication;

import com.facebook.AccessToken;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.RequestPojo;

import java.util.List;

public interface RemoteDataSourceInterface {

    public void registerListeners();
    public void unregisterListeners();
    public void handleFacebookToken(AccessToken token,NetworkDelegate networkDelegate);
    public void addMedicationToFirebase(MedicationPojo med);
    public void updateMedicationToFirebase(MedicationPojo med);
    public void sendRequest(RequestPojo request);
    public void getAllRequestsForReceiver(String receiverEmail);
    public void setNetworkDelegate(NetworkDelegate networkDelegate);
    public void changeRequestStateWhenAccept(RequestPojo request);
    public void rejectRequest(RequestPojo request);
}
