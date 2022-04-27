package com.mc2022.template;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WifiModel.class}, version = 1)
public abstract class DatabaseClass extends RoomDatabase {
    public abstract DaoClass getDao();

    public static DatabaseClass myDatabaseinstance;

    public static  DatabaseClass getInstance(Context context){

        if(myDatabaseinstance == null){
            myDatabaseinstance = Room.databaseBuilder(context.getApplicationContext(),
                    DatabaseClass.class,
                    "my_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }

        return myDatabaseinstance;
    }

}

