package com.iti.mad42.remedicine.data.FacebookAuthentication;

import com.facebook.AccessToken;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;

public interface RemoteDataSourceInterface {

    public void registerListeners();

    public void unregisterListeners();

    public void handleFacebookToken(AccessToken token,NetworkDelegate networkDelegate);

    public void addMedicationToFirebase(MedicationPojo med);

}
