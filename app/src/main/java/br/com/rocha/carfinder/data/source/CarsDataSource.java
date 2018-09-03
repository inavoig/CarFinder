package br.com.rocha.carfinder.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.rocha.carfinder.data.Car;

public interface CarsDataSource {
    interface LoadCarsCallback {

        void onCarsLoaded(List<Car> cars);

        void onDataNotAvailable();
    }

    void getCars(@NonNull LoadCarsCallback callback);

    void saveCar(@NonNull Car car);
}
