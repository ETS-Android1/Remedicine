package com.iti.mad42.remedicine.homeRecyclerView.presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.lifecycle.Observer;

import com.iti.mad42.remedicine.Model.pojo.CurrentUser;
import com.iti.mad42.remedicine.Model.pojo.HomeParentItem;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.MedicineDose;
import com.iti.mad42.remedicine.Model.pojo.OnlineDataInterface;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.homeRecyclerView.view.HomeFragmentInterface;
import com.iti.mad42.remedicine.homeRecyclerView.view.HomeParentItemAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HomePresenter implements HomePresenterInterface, OnlineDataInterface {

    private Context context;
    private HomeFragmentInterface view;
    private Repository repo;

    public HomePresenter(Context context, HomeFragmentInterface view, Repository repo) {
        this.context = context;
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void getAlMedicines() {
        view.showData(repo.getAllMedications());
    }

    public void setCurrentUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginTest",MODE_PRIVATE);
        CurrentUser.getInstance().setEmail(sharedPreferences.getString(Utility.myCredentials,null));
    }

    public void filterMedicationByDay(List<MedicationPojo> medicationList, String date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<MedicationPojo> medicinesPerDay = medicationList.stream().filter(medicine -> medicine.getMedDays().contains(date.trim())).collect(Collectors.toList());
            for (MedicationPojo m : medicinesPerDay) {
                System.out.println(m);
            }
            buildArrayOfTimes(medicinesPerDay, date);
        }
    }



    public void buildArrayOfTimes(List<MedicationPojo> medicines, String date) {
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
        repo.updateMedication(medication);
    }

    @Override
    public void getOnlineData(String medFriendEmail) {
        repo.getAllMedicationFromFBForCurrentMedOwner(medFriendEmail,this);
    }


    @Override
    public void onlineDataResult(List<MedicationPojo> friendMedications) {
        view.getOnlineData(friendMedications);
    }
}
