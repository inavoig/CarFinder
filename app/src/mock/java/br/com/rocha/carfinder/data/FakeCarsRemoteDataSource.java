package br.com.rocha.carfinder.data;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.Map;

import br.com.rocha.carfinder.data.source.CarsDataSource;

public class FakeCarsRemoteDataSource implements CarsDataSource {

    private static FakeCarsRemoteDataSource INSTANCE;

    private static final Map<String, Car> CARS_SERVICE_DATA = new LinkedHashMap<>();

    private FakeCarsRemoteDataSource() {
    }

    public static FakeCarsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeCarsRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getCars(@NonNull LoadCarsCallback callback) {
        callback.onCarsLoaded(Lists.newArrayList(CARS_SERVICE_DATA.values()));
    }

    @Override
    public void saveCar(@NonNull Car car) {
        throw new UnsupportedOperationException("Saving operation not allowed");
    }
}
