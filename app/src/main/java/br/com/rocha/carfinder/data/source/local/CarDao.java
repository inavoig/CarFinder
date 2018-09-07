package br.com.rocha.carfinder.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.rocha.carfinder.data.Car;

@Dao
public interface CarDao {

    @Query("SELECT * FROM Car")
    List<Car> getCars();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Car car);
}
