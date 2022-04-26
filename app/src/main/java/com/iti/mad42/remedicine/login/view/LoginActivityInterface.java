package com.iti.mad42.remedicine.login.view;

public interface LoginActivityInterface {
    public void navigateToHome();
    public void updateUIToShowError(String message);
    public void showToast(String message);
    public void requestFocusFor(String viewName);
}
