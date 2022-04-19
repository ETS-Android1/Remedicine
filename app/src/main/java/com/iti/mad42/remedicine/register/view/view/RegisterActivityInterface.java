package com.iti.mad42.remedicine.register.view.view;

import android.content.Intent;

import com.iti.mad42.remedicine.homeRecyclerView.view.HomeRecyclerView;

public interface RegisterActivityInterface {
    public void navigateToHome();
    public void showToast(String message);
    public void updateUIToShowError();
}
