package com.iti.mad42.remedicine.Model.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.iti.mad42.remedicine.Model.pojo.User;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMedfriendUser(User user);
}
