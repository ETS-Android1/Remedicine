package com.iti.mad42.remedicine.home.presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.iti.mad42.remedicine.networkChengerBrodcast.NetworkChangeReceiver;
import com.iti.mad42.remedicine.data.pojo.CurrentUser;
import com.iti.mad42.remedicine.data.pojo.HomeParentItem;
import com.iti.mad42.remedicine.data.pojo.MedicationPojo;
import com.iti.mad42.remedicine.data.pojo.MedicineDose;
import com.iti.mad42.remedicine.data.repositry.OnlineDataInterface;
import com.iti.mad42.remedicine.data.repositry.Repository;
import com.iti.mad42.remedicine.utility.Utility;
import com.iti.mad42.remedicine.WorkManger.MyPeriodicWorkManger;
import com.iti.mad42.remedicine.home.view.HomeFragmentInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Single;

public class HomePresenter implements HomePresenterInterface, OnlineDataInterface {

    private Context context;
    private HomeFragmentInterface view;
    private Repository repo;
    Single<List<MedicationPojo>> medicationSingleList;
    List<MedicationPojo> medList = new ArrayList<>();

    public HomePresenter(Context context, HomeFragmentInterface view, Repository repo) {
        this.context = context;
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void getAlMedicines() {
        view.showData(repo.getAllMedications());
    }


    public void filterMedicationByDay(List<MedicationPojo> medicationList, String date) {

        if (medicationList != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<MedicationPojo> medicinesPerDay = medicationList.stream().filter(medicine -> medicine.getMedDays().contains(date.trim())).collect(Collectors.toList());
                for (MedicationPojo m : medicinesPerDay) {
                    System.out.println(m);
                }
                buildArrayOfTimes(medicinesPerDay, date);
            }
        }
    }



    public void buildArrayOfTimes(List<MedicationPojo> medicines, String date) {
        Log.e("mando", "buildArrayOfTimes: "+medicines.size());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            List<Long> arrayOdTimes = new ArrayList<Long>();
            List<Long> distinctArrayOfTimes;
            for (MedicationPojo m : medicines) {
                for (MedicineDose medicineDose : m.getMedDoseReminders()) {
                    arrayOdTimes.add(medicineDose.getDoseTimeInMilliSec());
                }
            }
            distinctArrayOfTimes = arrayOdTimes.stream().distinct().collect(Collectors.toList());
            Collections.sort(distinctArrayOfTimes);
            for (Long m : distinctArrayOfTimes) {
                System.out.println(Utility.millisToTimeAsString(m) + "<<<<<<<<<<<<<<<<<<<<<");
            }
            matchMedicineToTime(medicines,distinctArrayOfTimes,date);
        }
    }

    public void matchMedicineToTime(List<MedicationPojo> medicines, List<Long> times, String date) {
        ArrayList<ArrayList<MedicationPojo>> listsOfMedicines = new ArrayList<ArrayList<MedicationPojo>>();
        for (Long time : times) {
            ArrayList<MedicationPojo> list = new ArrayList<MedicationPojo>();
            for (MedicationPojo m : medicines) {
                for (MedicineDose md : m.getMedDoseReminders()) {
                    if (md.getDoseTimeInMilliSec() == time) {
                        list.add(m);
                    }
                }
            }
            listsOfMedicines.add(list);
        }
        printLists(listsOfMedicines);
        ParentItemList(listsOfMedicines,times,date);
    }

    public void printLists(ArrayList<ArrayList<MedicationPojo>> lists) {
        for (ArrayList<MedicationPojo> l : lists) {
            for (MedicationPojo m : l) {
                System.out.println(m);
            }
            System.out.println("\n");
        }
    }

    private void ParentItemList(ArrayList<ArrayList<MedicationPojo>> lists, List<Long> times, String date ) {

        List<HomeParentItem> itemList = new ArrayList<>();
        for (int x = 0; x < times.size(); x++) {
            HomeParentItem item = new HomeParentItem(times.get(x), lists.get(x));
            itemList.add(item);
        }
        view.setParentItemAdapter(itemList, date);
    }

    @Override
    public void updateMedication(MedicationPojo medication) {
        if (getSharedPref().equals(CurrentUser.getInstance().getEmail())) {
            repo.insertMedication(medication);
            repo.updateMedicationToFirebase(medication);
        }else {
            if (NetworkChangeReceiver.isConnected){
                repo.updateMedicationToFirebase(medication);
            }else {
                Toast.makeText(context,"Please check your network connection",Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void getOnlineData(String medFriendEmail) {
        repo.getAllMedicationFromFBForCurrentMedOwner(medFriendEmail,this);
    }

    @Override
    public void onlineDataResult(List<MedicationPojo> friendMedications) {
        Log.e("mando", "onlineDataResult: "+friendMedications.size() );

        if (getSharedPref().equals(CurrentUser.getInstance().getEmail())) {
            for (MedicationPojo med : friendMedications){
                repo.insertMedication(med);
            }
            setWorkTimer();
        }

        view.getOnlineData(friendMedications);
    }

//    @Override
//    public void medDataResult(MedicationPojo medicationPojo) {
//
//    }

    private void setWorkTimer() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyPeriodicWorkManger.class,
                15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork("Counter", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }
    public String getSharedPref() {
        SharedPreferences prefs = context.getSharedPreferences("LoginTest", MODE_PRIVATE);
        return prefs.getString(Utility.myCredentials, "No user registered");
    }
}
