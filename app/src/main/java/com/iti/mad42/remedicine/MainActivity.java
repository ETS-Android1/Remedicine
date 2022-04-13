package com.iti.mad42.remedicine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.iti.mad42.remedicine.MyAccount.View.MyAccountFragment;
import com.iti.mad42.remedicine.ShowMedications.View.ShowMedicationsFragment;

public class MainActivity extends AppCompatActivity {


    //for medications fragment
//    ShowMedicationsFragment fragment;
//    FragmentManager mgr;
//    FragmentTransaction transaction;

    MyAccountFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for medications fragment
//        mgr = getSupportFragmentManager();
//        if(savedInstanceState == null){
//            fragment = new ShowMedicationsFragment();
//            transaction = mgr.beginTransaction();
//            transaction.setReorderingAllowed(true);
//            transaction.add(R.id.activeFragment,fragment,"ActiveFragment");
//            transaction.commit();
//        }else {
//            fragment = (ShowMedicationsFragment) mgr.findFragmentByTag("ActiveFragment");
//        }
        
    }
}