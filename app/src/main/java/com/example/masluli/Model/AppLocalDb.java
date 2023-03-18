package com.example.masluli.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.masluli.Converters;
import com.example.masluli.MyApplication;

@Database(entities = {Maslul.class}, version = 3)
@TypeConverters({Converters.class})
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract MaslulDao maslulDao();
}
public class AppLocalDb {
    static public AppLocalDbRepository getAppDb() {
        return Room.databaseBuilder(MyApplication.getContext(),
                        AppLocalDbRepository.class,
                        "dbFileName.db")
                .fallbackToDestructiveMigration()
                .build();
    }

}
