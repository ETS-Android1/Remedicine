package com.iti.mad42.remedicine.data.FacebookAuthentication;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.mad42.remedicine.Model.database.LocalDatabaseSourceInterface;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.OnlineDataInterface;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.Model.pojo.RequestPojo;
import com.iti.mad42.remedicine.Model.pojo.User;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.login.view.presenter.LoginPresenterInterface;

import java.util.ArrayList;
import java.util.List;

public class RemoteDataSource implements RemoteDataSourceInterface {

    private Context context;
    private FirebaseAuth firebaseAuth;
    public static final String TAG = "Facebook Authentication";
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker tracker;
    private FirebaseUser user;
    private static RemoteDataSource instance = null;
    private DatabaseReference databaseReferenceUser;
    private DatabaseReference databaseReferenceMedication;
    private DatabaseReference databaseReferenceRequests;
    private NetworkDelegate networkDelegate;

    private RemoteDataSource(Context context, CallbackManager callbackManager) {
        firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");
        databaseReferenceMedication = FirebaseDatabase.getInstance().getReference("meds");
        databaseReferenceRequests = FirebaseDatabase.getInstance().getReference("requests");
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        callbackManager = callbackManager;
        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                getUserInfo(user);
            } else {
                getUserInfo(null);
            }
        };
        tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(@Nullable AccessToken accessToken, @Nullable AccessToken accessToken1) {
                if (accessToken == null) {
                    firebaseAuth.signOut();
                }
            }
        };
    }

    public static RemoteDataSource getInstance(Context context, CallbackManager callbackManager) {
        if (instance == null) {
            instance = new RemoteDataSource(context, callbackManager);
        }
        return instance;
    }

    @Override
    public void setNetworkDelegate(NetworkDelegate networkDelegate) {
        this.networkDelegate = networkDelegate;
    }

    public void registerListeners() {
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void unregisterListeners() {
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void handleFacebookToken(AccessToken token, NetworkDelegate networkDelegate) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "sign in with credential: successful ");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    addUserToFirebase(networkDelegate, new User(user.getEmail(), user.getDisplayName(), ""));
                } else {
                    Log.i(TAG, "sign in with credential: failed ", task.getException());
                }
            }
        });
    }

    private void getUserInfo(FirebaseUser user) {
        if (user != null) {
            Log.i(TAG, "updateUI with: " + user.getEmail());
            Log.i(TAG, "updateUI with: " + user.getDisplayName());
            Log.i(TAG, "updateUI with: " + user.getUid());
            Log.i(TAG, "updateUI with: " + user.getProviderId());
            Log.i(TAG, "updateUI with: " + user.getPhoneNumber());
        }
    }

    private void addUserToFirebase(NetworkDelegate networkDelegate, User user) {
        String id = databaseReferenceUser.push().getKey();
        databaseReferenceUser.orderByChild("email")
                .equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(context, "This email is already registered", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReferenceUser.child(id).setValue(user);
                    networkDelegate.saveString(Utility.myCredentials, user.getEmail().toString().trim());
                    networkDelegate.navigateToHome();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void addMedicationToFirebase(MedicationPojo med) {
        String id = databaseReferenceMedication.push().getKey();
        databaseReferenceMedication.orderByChild("name").equalTo(med.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(context, "This Medication is already added", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReferenceMedication.child(id).setValue(med);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void updateMedicationToFirebase(MedicationPojo med) {
        databaseReferenceMedication.orderByChild("medOwnerEmail").equalTo(getString(Utility.myCredentials)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        MedicationPojo medicationPojo = postSnapshot.getValue(MedicationPojo.class);
                        if (medicationPojo.getName().equals(med.getName())) {
                            databaseReferenceMedication.child(postSnapshot.getKey()).setValue(med);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteMedicationFromFirebase(MedicationPojo med) {
        databaseReferenceMedication.orderByChild("medOwnerEmail").equalTo(getString(Utility.myCredentials)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        MedicationPojo medicationPojo = postSnapshot.getValue(MedicationPojo.class);
                        if (medicationPojo.getName().equals(med.getName())) {
                            databaseReferenceMedication.child(postSnapshot.getKey()).removeValue();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getString(String key) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("LoginTest", MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }


    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("LoginTest", MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public void sendRequest(RequestPojo request) {
        String id = databaseReferenceRequests.push().getKey();
        databaseReferenceUser.orderByChild("email")
                .equalTo(request.getRecieverEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    databaseReferenceRequests.orderByChild("recieverEmail").equalTo(request.getRecieverEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                boolean isFound = false;
                                Log.i("sandra", "Error Here before for" + snapshot.toString());
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    RequestPojo requestPojo = snapshot1.getValue(RequestPojo.class);
                                    if (request.getSenderEmail().equals(requestPojo.getSenderEmail())) {
                                        isFound = true;
                                        Toast.makeText(context, "Request is already exists", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                                if (!isFound) {
                                    databaseReferenceRequests.child(id).setValue(request);
                                }
                            } else {
                                databaseReferenceRequests.child(id).setValue(request);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "Something went wrong. Error is: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, "This Email Doesn't Exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    public void getAllRequestsForReceiver(String receiverEmail) {
        List<RequestPojo> requests = new ArrayList<>();
        databaseReferenceRequests.orderByChild("recieverEmail").equalTo(receiverEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    requests.clear();
                    for (DataSnapshot requestsSnapshot : snapshot.getChildren()) {
                        RequestPojo request = requestsSnapshot.getValue(RequestPojo.class);
                        if (!request.getState().equals("friend")) {
                            requests.add(request);
                        }
                    }
                } else {
                    Toast.makeText(context, "There is no requests to show.", Toast.LENGTH_SHORT).show();
                }
                //call method that will set the adapter with the list requests.
                networkDelegate.successReturnRequests(requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void changeRequestStateWhenAccept(RequestPojo request) {
        Log.e(TAG, "changeRequestStateWhenAccept: " + request.getSenderEmail());
        databaseReferenceRequests.orderByChild("recieverEmail").equalTo(request.getRecieverEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot requestsSnapshot : snapshot.getChildren()) {
                        RequestPojo requestPojo = requestsSnapshot.getValue(RequestPojo.class);
                        if (request.getRecieverEmail().equals(requestPojo.getRecieverEmail()) && request.getSenderEmail().equals(requestPojo.getSenderEmail())) {
                            String id = requestsSnapshot.getKey();
                            Log.e("sandra", "Request id : " + id);
                            requestPojo.setState("friend");
                            databaseReferenceRequests.child(id).setValue(requestPojo);
                            saveString(Utility.currentMedFriend, requestPojo.getSenderEmail());
                            String medEmail = getString(Utility.currentMedFriend);
                            Log.e("sandra", "medEmail is : " + medEmail);
                            networkDelegate.insertMedFriend(new User(medEmail, "", ""));
                            Toast.makeText(context, "Request accepted.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void rejectRequest(RequestPojo request) {
        databaseReferenceRequests.orderByChild("recieverEmail").equalTo(request.getRecieverEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Log.e("sandra", "before if receiver email");
                    Log.e("sandra", "before if receiver email :" + request.getRecieverEmail());
                    Log.e("sandra", "before if sender email" + request.getSenderEmail());
                    Log.e("sandra", "before if sender email : " + snapshot.getValue(RequestPojo.class).getSenderEmail());
                    RequestPojo requestPojo = snapshot1.getValue(RequestPojo.class);
                    if (request.getRecieverEmail().equals(requestPojo.getRecieverEmail()) && request.getSenderEmail().equals(requestPojo.getSenderEmail())) {
                        String id = snapshot1.getKey();
                        Log.e("sandra", "Request id : " + id);
                        databaseReferenceRequests.child(id).removeValue();
                        Toast.makeText(context, "Request has been rejected.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    @Override
//    public void getUserData(String senderEmail) {
//        databaseReferenceUser.orderByChild("email").equalTo(senderEmail).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
//                        if(senderEmail.equals(snapshot1.getValue(User.class).getEmail())){
//                            User senderUser = snapshot1.getValue(User.class);
//                            Log.e("sandra", "User is : "+senderUser);
//                            Log.e("sandra", "local : "+networkDelegate);
//                            networkDelegate.insertMedFriend(senderUser);
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


    @Override
    public void getAllMedicationFromFBForCurrentMedOwner(String medOwnerEmail, OnlineDataInterface onlineDataInterface) {
        List<MedicationPojo> friendMedications = new ArrayList<>();
        databaseReferenceMedication.orderByChild("medOwnerEmail").equalTo(medOwnerEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    friendMedications.clear();
                    for (DataSnapshot medSnapshot : snapshot.getChildren()) {
                        MedicationPojo medication = medSnapshot.getValue(MedicationPojo.class);
                        String id = medSnapshot.getKey();
                        if (medication.getMedOwnerEmail().equals(medOwnerEmail)) {
                            friendMedications.add(medication);
                        }
                    }
                    onlineDataInterface.onlineDataResult(friendMedications);
                } else {
                    Toast.makeText(context, "There is no Medications to show.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
