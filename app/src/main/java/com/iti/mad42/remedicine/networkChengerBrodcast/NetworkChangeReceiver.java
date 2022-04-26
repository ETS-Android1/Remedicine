package com.iti.mad42.remedicine.networkChengerBrodcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.CallbackManager;
import com.iti.mad42.remedicine.data.localDataSource.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.data.pojo.MedicationPojo;
import com.iti.mad42.remedicine.data.repositry.Repository;
import com.iti.mad42.remedicine.data.repositry.RepositoryInterface;
import com.iti.mad42.remedicine.data.remoteDataSource.RemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class NetworkChangeReceiver extends BroadcastReceiver {

    RepositoryInterface repo;
    public static boolean isConnected = false;
    Single<List<MedicationPojo>> medicationSingleList;
    List<MedicationPojo> medList = new ArrayList<>();
    @Override
    public void onReceive(final Context context, final Intent intent) {
        repo= Repository.getInstance(context, ConcreteLocalDataSource.getInstance(context), RemoteDataSource.getInstance(context, new CallbackManager() {
            @Override
            public boolean onActivityResult(int i, int i1, @Nullable Intent intent) {
                return false;
            }
        }));

        int status = NetworkUtility.getConnectivityStatusString(context);
        Log.e("mando", "network");
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == NetworkUtility.NETWORK_STATUS_NOT_CONNECTED) {
                Log.e("mando", "no network");
                isConnected =false;
            } else {
                isConnected = true;
                sync();
                Log.e("mando", "network back");
            }
        }
    }
    public void sync(){
        Log.e("mando", "sync: " );
        new Thread(new Runnable() {
            @Override
            public void run() {
                medicationSingleList = repo.getAllMedicationsList();
                medicationSingleList.subscribe(new SingleObserver<List<MedicationPojo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<MedicationPojo> medicationPojos) {
                        medList = medicationPojos;
                        Log.e("mando", "onSuccess:" +medicationPojos.size());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("mando", "onError:" +e.toString());
                    }
                });
                if(!medList.isEmpty()){
                    for (MedicationPojo med : medList){
                        Log.e("mando", "onSuccess: "+med );
                        repo.updateMedicationToFirebase(med);
                    }
                }
            }
        }).start();
    }
}
