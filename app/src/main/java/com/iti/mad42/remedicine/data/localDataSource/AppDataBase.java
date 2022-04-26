package com.iti.mad42.remedicine.data.localDataSource;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.iti.mad42.remedicine.data.pojo.Converters;
import com.iti.mad42.remedicine.data.pojo.MedicationPojo;
import com.iti.mad42.remedicine.data.pojo.User;

@TypeConverters(Converters.class)
@Database(entities = {MedicationPojo.class, User.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    private static final String DATABASE_NAME = "Med_DB";

    private static AppDataBase instance = null;
    public abstract MedicineDAO medicineDAO();
    public abstract UserDAO userDAO();
    public static synchronized AppDataBase getInstance(Context context){
        if(instance ==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class, DATABASE_NAME).build();
        }
        return instance;
    }
}
