package br.com.rocha.carfinder.data.source.remote;

import android.support.annotation.NonNull;

import br.com.rocha.carfinder.data.Car;
import br.com.rocha.carfinder.data.source.CarsDataSource;

public class CarsRemoteDataSource implements CarsDataSource {

    private static CarsRemoteDataSource INSTANCE;

    private CarsRemoteDataSource() {
    }

    public static CarsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CarsRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getCars(@NonNull LoadCarsCallback callback) {
        new ImportCarsTask(callback).execute();
    }

    @Override
    public void saveCar(@NonNull Car car) {
        throw new UnsupportedOperationException("Saving operation not allowed");
    }
}
