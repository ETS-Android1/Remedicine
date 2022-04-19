package com.iti.mad42.remedicine.MyAccount.Presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.iti.mad42.remedicine.MyAccount.View.MyAccountFragmentInterface;

public class MyAccountPresenter implements MyAccountPresenterInterface {

    Context context;
    MyAccountFragmentInterface view;

    public MyAccountPresenter(Context context, MyAccountFragmentInterface view) {
        this.context = context;
        this.view = view;
    }

    public void saveString (String key , String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("LoginTest",MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.apply();
    }

}
