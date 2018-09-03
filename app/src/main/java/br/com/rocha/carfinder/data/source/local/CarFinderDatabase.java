package br.com.rocha.carfinder.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.com.rocha.carfinder.data.Car;

@Database(entities = {Car.class}, version = 1)
public abstract class CarFinderDatabase extends RoomDatabase {

    private static CarFinderDatabase INSTANCE;

    public abstract CarDao carDao();

    private static final Object sLock = new Object();

    public static CarFinderDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        CarFinderDatabase.class, "car_finder.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}
