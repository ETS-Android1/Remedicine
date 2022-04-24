package com.iti.mad42.remedicine.homeRecyclerView.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.iti.mad42.remedicine.MyAccount.View.MyAccountFragment;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.ShowMedications.View.ShowMedicationsFragment;

public class HomeRecyclerView extends AppCompatActivity {

    BubbleNavigationLinearView bubbleNavigationLinearView;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_recycler_view);


        bubbleNavigationLinearView = findViewById(R.id.bubbleNavigationBar);
        replaceFragments(new HomeFragment());

        bubbleNavigationLinearView.setNavigationChangeListener((view, position) -> {
            switch (position) {
                case 0:
                    replaceFragments(new HomeFragment());
                    break;
                case 1:
                    replaceFragments(new ShowMedicationsFragment());
                    break;
                case 2:
                    replaceFragments(new MyAccountFragment());
                    break;
            }
        });

    }

    private void replaceFragments(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}