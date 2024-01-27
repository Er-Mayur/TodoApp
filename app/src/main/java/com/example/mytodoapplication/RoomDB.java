package com.example.mytodoapplication;

//add database entities
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {MainData.class},version = 1,exportSchema = false)

public abstract class RoomDB extends RoomDatabase {
    //cretae atabse instance

    private static RoomDB databse;

    //define db name

    private static String DATABASE_NAME="database";

    public  synchronized static RoomDB getInstance(Context context){
        if(databse==null){
            //when db is null
            //initialize dab
            databse= Room.databaseBuilder(context.getApplicationContext(),RoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        //return db
        return databse;
    }

    //create Dao

    public abstract MainDao mainDao();

}
